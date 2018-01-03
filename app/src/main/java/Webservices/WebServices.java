package Webservices;

import java.util.Map;

import Model.GetTempResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * Created by Yash on 03-Jan-18.
 */

public interface WebServices {

    @GET("/2.5/forecast")
    public void getTempResponse(@QueryMap Map<String, String> map, Callback<GetTempResponse> callback);

}

