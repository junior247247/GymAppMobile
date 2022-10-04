package Models;

public class Noticias {

    private String id;
    private String imageUrl;
    private String titelNoticia;
    private String fechaNoticia;
    private String noticia;
    private long timestamp;
    public Noticias(){

    }

    public Noticias( String imageUrl, String titelNoticia, String fechaNoticia,long timestamp,String noticia,String id) {
        this.imageUrl = imageUrl;
        this.titelNoticia = titelNoticia;
        this.fechaNoticia = fechaNoticia;
        this.timestamp=timestamp;
        this.noticia=noticia;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoticia() {
        return noticia;
    }

    public void setNoticia(String noticia) {
        this.noticia = noticia;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitelNoticia() {
        return titelNoticia;
    }

    public void setTitelNoticia(String titelNoticia) {
        this.titelNoticia = titelNoticia;
    }

    public String getFechaNoticia() {
        return fechaNoticia;
    }

    public void setFechaNoticia(String fechaNoticia) {
        this.fechaNoticia = fechaNoticia;
    }
}
