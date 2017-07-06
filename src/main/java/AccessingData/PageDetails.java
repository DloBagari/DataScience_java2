package AccessingData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dlo on 05/07/17.
 */
public class PageDetails {
    private String url;
    private int position;
    private int page;
    private int titleLength;
    private int bodyContentLength;
    private boolean queryInTitle;
    private int numberOfHeaders;
    private int numberOfLinks;

    public  PageDetails( String url, int position, int serachPageNumber) {
        this.url =url;
        this.position = position;
        this.page =serachPageNumber;
    }

    public String toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("\"url\"", "\"" + this.url + "\"");
        map.put("\"position\"",this.position);
        map.put("\"page\"",this.page);
        map.put("\"titleLength\"",this.titleLength);
        map.put("\"bodyContentLength\"",this.bodyContentLength);
        map.put("\"queryInTitle\"",this.queryInTitle);
        map.put("\"numberOfHeaders\"",this.numberOfHeaders);
        map.put("\"numberOfLinks\"",this.numberOfLinks);
        return map.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTitleLength() {
        return titleLength;
    }

    public void setTitleLength(int titleLength) {
        this.titleLength = titleLength;
    }

    public int getBodyContentLength() {
        return bodyContentLength;
    }

    public void setBodyContentLength(int bodyContentLength) {
        this.bodyContentLength = bodyContentLength;
    }

    public boolean isQueryInTitle() {
        return queryInTitle;
    }

    public void setQueryInTitle(boolean queryInTitle) {
        this.queryInTitle = queryInTitle;
    }

    public int getNumberOfHeaders() {
        return numberOfHeaders;
    }

    public void setNumberOfHeaders(int numberOfHeaders) {
        this.numberOfHeaders = numberOfHeaders;
    }

    public int getNumberOfLinks() {
        return numberOfLinks;
    }

    public void setNumberOfLinks(int numberOfLinks) {
        this.numberOfLinks = numberOfLinks;
    }
}
