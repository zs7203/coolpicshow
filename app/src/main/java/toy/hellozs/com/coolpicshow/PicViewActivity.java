package toy.hellozs.com.coolpicshow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import toy.hellozs.com.coolpicshow.bean.Pic;
import toy.hellozs.com.coolpicshow.net.PicRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PicViewActivity extends AppCompatActivity {

    public static final String POST_URL = "post_url";
    FloatingActionButton fab_play;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<Pic> mPics;
    private RequestQueue mQueue;
    private String post_url;
    private CoordinatorLayout rootLayout;
    private boolean is_playing = false;
    private Handler player = new Handler(Looper.getMainLooper());
    private Runnable auto_play = new Runnable() {
        @Override
        public void run() {
            int index = mViewPager.getCurrentItem();
            index = (index == mPics.size() - 1 ? 0 : index + 1);
            mViewPager.setCurrentItem(index);
            player.postDelayed(auto_play, TimeUnit.SECONDS.toMillis(5));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_view);
        initView();
        post_url = getIntent().getStringExtra(POST_URL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new PicRequest(post_url) {
            @Override
            protected void deliverResponse(List<Pic> response) {
                if (response.size() == 0) {
                    Toast.makeText(PicViewActivity.this, "无法载入图片！", Toast.LENGTH_LONG);
                } else
                    mSectionsPagerAdapter.setList(response);
            }

            @Override
            public void deliverError(VolleyError error) {
            }
        }.enqueue(mQueue);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rootLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        fab_play = (FloatingActionButton) findViewById(R.id.fab);
        fab_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_playing) {
                    is_playing = false;
                    pause();
                    fab_play.setImageResource(android.R.drawable.ic_media_play);
                    Snackbar.make(rootLayout, "暂停播放..", Snackbar.LENGTH_LONG).show();
                } else {
                    is_playing = true;
                    play();
                    fab_play.setImageResource(android.R.drawable.ic_media_pause);
                    Snackbar.make(rootLayout, "自动播放中..", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        mQueue = Volley.newRequestQueue(this);
    }

    public Pic getPic(int position) {
        return mPics.get(position);
    }

    public int getPicCount() {
        return mPics.size();
    }

    private void play() {
        player.postDelayed(auto_play, TimeUnit.SECONDS.toMillis(5));
    }

    private void pause() {
        player.removeCallbacksAndMessages(null);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(AlbumFragment.ARG_INDEX, getIntent().getIntExtra(AlbumFragment.ARG_INDEX, 0));
        intent.putExtra(PicViewFragment.POSITION, getIntent().getIntExtra(PicViewFragment.POSITION, 0));
        setResult(RESULT_OK, intent);
        super.finish();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PicViewFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mPics == null ? 0 : mPics.size();
        }

        public void setList(List<Pic> list) {
            mPics = list;
            notifyDataSetChanged();
        }
    }
}
