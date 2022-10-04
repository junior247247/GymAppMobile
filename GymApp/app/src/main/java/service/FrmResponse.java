package service;

import java.util.ArrayList;

public class FrmResponse {
    private long multicast_id;
    private int success;
    private int failure;
    private int canonical_ids;
    ArrayList<Object> result= new ArrayList<Object>();


    public FrmResponse(long multicast_id, int success, int failure, int canonical_ids, ArrayList<Object> result) {
        this.multicast_id = multicast_id;
        this.success = success;
        this.failure = failure;
        this.canonical_ids = canonical_ids;
        this.result = result;
    }

    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public ArrayList<Object> getResult() {
        return result;
    }

    public void setResult(ArrayList<Object> result) {
        this.result = result;
    }
}
