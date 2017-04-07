package myproject.com.myapp.RetrofitLearn;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import myproject.com.myapp.RetrofitLearn.api.GitHub;
import myproject.com.myapp.RetrofitLearn.entitiy.Contributor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wang on 07/04/17.
 */

public class RetrofitTest {
    private static final String TAG = "RetrofitTest";
    private static final String BASEURL = "https://api.github.com";

    /**
     * 最简单使用，同步调用
     */
    public static void callExecute() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();

        GitHub gitHubService = retrofit.create(GitHub.class);
        final Call<List<Contributor>> call = gitHubService.contributors("square", "retrofit");
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                try {
                    Response<List<Contributor>> response = call.execute();//同步调用，只能被调用一次，多次调用会抛出java.lang.IllegalStateException: Already executed
                    Log.d(TAG, "retrofitTest1 response: " + response.body().toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    /**
     * clone call并且进行异步调用
     */
    public static void callTestEnqueue() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        GitHub gitHubService = retrofit.create(GitHub.class);
        Call<List<Contributor>> call = gitHubService.contributors("square", "retrofit");
        call.clone().enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                Log.d(TAG, "callTestEnqueue onResponse: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }
}
