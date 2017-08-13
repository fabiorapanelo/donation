package fabiorapanelo.com.donation.services;


import org.apache.commons.lang3.StringUtils;

import fabiorapanelo.com.donation.model.Credentials;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.repositories.UserRepository;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fabio on 29/07/2017.
 */

public class UserService extends ServiceBase {

    protected UserRepository userRepository;

    private static UserService instance = new UserService();

    private UserService(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        userRepository = retrofit.create(UserRepository.class);
    }

    public static UserService getInstance(){
        return instance;
    }

    public static String getUrlForUser(User user){

        String url = BASE_URL + "users/" + user.getId();
        return url;

    }
    public void authenticate(Credentials credentials, final Callback<User> callback) {
        Call<User> authenticate = userRepository.authenticate(credentials);
        authenticate.enqueue(callback);
    }

    public void findByUsername(final String username, final Callback<User> callback) {
        Call<User> findByUsername = userRepository.findByUsername(username);
        findByUsername.enqueue(callback);
    }

    public void save(User user, final Callback<ResponseBody> callback) {

        Call<ResponseBody> call;
        if(user.getId() != null){
            call = userRepository.update(user.getId().toString(), user);
        } else {
            call = userRepository.create(user);
        }

        call.enqueue(callback);

    }

    public void findMyCampaigns(Long userId, final Callback<ResponseBody> callback){
        Call<ResponseBody> call = userRepository.findMyCampaigns(userId);
        call.enqueue(callback);
    }

}
