package mao.com.mycustomview.retrofit;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by maoqitian on 2018/12/5 0005.
 */

public interface GitHub {

    public static final String API_URL = "https://api.github.com";

    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<SimpleService.Contributor>> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo);
}
