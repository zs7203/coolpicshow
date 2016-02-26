package toy.hellozs.com.coolpicshow.bean;

/**
 * Created by Administrator on 2015/12/12.
 */
public class Pic {

    private int id;
    private String title;
    private String url;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        String s = content.replaceAll("，|。|；| ", "\n");
        String s1 = s.replaceAll("？", "？\n");
        return s1.replaceAll("！", "！\n");
    }

    public void setContent(String content) {
        this.content = content;
    }
}
