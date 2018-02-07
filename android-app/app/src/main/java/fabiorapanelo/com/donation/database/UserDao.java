package fabiorapanelo.com.donation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import fabiorapanelo.com.donation.cache.CacheManager;
import fabiorapanelo.com.donation.model.User;

import static java.security.AccessController.getContext;

/**
 * Created by fabio on 08/08/2017.
 */

public class UserDao {

    CacheManager cacheManager = CacheManager.getInstance();

    protected DonationDbHelper donationDbHelper;

    public static final String CACHE_KEY_USER = "donation.user";

    public static final String TABLE_NAME = "USER";
    public static final String FIELD_USER_ID = "USER_ID";
    public static final String FIELD_NAME = "NAME";
    public static final String FIELD_USERNAME = "USERNAME";

    public static final String SQL_DROP_TABLE_USER = "DROP TABLE IF EXISTS USER";
    public static final String SQL_CREATE_USER =
            "CREATE TABLE " + TABLE_NAME + " ( " + FIELD_USER_ID +" TEXT PRIMARY KEY," +
                    FIELD_NAME + " TEXT," +
                    FIELD_USERNAME + " TEXT)";


    public UserDao(Context context){
        donationDbHelper = new DonationDbHelper(context);
    }

    public void delete(){
        SQLiteDatabase db = donationDbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public void save(User user, boolean remember){

        cacheManager.put(CACHE_KEY_USER, user);

        if(remember){
            SQLiteDatabase db = donationDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(FIELD_USER_ID, user.getId());
            values.put(FIELD_NAME, user.getName());
            values.put(FIELD_USERNAME, user.getUsername());

            delete();
            db.insert(TABLE_NAME, null, values);
        }

    }

    public User find(){
        return (User) cacheManager.get(CACHE_KEY_USER);
    }

    public User readFromDB(){

        User user = null;

        SQLiteDatabase db = donationDbHelper.getWritableDatabase();

        String[] projection = { FIELD_USER_ID, FIELD_NAME, FIELD_USERNAME };

        Cursor cursor = db.query(
            TABLE_NAME, // The table to query
            projection, // The columns to return
            null,       // The columns for the WHERE clause
            null,       // The values for the WHERE clause
            null,       // don't group the rows
            null,       // don't filter by row groups
            null        // The sort order
        );

        if(cursor.moveToNext()) {
            user = new User();
            user.setId(cursor.getString(0));
            user.setName(cursor.getString(1));
            user.setUsername(cursor.getString(2));
        }
        cursor.close();

        return user;
    }
}
