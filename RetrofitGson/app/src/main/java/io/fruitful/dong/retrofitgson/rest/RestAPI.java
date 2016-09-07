package io.fruitful.dong.retrofitgson.rest;

import io.fruitful.dong.retrofitgson.model.Place;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ares on 8/23/2016.
 */
public interface RestAPI {
    @GET("json")
    Call<Place> getResultCall(@Query("location") String location,
                              @Query("rankby") String rankby,
                              @Query("types") String types,
                              @Query("key") String apiKey);
    @GET("json")
    Call<Place> getNextResultCall(@Query("pagetoken") String nextPageToken,
                                  @Query("key") String apiKey);
}
