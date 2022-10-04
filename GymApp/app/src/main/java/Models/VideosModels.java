package Models;

public class VideosModels {
    private int videImage;
    private String videoUrl;
    private String titleVideo;
    private String descriptionVideo;
    private String timeVideo;

    public VideosModels(){

    }

    public VideosModels(int videImage, String videoUrl, String titleVideo, String descriptionVideo, String timeVideo) {
        this.videImage = videImage;
        this.videoUrl = videoUrl;
        this.titleVideo = titleVideo;
        this.descriptionVideo = descriptionVideo;
        this.timeVideo = timeVideo;
    }

    public int getVideImage() {
        return videImage;
    }

    public void setVideImage(int videImage) {
        this.videImage = videImage;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitleVideo() {
        return titleVideo;
    }

    public void setTitleVideo(String titleVideo) {
        this.titleVideo = titleVideo;
    }

    public String getDescriptionVideo() {
        return descriptionVideo;
    }

    public void setDescriptionVideo(String descriptionVideo) {
        this.descriptionVideo = descriptionVideo;
    }

    public String getTimeVideo() {
        return timeVideo;
    }

    public void setTimeVideo(String timeVideo) {
        this.timeVideo = timeVideo;
    }
}
