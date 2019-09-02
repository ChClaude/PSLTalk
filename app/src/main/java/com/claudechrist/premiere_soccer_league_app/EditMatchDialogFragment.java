package com.claudechrist.premiere_soccer_league_app;

import android.app.Dialog;
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



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.edit_view, null);
        builder.setView(view);

        builder.setPositiveButton("Validate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // validate the data
            }
        })

        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditMatchDialogFragment.this.getDialog().cancel();
            }
        });

        EditText teamA = view.findViewById(R.id.teamA_editext_edit_box);
        EditText scoreTeamA = view.findViewById(R.id.teamA_score_editext_edit_box);
        EditText teamB = view.findViewById(R.id.teamB_editext_edit_box);
        EditText scoreTeamB = view.findViewById(R.id.teamB_score_editext_edit_box);
        EditText label = view.findViewById(R.id.label_editext_edit_box);
        Button pickDateButton = view.findViewById(R.id.pick_date_btn_edit);

        // setting data
        Match match = ViewMatch.match;
        teamA.setText(match.getTeamA());
//        scoreTeamA.setText(match.getScoreA());
        teamB.setText(match.getTeamB());
//        scoreTeamB.setText(match.getScoreB());
        label.setText(match.getLabel());
        pickDateButton.setText(match.getDate());


        return builder.create();
    }
}
