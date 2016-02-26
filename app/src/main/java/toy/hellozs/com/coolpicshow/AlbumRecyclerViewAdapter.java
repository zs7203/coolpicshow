package toy.hellozs.com.coolpicshow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import toy.hellozs.com.coolpicshow.bean.Album;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2015/12/9.
 */
public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Album> albums;
    private ImageClickListener clickListener;
    private CheckIsEmptyListener isEmptyListener;

    public AlbumRecyclerViewAdapter(Context context, ImageClickListener listener1, CheckIsEmptyListener listener2) {
        this.context = context;
        this.clickListener = listener1;
        this.isEmptyListener = listener2;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String title = albums.get(position).getTitle();
        holder.title.setText(title);
        String coverURL = albums.get(position).getCover();
        Picasso.with(context).load(coverURL).error(R.drawable.ic_menu_gallery).into(holder.cover);
        holder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onImageClick(v, albums.get(position).getLink(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = (albums == null ? 0 : albums.size());
        isEmptyListener.onCheck(count == 0 ? true : false);
        return count;
    }

    public void setList(List<Album> albumList) {
        this.albums = albumList;
        notifyDataSetChanged();
    }

    public void addToList(List<Album> albumList) {
        this.albums.addAll(albumList);
        notifyDataSetChanged();
    }

    public interface ImageClickListener {
        void onImageClick(View view, String post_url, int position);
    }

    public interface CheckIsEmptyListener {
        void onCheck(boolean isEmpty);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView cover;
        private TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.id_album_cover);
            title = (TextView) itemView.findViewById(R.id.id_album_title);
        }
    }
}
