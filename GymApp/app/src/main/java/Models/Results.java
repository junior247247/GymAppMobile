package Models;

public class Results {
    private String title;
    private String lugar;
    public Results(){

    }
    public Results(String title, String lugar) {
        this.title = title;
        this.lugar = lugar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
