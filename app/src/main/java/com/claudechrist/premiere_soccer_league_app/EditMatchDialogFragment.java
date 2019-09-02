package com.claudechrist.premiere_soccer_league_app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditMatchDialogFragment extends DialogFragment {

    EditText teamA;
    EditText scoreTeamA;
    EditText teamB;
    EditText scoreTeamB;
    EditText label;
    Button pickDateButton;
    View view;
    Match match;
    // Use this instance of the interface to deliver action events
    EditDialogListener listener;

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(R.layout.edit_view, null);
        builder.setView(view);

        teamA = view.findViewById(R.id.teamA_editext_edit_box);
        scoreTeamA = view.findViewById(R.id.teamA_score_editext_edit_box);
        teamB = view.findViewById(R.id.teamB_editext_edit_box);
        scoreTeamB = view.findViewById(R.id.teamB_score_editext_edit_box);
        label = view.findViewById(R.id.label_editext_edit_box);
        pickDateButton = view.findViewById(R.id.pick_date_btn_edit);

        builder.setPositiveButton("Validate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onEditDialogListener(match);
            }
        })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditMatchDialogFragment.this.getDialog().cancel();
                    }
                });


        // setting data
        match = ViewMatch.match;
        teamA.setText(match.getTeamA());

        String scoreTeamAValue = Integer.toString(match.getScoreA());
        String scoreTeamBValue = Integer.toString(match.getScoreB());

        scoreTeamA.setText(scoreTeamAValue);
        teamB.setText(match.getTeamB());
        scoreTeamB.setText(scoreTeamBValue);
        label.setText(match.getLabel());
        pickDateButton.setText(match.getDate());


        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the EditDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditDialogListener so we can send events to the host
            listener = (EditDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(this.toString()
                    + " must implement EditDialogListener");
        }
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface EditDialogListener {
        void onEditDialogListener(Match match);
        boolean validateEdDialogListener();
    }


}
