package in.techbeat.AllIndiaDirectory;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import in.techbeat.AllIndiaDirectory.helpers.DatabaseHelper;
import in.techbeat.AllIndiaDirectory.utils.DirectorySearcher;
import in.techbeat.AllIndiaDirectory.views.CallAdapter;

import java.text.SimpleDateFormat;
import java.util.*;

public class ByLogs extends ListActivity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Map.Entry<String, String>[] array = DirectorySearcher.retrieveFromLogs(getApplicationContext());
        Log.i("ByLogs", "Data is: " + array.toString());
        if (array.length > 0) {
            setListAdapter(new CallAdapter(this, array));
        }
    }


}

