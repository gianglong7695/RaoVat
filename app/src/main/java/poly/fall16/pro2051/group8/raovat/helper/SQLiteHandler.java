/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package poly.fall16.pro2051.group8.raovat.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	public static final String DATABASE_NAME = "android_api";

	// Login table name
	public static final String TABLE_USER = "user";

	// Login Table Columns names
	public static final String KEY_ID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_FULLNAME = "fullname";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_UID = "uid";
	public static final String KEY_AVATAR = "profile_url";
	public static final String KEY_ADDRESS = "address";


	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE," + KEY_FULLNAME + " TEXT," + KEY_PHONE + " TEXT," + KEY_UID + " TEXT, " + KEY_AVATAR + " TEXT, " + KEY_ADDRESS + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String name, String email, String fullName, String phone , String uid, String profile_url, String address) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_FULLNAME, fullName);
		values.put(KEY_PHONE, phone);
		values.put(KEY_UID, uid); // Email
		values.put(KEY_AVATAR, profile_url); // profile_url
		values.put(KEY_ADDRESS, address); // address

		 // Created At

		// Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put(KEY_NAME, cursor.getString(1));
			user.put(KEY_EMAIL, cursor.getString(2));
			user.put(KEY_FULLNAME, cursor.getString(3));
			user.put(KEY_PHONE, cursor.getString(4));
			user.put(KEY_AVATAR, cursor.getString(6));
			user.put(KEY_ADDRESS, cursor.getString(7));
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
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
