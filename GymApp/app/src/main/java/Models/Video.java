package Models;

public class Video {
    private String id;
    private String videoURL;
    private String videoTittle;
    private String videoDescription;
    private String timeVideo;
    private String miniatura;
    private long timestamp;

    public Video(){

    }

    public Video(String id, String videoURL, String videoTittle, String videoDescription, String timeVideo,long timestamp,String miniatura) {
        this.id = id;
        this.videoURL = videoURL;
        this.videoTittle = videoTittle;
        this.videoDescription = videoDescription;
        this.timeVideo = timeVideo;
        this.timestamp=timestamp;
        this.miniatura=miniatura;
    }

    public String getMiniatura() {
        return miniatura;
    }

    public void setMiniatura(String miniatura) {
        this.miniatura = miniatura;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoTittle() {
        return videoTittle;
    }

    public void setVideoTittle(String videoTittle) {
        this.videoTittle = videoTittle;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getTimeVideo() {
        return timeVideo;
    }

    public void setTimeVideo(String timeVideo) {
        this.timeVideo = timeVideo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
