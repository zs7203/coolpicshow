package toy.hellozs.com.coolpicshow.net;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import toy.hellozs.com.coolpicshow.bean.Album;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public abstract class AlbumRequest extends Request<List<Album>> {

    public AlbumRequest(String id) {

        super(Method.GET, id, null);
    }

    @Override
    protected Response<List<Album>> parseNetworkResponse(NetworkResponse response) {
        try {
            List<Album> photos = new ArrayList<>();
            String html = new String(response.data, "utf-8");
            Document document = Jsoup.parse(html);


            for (Element post : document.select("div#mainbox div.post")) {
                String cover = post.select("img").attr("src");
                String link = post.select("a[title]").attr("href");
                String title = post.select("a[title]").attr("title");
                Log.d("test", cover + ":" + link + ":" + title);

                Album album = new Album();
                album.setCover(cover);
                album.setTitle(title);
                album.setLink(link);
                photos.add(album);
            }

            return Response.success(photos, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    public void enqueue(RequestQueue queue) {
        queue.add(this);
    }

}
