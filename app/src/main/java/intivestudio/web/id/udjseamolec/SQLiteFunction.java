package intivestudio.web.id.udjseamolec;

/**
 * Created by outattacker on 04/05/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteFunction extends SQLiteOpenHelper {

    private static final String TAG = SQLiteFunction.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_HASIL = "hasil";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IDS = "id_users";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_NILAI = "nilai";

    public SQLiteFunction(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_HASIL + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IDS + " TEXT,"
                + KEY_NAMA + " TEXT UNIQUE," + KEY_NILAI + " TEXT)";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HASIL);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addNilai(String nama, String nilai) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAMA, nama); // Nama
        values.put(KEY_NILAI, nilai); // Nilai

        // Inserting Row
        long id = db.insert(TABLE_HASIL, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getNilaiDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_HASIL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("ids", cursor.getString(1));
            user.put("nama", cursor.getString(2));
            user.put("nilai", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteNilai() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_HASIL, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}