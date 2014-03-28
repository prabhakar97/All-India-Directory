package in.techbeat.AllIndiaDirectory.views;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import in.techbeat.AllIndiaDirectory.R;
import in.techbeat.AllIndiaDirectory.dao.NumberDetailsDAO;
import in.techbeat.AllIndiaDirectory.helpers.LookupTask;
import in.techbeat.AllIndiaDirectory.helpers.RowListener;
import in.techbeat.AllIndiaDirectory.model.NumberDetail;
import in.techbeat.AllIndiaDirectory.utils.SuccessCallable;

import java.util.Map;

/**
 * Created by prabhakar on 24/3/14.
 */
public class CallAdapter extends ArrayAdapter<Map.Entry<String, String>> {

    private final Context context;
    private final Map.Entry<String, String>[] values;
    private NumberDetailsDAO numberDetailsDAO;

    public CallAdapter(Context context, Map.Entry<String, String>[] values) {
        super(context, R.layout.call_list, values);
        numberDetailsDAO = new NumberDetailsDAO(context);
        numberDetailsDAO.open();
        this.context = context;
        this.values = values;
        Log.i("CallAdapter", "Initialized callAdapter");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.call_list, parent, false);
        final TextView name = (TextView) rowView.findViewById(R.id.name);
        final TextView number = (TextView) rowView.findViewById(R.id.number);
        final TextView numberType = (TextView) rowView.findViewById(R.id.numberType);
        final TextView address = (TextView) rowView.findViewById(R.id.address);
        final TextView logType = (TextView) rowView.findViewById(R.id.logType);

        String phoneNumber = values[position].getKey();
        number.setText(phoneNumber);
        logType.setText(values[position].getValue());
        if(phoneNumber.length() < 10) {
            Log.w("CallAdapter", "Phone number is less than 10 chars");
            address.setText("This number is invalid.");
            name.setText("Invalid number");
            numberType.setText("Invalid num.");
            return rowView;
        }
        phoneNumber = phoneNumber.substring(phoneNumber.length() - 10);
        final NumberDetail numberDetail = numberDetailsDAO.getNumberDetail('0' + phoneNumber);
        if (numberDetail != null) {
            Log.i("CallAdapter", "Phone number " + phoneNumber + " was found locally cached.");
            name.setText(numberDetail.getName());
            numberType.setText(numberDetail.getNumberType());
            address.setText(numberDetail.getAddress());
            rowView.setOnClickListener(new RowListener(numberDetail.getName(), numberDetail.getNumber(), context));
            return rowView;
        }

        name.setText("Checking...");
        numberType.setText("Checking...");
        address.setText("Checking...");
        Log.i("CallAdpater", "Launching lookup thread for number " + phoneNumber);
        new LookupTask().execute(phoneNumber, new SuccessCallable() {
            @Override
            public Void call() throws Exception {
                name.setText(getNumberDetails().getName());
                number.setText(getNumberDetails().getNumber());
                numberType.setText(getNumberDetails().getNumberType());
                address.setText(getNumberDetails().getAddress());
                numberDetailsDAO.insertNumberDetail(getNumberDetails());
                rowView.setOnClickListener(new RowListener(getNumberDetails().getName(), getNumberDetails().getNumber(), context));
                return null;
            }
        }, null);
        return rowView;
    }

}