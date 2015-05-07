package tarwinderjosan.com.drunkoclock;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Uses CustomListAdapter to modify the views.
 */
public class WatchFaceConfigActivity extends ListActivity {

    private ArrayList<String> itemsAsList;
    // Create the list of items
    String[] items= {"Look and Feel", "Date and Time", "Features", "Weather", "Advanced",
            "Information and Support"};
    String[] classNames = {"LookFeel", "DateTime", "Features", "Weather", "Advanced", "Support"};

    private CustomListAdapter listAdapter;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        listAdapter = new CustomListAdapter(this, R.layout.custom_list, items);
        // Set the background color of the list
        getListView().setBackgroundColor(Color.parseColor("#3B7CB7"));
        setListAdapter(listAdapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        try {
            Class classToLaunch = Class.forName("tarwinderjosan.com.drunkoclock." + classNames[position]);
            Intent intent = new Intent(this, classToLaunch);
            startActivity(intent);
        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        }
    }
}
