package ca.squall.motoguzzler;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by charles on 2016-09-16.
 */

public class SettingsDialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final String TAG = "FillupDialogFragment";
    MainActivity context;
    private DialogInterface.OnDismissListener onDismissListener;

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.settings_fragment, container, false);

        Spinner unitSpinner = (Spinner) root.findViewById(R.id.unit_spinner);
        ArrayAdapter unitAdapter = ArrayAdapter.createFromResource(context, R.array.units_array, android.R.layout.simple_spinner_item);

        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);

        unitSpinner.setOnItemSelectedListener(this);

        String unitPreference = context.getUnitPreference();
        int position = unitAdapter.getPosition(unitPreference);
        unitSpinner.setSelection(position);


        return root;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onClick(View view) {

    }

    public void setContext(MainActivity context) {
        this.context = context;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "Unit item Selected");

        String choice = ((AppCompatTextView) (view)).getText().toString();
        context.setUnitPreferences(choice);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d(TAG, "Unit item nothing Selected");
    }
}
