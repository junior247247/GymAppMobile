package service;

import retrofit2.Call;

public class RetroFitConfiguration {
    private String url="https://fcm.googleapis.com";
    public RetroFitConfiguration(){

    }

    public Call<FrmResponse> senNotification(FrmBody body){
        return RetroFitClient.getClient(url).create(IFCMAPI.class).send(body);
    }
}
