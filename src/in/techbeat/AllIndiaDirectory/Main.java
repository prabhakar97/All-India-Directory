package in.techbeat.AllIndiaDirectory;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by prabhakar on 27/3/14.
 */
public class Main extends TabActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost tabHost = getTabHost();

        Intent intentByNumber = new Intent().setClass(this, ByNumber.class);
        TabHost.TabSpec tsbn = tabHost.newTabSpec("Number").setIndicator("Number").setContent(intentByNumber);

        Intent intentByLogs = new Intent().setClass(this, ByLogs.class);
        TabHost.TabSpec tsbl = tabHost.newTabSpec("Logs").setIndicator("Logs").setContent(intentByLogs);

        tabHost.addTab(tsbn);
        tabHost.addTab(tsbl);

        tabHost.setCurrentTab(0);
        int tabCount = tabHost.getTabWidget().getTabCount();
        for (int i = 0; i < tabCount; i++) {
            final View view = tabHost.getTabWidget().getChildTabViewAt(i);
            if ( view != null ) {
                // reduce height of the tab
                view.getLayoutParams().height *= 0.66;

                //  get title text view
                final View textView = view.findViewById(android.R.id.title);
                if ( textView instanceof TextView) {
                    // just in case check the type

                    // center text
                    ((TextView) textView).setGravity(Gravity.CENTER);
                    // wrap text
                    ((TextView) textView).setSingleLine(false);

                    // explicitly set layout parameters
                    textView.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
                    textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
            }
        }
    }
}