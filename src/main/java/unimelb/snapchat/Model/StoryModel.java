package unimelb.snapchat.Model;

/**
 * Created by Xiaoyu on 10/11/2016.
 */
public class StoryModel {
    String username;
    String title;
    String content;
    String url;
    String date;

    public StoryModel(String username, String title, String content, String url, String date) {
        this.username = username;
        this.title = title;
        this.content = content;
        this.url = url;
        this.date = date;
    }


    public StoryModel(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
