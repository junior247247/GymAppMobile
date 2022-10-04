package service;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMAPI {
        @Headers({
                "Content-Type:application/json",
                "Authorization:key=AAAAZ82YFQo:APA91bF2aJaegvGDhCs8Bl42nEpkgUgaRb3XkJH-nLKzNDowX5jHPTrrX02GPsqi3i5X55xhQGkCLubByRfk4JPgamFl33TauVLSsjM7Ya4JcNFgGTYjqxlFS08y7SdEN0Pu0Q7tz6St"
        })

    @POST("fcm/send")
    Call<FrmResponse> send(@Body FrmBody body) ;


}
