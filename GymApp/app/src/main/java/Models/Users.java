package Models;

public class Users {
    private String id;
    private String name;
    private String urlImg;

    public Users(){

    }
    public Users(String id, String name, String urlImg) {
        this.id = id;
        this.name = name;
        this.urlImg = urlImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
