package in.techbeat.AllIndiaDirectory.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.http.AndroidHttpClient;
import android.provider.CallLog;
import android.util.Log;
import in.techbeat.AllIndiaDirectory.model.NumberDetail;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirectorySearcher {

    private static final Logger LOGGER = Logger.getLogger(DirectorySearcher.class.getName());
    
    /** Insert correct endpoint here that returns the result
     *  Example endpoint: http://example.com/lookup/<number>
     *  Example response:
     *  {"number":"0123456789","name":"Name of the User","number_type":"Mobile","address":"State and Operator"}
     *
     */
    private static final String REST_ENDPOINT = "";

    /**
     * Retrieves number details from REST end point
     * @param number
     * @return
     */
    public static NumberDetail lookup(final String number) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet request = new HttpGet(REST_ENDPOINT + number);
        try {
            LOGGER.info("Making REST call: " + request);
            final HttpResponse result = client.execute(request);
            final InputStream is = result.getEntity().getContent();
            final StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer);
            final String response = writer.toString();
            final JSONObject json = (JSONObject) new JSONParser().parse(response);
            final NumberDetail res = new NumberDetail();
            res.setNumber((String) json.get("number"));
            res.setName((String) json.get("name"));
            res.setAddress((String) json.get("address"));
            res.setNumberType((String) json.get("number_type"));
            return res;
        } catch (final IOException ioe) {
            LOGGER.log(Level.SEVERE, "IOException occurred: " + ioe.getMessage());
            return null;
        } catch (final ParseException pe) {
            LOGGER.log(Level.SEVERE, "JSON parse Exception occurred: " + pe.getMessage());
            return null;
        } finally {
            client.close();
        }
    }

    /**
     * Retrieves those numbers which are in call logs but not in contacts
     * @param context
     * @return
     */
    public static Map.Entry<String, String>[] retrieveFromLogs(Context context) {
        final Map<String, String> data = new LinkedHashMap<String, String>();

        final String[] strFields = {
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DATE
        };
        final String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        final Cursor cursor = context.getContentResolver().query(
                android.provider.CallLog.Calls.CONTENT_URI,
                strFields,
                null,
                null,
                strOrder
        );
        if (cursor.moveToFirst()) {
            // Loop through
            do {
                final String name = cursor.getString(2);
                if (name != null) {
                    Log.i("DirectorySearcher", "Name already exists in contacts.");
                    continue;
                }
                final String number = cursor.getString(0);
                Log.i("DirectorySearcher", "Number is: " + number + " date is " + cursor.getString(3));
                Log.i("DirectorySearcher", "Number " + number + " is not present in contacts.");
                final int type = cursor.getInt(1);
                final Long time = cursor.getLong(3);
                Date date = new Date(time);
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm");
                final String timeStr = sdf.format(date);
                if (!data.containsKey(number)) {
                    data.put(number, logTypeToString(type, timeStr));
                }
            } while (cursor.moveToNext());
        }
        Map.Entry<String, String>[] array = data.entrySet().toArray(new Map.Entry[data.size()]);
        return array;
    }


    private static String logTypeToString(int logType, final String time) {
        switch(logType) {
            case CallLog.Calls.INCOMING_TYPE:
                return time + " INCOMING";
            case CallLog.Calls.MISSED_TYPE:
                return time + " MISSED";
            case CallLog.Calls.OUTGOING_TYPE:
                return time + " OUTGOING";
            default:
                return "NONE";
        }
    }

}
