package service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import service.api.UserInfoAPI;

public class UserInfoService {
    private static  final String BASE_URL ="http://8.209.105.157:6379";
//    private static  final String BASE_URL ="http://192.168.137.1:80";

    public static UserInfoAPI getUserinfoAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(UserInfoAPI.class);
    }
}
