package Models;

public class Atletas {
    private int imgResource;
    private String imgUrl;
    private String name;
    private String rango;
    private String alias;

    public Atletas(){

    }

    public Atletas(int imgResource, String imgUrl, String name, String rango, String alias) {
        this.imgResource = imgResource;
        this.imgUrl = imgUrl;
        this.name = name;
        this.rango = rango;
        this.alias = alias;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
