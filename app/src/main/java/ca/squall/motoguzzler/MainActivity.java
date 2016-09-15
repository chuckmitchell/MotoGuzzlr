package ca.squall.motoguzzler;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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

    private static final String TAG = "MainActivity";
    ListView fillupsListView;
    TextView avgTextView, changeTextView;
    private FillupArrayAdapter mListAdapter;

    public void setAverageFuelConsumption(BigDecimal fuelConsumption) {
        avgTextView.setText(fuelConsumption + "");
    }

    public void setFuelConsumptionChange(String fuelConsumption) {
        changeTextView.setText(fuelConsumption);
    }

    private void setupViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fillupsListView = (ListView) findViewById(R.id.fillupListView);
        setOnItemLongClickListener(fillupsListView);

        avgTextView = (TextView) findViewById(R.id.avgTextView);
        changeTextView = (TextView) findViewById(R.id.changeTextView);

        final Context context = this;

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

    private void notifyDataSetChanged() {
        mListAdapter.clear();
        mListAdapter.addAll(Fillup.listAll());
        mListAdapter.notifyDataSetChanged();

        avgTextView.setText(Fillup.getAvgFuelEconomy() + " km/l");

        BigDecimal change = Fillup.getFuelEconomyChange();
        changeTextView.setTextColor(Color.BLACK);
        if (change.compareTo(BigDecimal.ZERO) > 0) {
            changeTextView.setTextColor(Color.parseColor("#008800"));
        }

        if (change.compareTo(BigDecimal.ZERO) < 0) {
            changeTextView.setTextColor(Color.parseColor("#880000"));
        }
        changeTextView.setText(change + " km/l");

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
}
