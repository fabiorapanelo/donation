package fabiorapanelo.com.donation.services;

import java.io.File;

import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.repositories.CampaignRepository;
import fabiorapanelo.com.donation.repositories.ImageRepository;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fabio on 08/08/2017.
 */

public class ImageService extends ServiceBase {

    protected ImageRepository imageRepository;

    private static ImageService instance = new ImageService();

    private ImageService(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        imageRepository = retrofit.create(ImageRepository.class);
    }

    public static ImageService getInstance(){
        return instance;
    }

    public void upload(User user, String filePath, final Callback<ResponseBody> callback) {

        File file = new File(filePath);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "image");

        Call<ResponseBody> call = imageRepository.upload(user.getId().toString(), body, name);

        call.enqueue(callback);

    }
}
