package toy.hellozs.com.coolpicshow.net;

import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley. Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import toy.hellozs.com.coolpicshow.bean.Pic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public abstract class PicRequest extends Request<List<Pic>> {

    public PicRequest(String url) {

        super(Method.GET, url, null);
    }

    @Override
    protected Response<List<Pic>> parseNetworkResponse(NetworkResponse response) {
        try {
            List<Pic> pics = new ArrayList<>();

            Document document = Jsoup.parse(new String(response.data, "utf-8"));

            String post_title = document.select("h1").text();
            String url = null;
            StringBuilder content = new StringBuilder();
            int count = 0;
            int id = 1;
            Elements posts = document.select("div.content-c p");
            for (Element post : posts) {
                count++;
                String s1 = post.select("img").attr("src");
                if (!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(url)) {
                    if (count > 1) {
                        Pic pic = new Pic();
                        pic.setId(id);
                        pic.setTitle(post_title);
                        pic.setUrl(url);
                        pic.setContent(TextUtils.isEmpty(content.toString()) ? post_title : content.toString());
                        pics.add(pic);
                        id++;
                        content.setLength(0);
                        url = s1;
                    }
                } else if (!TextUtils.isEmpty(s1)) {
                    url = s1;

                }
                String s2 = post.select("span").text();
                if (!TextUtils.isEmpty(s2)) {
                    content.append(s2);
                }

                if (posts.size() == count) {
                    Pic pic = new Pic();
                    pic.setId(id);
                    pic.setTitle(post_title);
                    pic.setUrl(url);
                    pic.setContent(TextUtils.isEmpty(content.toString()) ? post_title : content.toString());
                    pics.add(pic);
                }

            }

            return Response.success(pics, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    public void enqueue(RequestQueue queue) {
        queue.add(this);
    }

}
