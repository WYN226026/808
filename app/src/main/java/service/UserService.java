package service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import service.api.UserAPI;

public class UserService {
    private static  final String BASE_URL ="http://8.209.105.157:6379";

    public static UserAPI getUserAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(UserAPI.class);
    }
}
