package ca.squall.motoguzzler;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.math.BigDecimal;

/**
 * Created by charles on 2016-08-10.
 */
public class FillupDialogFragment extends DialogFragment implements View.OnClickListener {

    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "FillupDialogFragment";
    private DialogInterface.OnDismissListener onDismissListener;
    private Context context;
    private EditText costEditText;
    private EditText amountEditText;
    private EditText kmEditText;
    private Fillup fillup;
    private ImageView deleteButton;

    public void setFillup(Fillup fillup) {
        this.fillup = fillup;

    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
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

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
        }

        if (fillup != null) {
            kmEditText.setText(fillup.getOdometer() + "");
            costEditText.setText(fillup.getCost().toPlainString());
            amountEditText.setText(fillup.getAmount().toPlainString());
            kmEditText.setText(fillup.getOdometer() + "");

            if (Fillup.last(Fillup.class).getId() == fillup.getId()) {
                deleteButton.setVisibility(View.VISIBLE);
            }
        } else {
            if (Fillup.count(Fillup.class) >= 1) {
                Fillup prevFillup = Fillup.last(Fillup.class);
                kmEditText.setText(prevFillup.getOdometer() + "");
                deleteButton.setVisibility(View.INVISIBLE);
            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fillup_fragment, container, false);

        costEditText = (EditText) root.findViewById(R.id.costEditText);
        amountEditText = (EditText) root.findViewById(R.id.amountEditText);
        kmEditText = (EditText) root.findViewById(R.id.kmEditText);

        Button cancelBtn = (Button) root.findViewById(R.id.cancelBtn);
        Button saveBtn = (Button) root.findViewById(R.id.saveBtn);

        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        deleteButton = (ImageView) root.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.cancelBtn) {
            Log.d(TAG, "Fillup Dialog Cancelled.");
            dismiss();
        }

        if (view.getId() == R.id.saveBtn) {
            Log.d(TAG, "Fillup Dialog Saved.");

            try {
                BigDecimal cost = new BigDecimal(costEditText.getText().toString());
                BigDecimal amount = new BigDecimal(amountEditText.getText().toString());
                int km = Integer.parseInt(kmEditText.getText().toString());

                Fillup newFillup = new Fillup(km, amount, cost);

                if (fillup != null) {
                    newFillup = fillup;
                    newFillup.setCost(cost);
                    newFillup.setAmount(amount);
                    newFillup.setOdometer(km);
                }

                newFillup.save();
                dismiss();
            } catch (Exception e) {
                Toast.makeText(context, "Error saving new Fillup!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Error saving Fillup");
                Log.e(TAG, e.toString(), e);
                return;
            }

        }

        if (view.getId() == R.id.deleteButton) {
            Log.d(TAG, "Delete Button clicked");
            final FillupDialogFragment fillupDialog = this;
            new AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_warning_black_18dp)
                    .setTitle("Deleting Fillup")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fillup.delete();
                            fillupDialog.dismiss();

                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
}
