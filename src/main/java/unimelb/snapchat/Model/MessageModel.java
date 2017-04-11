package unimelb.snapchat.Model;

/**
 * Created by Junwen on 10/15/2016.
 */
public class MessageModel  {


    private String content;
    private String time;
    private String identiy;
    private String url;


    public MessageModel(String content, String time, String identiy, String url) {
        this.content = content;
        this.time = time;
        this.identiy = identiy;
        this.url = url;
    }


    public MessageModel(){}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIdentiy() {
        return identiy;
    }

    public void setIdentiy(String identiy) {
        this.identiy = identiy;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
