package fabiorapanelo.com.donation.services;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import fabiorapanelo.com.donation.model.Credentials;
import fabiorapanelo.com.donation.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fabio on 29/07/2017.
 */

public class UserService extends ServiceBase {

    private static final String PATH = "users";

    private static final String USERS_ROOT_OBJECT = "users";

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_RECEIVE_DONATIONS = "receiveDonations";
    private static final String FIELD_VERIFIED = "verified";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_SECURE_PASSWORD = "securePassword";

    protected UserRepository userRepository;

    public UserService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userRepository = retrofit.create(UserRepository.class);
    }
    public void authenticate(Credentials credentials, final ServiceListener serviceListener) {

        if(credentials == null){
            serviceListener.onError(new NullPointerException());
            return;
        }

        Call<ResponseBody> authenticate = userRepository.authenticate(credentials);
        authenticate.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                serviceListener.onSuccess(null);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                serviceListener.onError(t);
            }
        });
    }

    public void findByUsername(final String username, final ServiceListener serviceListener) {

        if(StringUtils.isEmpty(username)){
            serviceListener.onError(new NullPointerException());
            return;
        }

        Call<User> findByUsername = userRepository.findByUsername(username);
        findByUsername.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {

                if(response.isSuccessful()){
                    serviceListener.onSuccess(response.body());
                } else {
                    serviceListener.onError(null);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                serviceListener.onError(t);
            }
        });
    }

    public void save(User user, final ServiceListener serviceListener) {

        if(user == null){
            serviceListener.onError(new NullPointerException());
            return;
        }

        Call<ResponseBody> call;
        if(user.getId() != null){
            call = userRepository.update(user.getId().toString(), user);
        } else {
            call = userRepository.create(user);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                serviceListener.onSuccess(null);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                serviceListener.onError(t);
            }
        });

    }

    public static User mapJsonToUser(JSONObject jsonObject) throws JSONException{
        User user = new User();

        user.setName(jsonObject.getString(FIELD_NAME));
        user.setUsername(jsonObject.getString(FIELD_USERNAME));
        user.setReceiveDonations(jsonObject.getBoolean(FIELD_RECEIVE_DONATIONS));
        user.setVerified(jsonObject.getBoolean(FIELD_VERIFIED));
        user.setSecurePassword(jsonObject.getString(FIELD_SECURE_PASSWORD));

        return user;
    }

    public static JSONObject mapUserToJson(User user) throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put(FIELD_NAME, user.getName());
        jsonObject.put(FIELD_USERNAME, user.getUsername());
        jsonObject.put(FIELD_RECEIVE_DONATIONS, user.isReceiveDonations());
        jsonObject.put(FIELD_VERIFIED, user.isVerified());
        jsonObject.put(FIELD_PASSWORD, user.getPassword());
        jsonObject.put(FIELD_SECURE_PASSWORD, user.getSecurePassword());

        return jsonObject;
    }

    public static JSONObject mapCredentialsToJson(Credentials credentials) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_USERNAME, credentials.getUsername());
        jsonObject.put(FIELD_PASSWORD, credentials.getPassword());

        return jsonObject;
    }
}
