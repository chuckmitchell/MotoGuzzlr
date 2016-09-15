package ca.squall.motoguzzler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by charles on 2016-08-10.
 */
public class FillupArrayAdapter extends ArrayAdapter<Fillup> {

    private static final String TAG = "FillupArrayAdapter";
    private final List<Fillup> fillups;

    public FillupArrayAdapter(Context context, List<Fillup> fillups) {
        super(context, 0, fillups);
        this.fillups = fillups;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View fillUpView = inflater.inflate(R.layout.fillup_list_item, parent, false);
        TextView odometerTextView = (TextView) fillUpView.findViewById(R.id.odometerTextView);
        TextView costTextView = (TextView) fillUpView.findViewById(R.id.costTextView);
        TextView amountTextView = (TextView) fillUpView.findViewById(R.id.amountTextView);
        TextView fuelEconomyTextView = (TextView) fillUpView.findViewById(R.id.fuelEconomyTextView);
        TextView dateTextView = (TextView) fillUpView.findViewById(R.id.dateTextView);

        Fillup fillup = fillups.get(position);
        costTextView.setText("$" + fillup.getCost());
        amountTextView.setText("" + fillup.getAmount() + "l");
        odometerTextView.setText(fillup.getOdometer() + " km");
        fuelEconomyTextView.setText(fillup.getFuelEconomy().toPlainString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.CANADA);
        dateTextView.setText(simpleDateFormat.format(fillup.getDate()));

        if (position == 0) {
            fuelEconomyTextView.setVisibility(View.INVISIBLE);
        }

        return fillUpView;
    }
}
