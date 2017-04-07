package myproject.com.myapp.RetrofitLearn.api;

import java.util.List;

import myproject.com.myapp.RetrofitLearn.entitiy.Contributor;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by wang on 07/04/17.
 */

public interface GitHub {
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo
    );
}
