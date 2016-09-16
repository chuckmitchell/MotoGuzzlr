package ca.squall.motoguzzler;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

/**
 * Created by charles on 2016-09-16.
 */

public class SettingsDialogFragment extends DialogFragment implements View.OnClickListener {

    private final String TAG = "FillupDialogFragment";
    private DialogInterface.OnDismissListener onDismissListener;

    Context context;

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

        return root;
    }

    @Override
    public void onClick(View view) {

    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }



}
