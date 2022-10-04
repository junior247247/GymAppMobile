package Models;

public class PeleasProgramadas {
    private int imgResoucer;
    private String imgUrl;
    private String title;
    private String date;
    public PeleasProgramadas(){

    }
    public PeleasProgramadas(int imgResoucer, String imgUrl, String title, String date) {
        this.imgResoucer = imgResoucer;
        this.imgUrl = imgUrl;
        this.title = title;
        this.date = date;
    }

    public int getImgResoucer() {
        return imgResoucer;
    }

    public void setImgResoucer(int imgResoucer) {
        this.imgResoucer = imgResoucer;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
