package service.api;

import java.util.List;

import entity.UploadUserInfo;
import entity.UserInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserInfoAPI {
    @POST("cl4/userInfo/addUserInfo")
    Call<Long> addUserInfo(@Body UserInfo userInfo);
    @GET("cl4/userInfo/deleteOne")
    Call<Long>deleteUserInfoOne(@Query("machine")String machine,@Query("userID")String userID,@Query("userInfoID")String userInfoID);

    @POST("cl4/userInfo/updateUserInfo")
    Call<Long> updateUserInfo(@Body UserInfo userInfo);

    @POST("cl4/userInfo/batchInsertORUpdate")
    Call<Long> batchInsertORUpdate(@Body List<UserInfo> userInfos);

    @POST("cl4/userInfo/batchDelete")
    Call<Long> batchDelete (@Query("machine") String machine , @Body List<UploadUserInfo> UploadUserInfo);

    @GET("cl4/userInfo/selectAll")
    Call<List<UserInfo>> selectAllUserInfo(@Query("machine")String machine);
}
