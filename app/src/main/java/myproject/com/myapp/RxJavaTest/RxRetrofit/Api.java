package myproject.com.myapp.RxJavaTest.RxRetrofit;

import io.reactivex.Observable;
import myproject.com.myapp.RxJavaTest.RxRetrofit.entity.LoginRequest;
import myproject.com.myapp.RxJavaTest.RxRetrofit.entity.LoginResponse;
import myproject.com.myapp.RxJavaTest.RxRetrofit.entity.RegisterRequest;
import myproject.com.myapp.RxJavaTest.RxRetrofit.entity.RegisterResponse;
import myproject.com.myapp.RxJavaTest.RxRetrofit.entity.UserBaseInfoRequest;
import myproject.com.myapp.RxJavaTest.RxRetrofit.entity.UserBaseInfoResponse;
import myproject.com.myapp.RxJavaTest.RxRetrofit.entity.UserExtraInfoRequest;
import myproject.com.myapp.RxJavaTest.RxRetrofit.entity.UserExtraInfoResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by wang on 01/04/17.
 */

public interface Api {
    @GET
    Observable<LoginResponse> login(@Body LoginRequest request);

    @GET
    Observable<RegisterResponse> register(@Body RegisterRequest request);

    @GET
    Observable<UserBaseInfoResponse> getUserBaseInfo(@Body UserBaseInfoRequest request);

    @GET
    Observable<UserExtraInfoResponse> getUserExtraInfo(@Body UserExtraInfoRequest request);

    @GET("v2/movie/top250")
    Observable<RegisterResponse> getTop250(@Query("start") int start, @Query("count") int count);
}
