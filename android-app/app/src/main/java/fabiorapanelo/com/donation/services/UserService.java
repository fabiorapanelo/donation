package fabiorapanelo.com.donation.services;


import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fabiorapanelo.com.donation.model.Credentials;
import fabiorapanelo.com.donation.model.User;

/**
 * Created by fabio on 29/07/2017.
 */

public class UserService extends ServiceBase {

    private static final String PATH = "/users";

    private static final String USERS_ROOT_OBJECT = "users";

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_SECURE_PASSWORD = "securePassword";

    public void authenticate(final Context context, final ServiceListener serviceListener, Credentials credentials) {

        if(credentials == null){
            serviceListener.onError(new NullPointerException());
            return;
        }

        try {
            JSONObject jsonObject = UserService.mapCredentialsToJson(credentials);
            RequestQueue queue = Volley.newRequestQueue(context);

            String url = this.getUrl("/authentication");

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject jsonObject) {
                    serviceListener.onSuccess(null);
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    serviceListener.onError(volleyError);
                }
            }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            serviceListener.onError(ex);
        }
    }

    public void findById(final Context context, final ServiceListener serviceListener, Long id) {

        RequestQueue queue = Volley.newRequestQueue(context);
        try {
            String url = this.getUrl(PATH + "/" + id);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject jsonObject) {
                    try {
                        User user = UserService.mapJsonToUser(jsonObject);
                        serviceListener.onSuccess(user);
                    } catch (JSONException ex) {
                        serviceListener.onError(ex);
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    serviceListener.onError(volleyError);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            serviceListener.onError(ex);
        }
    }

    public void findAll(final Context context, final ServiceListener serviceListener) {

        RequestQueue queue = Volley.newRequestQueue(context);
        try {
            String url = this.getUrl(PATH);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject s) {
                    List<User> users = new ArrayList<>();

                    try {

                        JSONArray jsonArray = s.getJSONObject("_embedded").getJSONArray(USERS_ROOT_OBJECT);

                        for (int index = 0; index < jsonArray.length(); index++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(index);
                            User user = UserService.mapJsonToUser(jsonObject);
                            users.add(user);
                        }

                        serviceListener.onSuccess(users);
                    } catch (JSONException ex) {
                        serviceListener.onError(ex);
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    serviceListener.onError(volleyError);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            serviceListener.onError(ex);
        }
    }

    public void findByUsername(final Context context, final ServiceListener serviceListener, final String username) {

        if(username == null){
            serviceListener.onError(new NullPointerException());
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        try {
            String url = this.getUrl(PATH + "/search/findOneByUsernameIgnoreCase?name=" + username);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject jsonObject) {
                    try {
                        User user = UserService.mapJsonToUser(jsonObject);
                        serviceListener.onSuccess(user);
                    } catch (JSONException ex) {
                        serviceListener.onError(ex);
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    serviceListener.onError(volleyError);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            serviceListener.onError(ex);
        }
    }

    public void save(final Context context, final ServiceListener serviceListener, User user) {

        if(user == null){
            serviceListener.onError(new NullPointerException());
            return;
        }

        try {
            JSONObject jsonObject = mapUserToJson(user);
            RequestQueue queue = Volley.newRequestQueue(context);

            String url = this.getUrl(PATH);
            if(user.getId() != null){
                url += "/" + user.getId();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject jsonObject) {
                    serviceListener.onSuccess(null);
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    serviceListener.onError(volleyError);
                }
            }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            serviceListener.onError(ex);
        }

    }

    public static User mapJsonToUser(JSONObject jsonObject) throws JSONException{
        User user = new User();
        //user.setId(jsonObject.getLong(FIELD_ID));
        user.setName(jsonObject.getString(FIELD_NAME));
        user.setUsername(jsonObject.getString(FIELD_USERNAME));
        user.setType(jsonObject.getString(FIELD_TYPE));
        user.setSecurePassword(jsonObject.getString(FIELD_SECURE_PASSWORD));

        return user;
    }

    public static JSONObject mapUserToJson(User user) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        if(user.getId() != null){
            //jsonObject.put(FIELD_ID, String.valueOf(user.getId()));
        }

        jsonObject.put(FIELD_NAME, user.getName());
        jsonObject.put(FIELD_USERNAME, user.getUsername());
        jsonObject.put(FIELD_TYPE, user.getType());
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
