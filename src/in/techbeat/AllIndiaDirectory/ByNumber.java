package in.techbeat.AllIndiaDirectory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import in.techbeat.AllIndiaDirectory.dao.NumberDetailsDAO;
import in.techbeat.AllIndiaDirectory.helpers.LookupTask;
import in.techbeat.AllIndiaDirectory.helpers.RowListener;
import in.techbeat.AllIndiaDirectory.model.NumberDetail;
import in.techbeat.AllIndiaDirectory.helpers.DirectorySearcher;
import in.techbeat.AllIndiaDirectory.helpers.SuccessCallable;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by prabhakar on 26/3/14.
 */
public class ByNumber extends Activity {
    Button searchButton;
    EditText inputNumber;
    LinearLayout layout1;
    LinearLayout layout2;
    LinearLayout layout3;
    TextView name;
    TextView number;
    TextView address;
    TextView numberType;
    ProgressDialog pd;
    Context context;
    String phoneNumber;
    Map.Entry<String, String>[] array;
    NumberDetailsDAO numberDetailsDAO;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bynumber);
        searchButton = (Button) findViewById(R.id.search);
        inputNumber = (EditText) findViewById(R.id.inputPhone);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        name = (TextView) findViewById(R.id.name1);
        number = (TextView) findViewById(R.id.number1);
        address = (TextView) findViewById(R.id.address1);
        numberType = (TextView) findViewById(R.id.numberType1);
        context = this;
        numberDetailsDAO = new NumberDetailsDAO(context);
        numberDetailsDAO.open();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = inputNumber.getText().toString();
                if (phoneNumber.length() < 10) {
                    setInvalidNumber("Number is less than 10 digits");
                    return;
                }
                phoneNumber = phoneNumber.substring(phoneNumber.length() - 10);
                if (!phoneNumber.matches("\\d{10}")) {
                    setInvalidNumber("Number does not contain all digits");
                    return;
                }
                // Lookup in the database first
                final NumberDetail numberDetail = numberDetailsDAO.getNumberDetail('0' + phoneNumber);
                if (numberDetail != null) {
                    // Display content
                    Log.i("ByNumber", "Phone number " + phoneNumber + " was found locally cached.");
                    name.setText(numberDetail.getName());
                    numberType.setText(numberDetail.getNumberType());
                    address.setText(numberDetail.getAddress());
                    toggleLayouts(View.VISIBLE, phoneNumber);
                    setListeners(numberDetail);
                    return;
                }
                // Launch an async task to contact lookup service
                new LookupTask() {
                    @Override
                    protected void onPreExecute() {
                        pd = new ProgressDialog(context);
                        pd.setTitle("Looking up...");
                        pd.setMessage("Please wait...");
                        pd.setCancelable(false);
                        pd.setIndeterminate(true);
                        pd.show();
                    }
                }.execute(phoneNumber, new SuccessCallable() {
                    @Override
                    public Void call() throws Exception {
                        pd.dismiss();
                        Log.i("ByNumber", "Setting numberDetail values onto UI: " + getNumberDetails());
                        name.setText(getNumberDetails().getName());
                        numberType.setText(getNumberDetails().getNumberType());
                        address.setText(getNumberDetails().getAddress());
                        toggleLayouts(View.VISIBLE, phoneNumber);
                        numberDetailsDAO.insertNumberDetail(getNumberDetails());
                        setListeners(getNumberDetails());
                        return null;
                    }
                }, new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        Log.w("ByNumber", "Look up returned a null. Probably internet is not connected.");
                        address.setText("Please turn on data or WiFi.");
                        name.setText("No conn.");
                        numberType.setText("No conn.");
                        toggleLayouts(View.VISIBLE, phoneNumber);
                        return null;
                    }
                });
            }
        });

        // Launch async task to retrieve call logs and load the details in the DB, for those numbers which are not in DB
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                array = DirectorySearcher.retrieveFromLogs(context);
                Log.i("ByNumber", "Retrieved " + array.length + " numbers which are in logs but not contacts");
                for (Map.Entry<String, String> item : array) {
                    String phoneNumber = item.getKey();
                    if (phoneNumber.length() < 10) {
                        continue;
                    }
                    phoneNumber = phoneNumber.substring(phoneNumber.length() - 10);
                    // Lookup in the database first
                    final NumberDetail numberDetailLocal = numberDetailsDAO.getNumberDetail('0' + phoneNumber);
                    if (numberDetailLocal != null) {
                        continue;
                    }
                    new LookupTask().execute(phoneNumber, new SuccessCallable() {
                        @Override
                        public Void call() throws Exception {
                            address.setText(getNumberDetails().getAddress());
                            name.setText(getNumberDetails().getName());
                            numberType.setText(getNumberDetails().getNumberType());
                            numberDetailsDAO.insertNumberDetail(getNumberDetails());
                            return null;
                        }
                    }, null);
                }
                return null;
            }
        }.execute();
    }

    private void setInvalidNumber(final String message) {
        // Invalid number
        Log.w("ByNumber", message);
        address.setText("This number is invalid.");
        name.setText("Invalid number");
        numberType.setText("Invalid num.");
        toggleLayouts(View.VISIBLE, phoneNumber);
    }

    private void toggleLayouts(int visibility, String phone) {
        layout1.setVisibility(visibility);
        layout2.setVisibility(visibility);
        layout3.setVisibility(visibility);
        number.setText(phone);
    }

    private void setListeners(final NumberDetail numberDetail) {
        layout1.setOnClickListener(new RowListener(numberDetail.getName(), numberDetail.getNumber(), context));
        layout2.setOnClickListener(new RowListener(numberDetail.getName(), numberDetail.getNumber(), context));
        layout3.setOnClickListener(new RowListener(numberDetail.getName(), numberDetail.getNumber(), context));
    }

}