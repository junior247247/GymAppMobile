package Models;

public class SliderItems {
    private String imgUrl;
    private int imgResource;

    public SliderItems(){

    }
    public SliderItems(String imgUrl, int imgResource) {
        this.imgUrl = imgUrl;
        this.imgResource = imgResource;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }
}
