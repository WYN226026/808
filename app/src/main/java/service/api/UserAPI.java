package service.api;

import java.util.List;

import entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserAPI {

    @GET("cl4/user/login")
    Call<Long> loginMachine(@Query("machine")String machine,@Query("model")String model);

    @POST("cl4/user/addUser")
    Call<Long> addUser(@Body User user);

    @GET("cl4/user/deleteOne")
    Call<Long> deleteUserOne(@Query("machine")String machine,@Query("userID") String userID);

    @POST("cl4/user/updateOne")
    Call<Long> updateOne(@Body User user);

    @POST("cl4/user/batchInsertORUpdate")
    Call<Long> batchInsertORUpdate(@Body List<User> users);
    @POST("cl4/user/batchDelete")
    Call<Long> batchDelete(@Query("machine") String machine,@Body List<String> userIDs);

    @GET("cl4/user/selectUser")
    Call<List<User>>  selectUserAll(@Query("machine")String machine);
}
