package fabiorapanelo.com.donation.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.ImageUpload;
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
        super();
        imageRepository = retrofit.create(ImageRepository.class);
    }

    public static ImageService getInstance(){
        return instance;
    }

    public void upload(User user, List<String> filePaths, final Callback<ImageUpload> callback) {

        List<MultipartBody.Part> parts = new ArrayList<>();

        for(String filePath: filePaths){

            File file = new File(filePath);
            String filename = file.getName();
            String extension = ImageService.getExtension(filename);

            Bitmap bmp = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            if(extension.equalsIgnoreCase("png")){
                bmp.compress(Bitmap.CompressFormat.PNG, 50, bos);
            } else {
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            }

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), bos.toByteArray());
            MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), requestBody);

            parts.add(part);
        }

        Call<ImageUpload> call = imageRepository.upload(user.getId().toString(), parts);

        call.enqueue(callback);

    }

    public static String getExtension(String filename){

        int i = filename.lastIndexOf('.');
        if (i > 0) {
            return filename.substring(i);
        }

        return "";
    }
}
