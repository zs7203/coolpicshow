package toy.hellozs.com.coolpicshow;

import android.app.Application;
import android.content.Context;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import toy.hellozs.com.coolpicshow.bean.Album;

import java.util.List;

/**
 * Created by Administrator on 2015/12/20.
 */
public class CoolPicShowApplication extends Application {

    private RequestQueue queue;
    private LruCache<String, List<Album>> albumsLruCache = new LruCache<>(7);

    public static CoolPicShowApplication from(Context context) {
        return (CoolPicShowApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this);
    }

    public RequestQueue getRequestQueue() {
        return this.queue;
    }

    public LruCache<String, List<Album>> getAlbumsLruCache() {
        return this.albumsLruCache;
    }
}
