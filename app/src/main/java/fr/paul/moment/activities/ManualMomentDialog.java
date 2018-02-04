package fr.paul.moment.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import fr.paul.moment.R;
import fr.paul.moment.utils.Consts;


/**
 * Created by paul on 22/12/17.
 */

public class ManualMomentDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final EditText inputText = new EditText(getActivity());
        inputText.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.enter_moment_url)
                .setView(inputText)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getActivity(), MomentActivity.class);
                        intent.putExtra(Consts.MANUAL_MOMENT, inputText.getText().toString());

                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.nope, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
