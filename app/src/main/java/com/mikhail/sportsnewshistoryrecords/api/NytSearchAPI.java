package com.mikhail.sportsnewshistoryrecords.api;

import com.google.gson.Gson;
import com.mikhail.sportsnewshistoryrecords.api.keys.NytKeys;
import com.mikhail.sportsnewshistoryrecords.model.search.ArticleSearch;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Mikhail on 4/28/16.
 */
public class NytSearchAPI {

    public static final String NYT_API_URL = "http://api.nytimes.com/svc/search/v2/";
    public static Gson gson = new Gson();


    public interface NytAPIRetrofitSimple {
        @GET("articlesearch.json?&api-key=" + NytKeys.nyTimesFullSearchQueryKey)
        Call<ArticleSearch> response(
                @Query("q") String q);
    }

    public static NytAPIRetrofitSimple create() {
        // Create a very simple REST adapter which points the GitHub API.
        return new Retrofit.Builder()
                .baseUrl(NYT_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NytSearchAPI.NytAPIRetrofitSimple.class);
    }

    public interface NytRx {
        @GET("articlesearch.json?&api-key=" + NytKeys.nyTimesFullSearchQueryKey)
        Observable<ArticleSearch> response(
                @Query("q") String q);
    }

    public static NytRx createRx() {
        return new Retrofit.Builder()
                .baseUrl(NYT_API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(NytSearchAPI.NytRx.class);
    }
}
//&sort=newest