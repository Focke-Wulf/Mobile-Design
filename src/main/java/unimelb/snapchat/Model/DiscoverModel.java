package unimelb.snapchat.Model;

/**
 * Created by Xiaoyu on 10/17/2016.
 */
public class DiscoverModel {
    private String url;
    private String label;

    public DiscoverModel(String url, String label) {
        this.url = url;
        this.label = label;
    }
    public DiscoverModel(){}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
