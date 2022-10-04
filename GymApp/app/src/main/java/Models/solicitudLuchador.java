package Models;

public class solicitudLuchador {
    private String id;
    private String idLuchador;
    private String title;
    private String message;
    private boolean isView;
    private long timestamp;


    public  solicitudLuchador(){

    }
    public solicitudLuchador(String id, String idLuchador, String title, String message, boolean isView, long timestamp) {
        this.id = id;
        this.idLuchador = idLuchador;
        this.title = title;
        this.message = message;
        this.isView = isView;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdLuchador() {
        return idLuchador;
    }

    public void setIdLuchador(String idLuchador) {
        this.idLuchador = idLuchador;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isView() {
        return isView;
    }

    public void setView(boolean view) {
        isView = view;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
