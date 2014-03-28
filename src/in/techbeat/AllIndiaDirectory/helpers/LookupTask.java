package in.techbeat.AllIndiaDirectory.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import in.techbeat.AllIndiaDirectory.dao.NumberDetailsDAO;
import in.techbeat.AllIndiaDirectory.model.NumberDetail;
import in.techbeat.AllIndiaDirectory.utils.DirectorySearcher;
import in.techbeat.AllIndiaDirectory.utils.SuccessCallable;

import java.util.concurrent.Callable;

/**
 * Created by prabhakar on 26/3/14.
 */
public class LookupTask extends AsyncTask<Object, Void, NumberDetail> {

    SuccessCallable callableSuccess;
    Callable<Void> callableFailure;

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected NumberDetail doInBackground(Object... params) {
        String phoneNumber = (String) params[0];
        callableSuccess = (SuccessCallable) params[1];
        callableFailure = (Callable<Void>) params[2];
        Log.i("LookupTask", "Calling REST API to retrieve details of " + phoneNumber);
        return DirectorySearcher.lookup(phoneNumber);
    }

    @Override
    protected void onPostExecute(final NumberDetail result) {
        if (result == null) {
            Log.w("LookupTask", "Look up returned a null. Probably internet is not connected.");
            try {
                callableFailure.call();
            } catch (Exception e) {
                Log.e("LookupTask", "Error calling failure callable: " + e.getMessage());
            }
        } else {
            try {
                Log.i("LookupTask", "Throwing NumberDetail object away from the closure: " + result);
                callableSuccess.setNumberDetails(result);
                callableSuccess.call();
            } catch (Exception e) {
                Log.e("LookupTask", "Error calling success callable: " + e.getMessage());
            }
        }
    }

}


