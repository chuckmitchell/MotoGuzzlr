package ca.squall.motoguzzler;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCES_NAME = "FILLUP_PREFERENCES";
    public static final String PREFERENCE_UNIT_NAME = "FILLUP_PREFERENCES_UNITS";
    private static final String TAG = "MainActivity";
    ListView fillupsListView;
    TextView avgTextView, changeTextView;
    private FillupArrayAdapter mListAdapter;

    private void setupViews() {
        final Context context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fillupsListView = (ListView) findViewById(R.id.fillupListView);
        setOnItemLongClickListener(fillupsListView);

        avgTextView = (TextView) findViewById(R.id.avgTextView);
        changeTextView = (TextView) findViewById(R.id.changeTextView);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                FillupDialogFragment frag = new FillupDialogFragment();
                frag.setContext(context);
                frag.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        notifyDataSetChanged();
                    }
                });

                frag.show(ft, "txn_tag");
            }
        });

    }

    public void openSettingsMenu(MenuItem item) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        SettingsDialogFragment frag = new SettingsDialogFragment();
        frag.setContext(this);

        frag.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                notifyDataSetChanged();
            }
        });

        frag.show(ft, "txn_tag");
    }

    private void notifyDataSetChanged() {
        mListAdapter.clear();
        mListAdapter.addAll(Fillup.listAll());
        mListAdapter.notifyDataSetChanged();

        avgTextView.setText(convertMileage(Fillup.getAvgFuelEconomy()));

        BigDecimal change = Fillup.getFuelEconomyChange();
        changeTextView.setTextColor(Color.BLACK);
        if (change.compareTo(BigDecimal.ZERO) > 0) {
            changeTextView.setTextColor(Color.parseColor("#008800"));
        }

        if (change.compareTo(BigDecimal.ZERO) < 0) {
            changeTextView.setTextColor(Color.parseColor("#880000"));
        }
        changeTextView.setText(convertMileage(change));

    }

    private String convertMileage(BigDecimal change) {
        String units = getUnitPreference();

        if (units.equals("Metric")) {
            return change + " km/l";
        }

        if (units.equals("Imperial")) {
            return UnitConverter.convert(change, UnitConverter.UNIT_MPG) + " mpg";
        }

        return "ERR";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();

        mListAdapter = new FillupArrayAdapter(this, Fillup.listAll(Fillup.class));
        fillupsListView.setAdapter(mListAdapter);
        notifyDataSetChanged();
    }


    private void setOnItemLongClickListener(ListView fillupListView) {
        final Context context = this;
        fillupListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "Fillup item long clicked. " + i);
                Fillup fillup = mListAdapter.getItem(i);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                FillupDialogFragment frag = new FillupDialogFragment();
                frag.setContext(context);
                frag.setFillup(fillup);
                frag.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        notifyDataSetChanged();
                    }
                });

                frag.show(ft, "txn_tag");
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            FillupDialogFragment frag = new FillupDialogFragment();
            ft.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUnitPreferences(String unitName) {
        SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, 0);
        String units = settings.getString(PREFERENCE_UNIT_NAME, "Metric");

        if (unitName.equals(units)) {
            return;
        }

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFERENCE_UNIT_NAME, unitName);
        editor.commit();
    }

    public String getUnitPreference() {
        SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, 0);
        return settings.getString(PREFERENCE_UNIT_NAME, "Metric");
    }
}
