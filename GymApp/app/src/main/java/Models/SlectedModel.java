package Models;

public class SlectedModel {

    boolean isSlected=false;

    public SlectedModel(boolean isSlected) {
        this.isSlected = isSlected;
    }

    public boolean isSlected() {
        return isSlected;
    }

    public void setSlected(boolean slected) {
        isSlected = slected;
    }
}
