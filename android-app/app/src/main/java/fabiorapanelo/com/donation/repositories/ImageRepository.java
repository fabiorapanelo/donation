package fabiorapanelo.com.donation.repositories;

import java.util.List;

import fabiorapanelo.com.donation.model.Campaign;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by fabio on 07/08/2017.
 */

public interface ImageRepository {

    @Multipart
    @POST("users/{userId}/upload-images")
    Call<ResponseBody> upload(@Path("userId") String userId,
                              @Part List<MultipartBody.Part> images);

}
