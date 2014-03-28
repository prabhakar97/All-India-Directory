package in.techbeat.AllIndiaDirectory.helpers;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.View;

/**
 * Created by prabhakar on 26/3/14.
 */
public class RowListener implements View.OnClickListener {

    private String name;
    private String number;
    private Context context;

    public RowListener(final String name, final String number, final Context context) {
        this.name = name;
        this.number = number;
        this.context = context;
    }
    @Override
    public void onClick(View view) {
        Intent i = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        i.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        i.putExtra(ContactsContract.Intents.Insert.NAME, name);
        i.putExtra(ContactsContract.Intents.Insert.PHONE, number);
        context.startActivity(i);
    }
}
