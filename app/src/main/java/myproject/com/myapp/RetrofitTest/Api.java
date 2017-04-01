package myproject.com.myapp.RetrofitTest;

import io.reactivex.Observable;
import myproject.com.myapp.RetrofitTest.entity.LoginRequest;
import myproject.com.myapp.RetrofitTest.entity.LoginResponse;
import myproject.com.myapp.RetrofitTest.entity.RegisterRequest;
import myproject.com.myapp.RetrofitTest.entity.RegisterResponse;
import myproject.com.myapp.RetrofitTest.entity.UserBaseInfoRequest;
import myproject.com.myapp.RetrofitTest.entity.UserBaseInfoResponse;
import myproject.com.myapp.RetrofitTest.entity.UserExtraInfoRequest;
import myproject.com.myapp.RetrofitTest.entity.UserExtraInfoResponse;
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
