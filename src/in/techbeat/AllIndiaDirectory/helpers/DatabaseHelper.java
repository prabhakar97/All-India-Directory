package in.techbeat.AllIndiaDirectory.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import in.techbeat.AllIndiaDirectory.model.NumberDetail;

/**
 * Created by prabhakar on 25/3/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "numbers.db";
    private static final int DB_VERSION = 2;
    private static final String TABLE_NAME = "number_details";

    private static final String TABLE_CREATE_SQL = "CREATE TABLE "+ TABLE_NAME + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "number TEXT NOT NULL, " +
            "number_type TEXT NOT NULL, " +
            "name TEXT NOT NULL, " +
            "address TEXT NOT NULL" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.i("DatabaseHelper", "Initialized SQLiteOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE_SQL);
        Log.i("DatabaseHelper", "Executed " + TABLE_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Log.w("DatabaseHelper", "Removing blank entries from " + TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME + " WHERE name = ''");
    }

}
