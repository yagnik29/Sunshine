package Webservices;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Yash on 03-Jan-18.
 */

public class ApiHandler {

    private static final String BASE_URL = "http://api.openweathermap.org/data";


    private static final long HTTP_TIMEOUT = TimeUnit.SECONDS.toMillis(60);
    private static WebServices apiService;


    public static WebServices getApiService() {


        if (apiService == null) {


            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            okHttpClient.setWriteTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(BASE_URL)
                    .setClient(new OkClient(okHttpClient))
                    .setConverter(new GsonConverter(new Gson()))
                    .build();

            apiService = restAdapter.create(WebServices.class);
            return apiService;
        } else {
            return apiService;
        }


    }
}
