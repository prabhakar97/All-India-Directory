package in.techbeat.AllIndiaDirectory.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import in.techbeat.AllIndiaDirectory.helpers.Constants;
import in.techbeat.AllIndiaDirectory.helpers.DatabaseHelper;
import in.techbeat.AllIndiaDirectory.model.NumberDetail;

import java.sql.SQLException;

/**
 * Created by prabhakar on 25/3/14.
 */
public class NumberDetailsDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    private static final String TABLE_NAME = "number_details";

    public NumberDetailsDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        if (db == null) {
            db = dbHelper.getWritableDatabase();
        }
    }

    public void close() {
        db.close();
    }

    public NumberDetail getNumberDetail(final String number) {
        final Cursor c = db.query(TABLE_NAME, null, "number = ?", new String[]{number}, null, null, null);
        if (c == null) {
            return null;
        }
        else if(c.moveToFirst()) {
            Log.i("DatabaseHelper", "Got matching row for " + number);
            NumberDetail numberDetail = new NumberDetail();
            numberDetail.setAddress(c.getString(c.getColumnIndex("address")));
            numberDetail.setName(c.getString(c.getColumnIndex("name")));
            numberDetail.setNumber(c.getString(c.getColumnIndex("number")));
            numberDetail.setNumberType(c.getString(c.getColumnIndex("number_type")));
            Log.i("DatabaseHelper", "Returning: " + numberDetail);
            c.close();
            return numberDetail;
        }
        else {
            return null;
        }
    }

    public void insertNumberDetail(final NumberDetail numberDetail) {
        ContentValues values = new ContentValues();
        if (numberDetail.getName().equals(Constants.TEMPORARY_ERROR.getText())) {
            return;
        }
        values.put("number", numberDetail.getNumber());
        values.put("name", numberDetail.getName());
        values.put("address", numberDetail.getAddress());
        values.put("number_type", numberDetail.getNumberType());

        db.insert(TABLE_NAME, null, values);
        Log.i("DatabaseHelper", "Inserted row for " + numberDetail);
    }

}
