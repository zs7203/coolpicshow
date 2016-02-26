package toy.hellozs.com.coolpicshow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import toy.hellozs.com.coolpicshow.bean.Album;
import toy.hellozs.com.coolpicshow.net.AlbumRequest;
import toy.hellozs.com.coolpicshow.util.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumFragment extends Fragment implements AlbumRecyclerViewAdapter.ImageClickListener, AlbumRecyclerViewAdapter.CheckIsEmptyListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARG_INDEX = "index";
    public static final int REQUEST_VIEW = 1;

    private int index;
    private CoolPicShowApplication application;
    private AlbumActivity activity;
    private GridLayoutManager layoutManager;
    private RecyclerView photos;
    private ImageView loadingImage;
    private AlbumRecyclerViewAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private int currentPage = 1;

    public AlbumFragment() {
        // Required empty public constructor
    }

    public static AlbumFragment newInstance(int i) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AlbumActivity) context;
        adapter = new AlbumRecyclerViewAdapter(getActivity(), this, this);
        application = CoolPicShowApplication.from(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    //重新载入相册列表
    @Override
    public void onResume() {
        super.onResume();
        refreshLayout.setRefreshing(false);
        final String category = Category.chooseCategory(index);
        List<Album> albumCache = application.getAlbumsLruCache().get(category);
        if (albumCache != null) {
            adapter.setList(albumCache);
        } else {
            new AlbumRequest(category) {
                @Override
                protected void deliverResponse(List<Album> response) {
                    adapter.setList(response);
                    application.getAlbumsLruCache().put(category, response);
                }

                @Override
                public void deliverError(VolleyError error) {
//                    Log.e(TAG, "error", error);
                }
            }.enqueue(application.getRequestQueue());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        refreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_dark, android.R.color.holo_blue_bright, android.R.color.holo_purple);//设置跑动的颜色值
        refreshLayout.setOnRefreshListener(this);
        adapter = new AlbumRecyclerViewAdapter(this.getActivity(), this, this);
        photos = (RecyclerView) view.findViewById(R.id.id_recyclerview);
        photos.setLayoutManager(layoutManager);
        photos.setAdapter(adapter);
        //当滑动到底部 ，弹出提示是否加载下一页
        photos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                    onBottomView();
                }
            }
        });
        loadingImage = (ImageView) view.findViewById(R.id.iv_loading);
        loadingImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Picasso.with(getActivity())
                .load(R.mipmap.github)
                .resize(100, 100)
                .centerInside()
                .into(loadingImage);
        return view;
    }

    @Override
    public void onImageClick(View view, String url, int position) {
        Intent intent = new Intent(getActivity(), PicViewActivity.class);
        intent.putExtra(PicViewActivity.POST_URL, url);
        intent.putExtra(AlbumFragment.ARG_INDEX, index);
        intent.putExtra(PicViewFragment.POSITION, position);
        startActivityForResult(intent, REQUEST_VIEW);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_VIEW:
                layoutManager.scrollToPosition(data.getIntExtra(PicViewFragment.POSITION, 0));
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    //下拉强制刷新
    @Override
    public void onRefresh() {
        final String category = Category.chooseCategory(index);
        new AlbumRequest(category) {
            @Override
            protected void deliverResponse(List<Album> response) {
                adapter.setList(response);
                application.getAlbumsLruCache().put(category, response);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void deliverError(VolleyError error) {
//                    Log.e(TAG, "error", error);
            }
        }.enqueue(application.getRequestQueue());
    }

    public void onLoad() {
        final String category = Category.chooseCategory(index);
        currentPage += 1;
        final String nextPage = category + "/page/" + currentPage;
        new AlbumRequest(nextPage) {
            @Override
            protected void deliverResponse(List<Album> response) {
                adapter.addToList(response);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void deliverError(VolleyError error) {
//                    Log.e(TAG, "error", error);
            }
        }.enqueue(application.getRequestQueue());
    }

    @Override
    public void onCheck(boolean isEmpty) {
        if (isEmpty) {
            loadingImage.setVisibility(View.VISIBLE);
            photos.setVisibility(View.GONE);
        } else {
            loadingImage.setVisibility(View.GONE);
            photos.setVisibility(View.VISIBLE);
        }
    }

    public void onBottomView() {
        Snackbar.make(((AlbumActivity) getActivity()).rootLayout, "已经到最后了！", Snackbar.LENGTH_LONG)
                .setAction("下一页", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLoad();
                    }
                }).show();
    }
}
