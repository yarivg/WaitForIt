package company.wfi.com.waitforit;


public class ItemInPlaylist {
    private String id;
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        if(this.getType().equals("1")) {//video
            int videoIdIndex = url.indexOf("?v=");
            return url.substring(videoIdIndex+3);
        }
        else if(this.getType().equals("2")){
            if(!url.contains("http"))
                return "https://" + url;
            else
                return url;
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String type;

    public ItemInPlaylist(String id,String url,String type){
        this.id = id;
        this.url = url;
        this.type = type;
    }
}
