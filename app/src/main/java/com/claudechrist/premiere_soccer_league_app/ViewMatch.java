package com.claudechrist.premiere_soccer_league_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ViewMatch extends AppCompatActivity implements EditMatchDialogFragment.EditDialogListener {

    static Match match;
    TextView scoreView;
    TextView dateView;
    TextView labelView;
    TextView toolBarScoreView;
    EditText inputCommentEditText;
    DialogFragment editMatchDialogFragment;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    MatchComment matchComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_match);

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        inputCommentEditText = findViewById(R.id.editText);
        scoreView = findViewById(R.id.scoreTextView);
        dateView = findViewById(R.id.dateTextView);
        labelView = findViewById(R.id.labelTextView);
        toolBarScoreView = findViewById(R.id.titleToolBar);

        Toolbar toolbar = findViewById(R.id.viewMatchToolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewMatch.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        MatchComment matchComment = (MatchComment) intent.getSerializableExtra("match_comment");
        if (matchComment == null)
            matchComment = new MatchComment();

        this.matchComment = matchComment;

        int i = MainActivity.intent.getItemPosition();
        match = MainActivity.matches.get(i);
        String matchResult = match.getTeamA() + " " + match.getScoreA() + " : " + match.getScoreB() +
                " " + match.getTeamB();

        dateView.setText(match.getDate());
        scoreView.setText(matchResult);
        labelView.setText(match.getLabel());
        toolBarScoreView.setText(matchResult);

    }

    RecyclerView matchComments;
    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUtil.openReference("match_comments", this);
        matchComments = findViewById(R.id.rv_comments);
        final CommentsAdapter adapter = new CommentsAdapter();
        matchComments.setAdapter(adapter);
        LinearLayoutManager commentsLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        matchComments.setLayoutManager(commentsLayoutManager);
        FirebaseUtil.attachListener();
    }

    public void addMatchComment(View view) {
        String input = inputCommentEditText.getText().toString();
        MatchComment matchC;
        if (!input.equals("")) {
            matchC = new MatchComment(match, input);
            mDatabaseReference.push().setValue(matchC);
//            matchComment = matchC;
            inputCommentEditText.setText("");
        } else {
            Toast.makeText(this, "Please type in your comment", Toast.LENGTH_SHORT).show();
        }
    }

    public  void deleteMatchComment(View view) {
        mDatabaseReference.child(matchComment.getId()).removeValue();
//        matchComments.getAdapter().;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_edit_match_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_item) {
            editMatchDialogFragment = new EditMatchDialogFragment();

            editMatchDialogFragment.show(getSupportFragmentManager(), "Edit Match");
            Toast.makeText(this, "Edit clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void pickDate(View view) {
        Calendar c = Calendar.getInstance();

        int myDay = c.get(Calendar.DAY_OF_MONTH);
        int myMonth = c.get(Calendar.MONTH);
        int myYear = c.get(Calendar.YEAR);


        DatePickerDialog datePickerDialog = new DatePickerDialog(ViewMatch.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String myMonth = "";

                switch (month) {
                    case 0:
                        myMonth = "January";
                        break;
                    case 1:
                        myMonth = "February";
                        break;
                    case 2:
                        myMonth = "March";
                        break;
                    case 3:
                        myMonth = "April";
                        break;
                    case 4:
                        myMonth = "May";
                        break;
                    case 5:
                        myMonth = "June";
                        break;
                    case 6:
                        myMonth = "July";
                        break;
                    case 7:
                        myMonth = "August";
                        break;
                    case 8:
                        myMonth = "September";
                        break;
                    case 9:
                        myMonth = "October";
                        break;
                    case 10:
                        myMonth = "November";
                        break;
                    case 11:
                        myMonth = "December";
                        break;
                }

                String date = dayOfMonth + " " + myMonth + " " + year;
                View editMatchView = editMatchDialogFragment.getView();
                if (editMatchView != null) {
                    Button button = editMatchView.findViewById(R.id.pick_date_btn_edit);
                    button.setText(date);
                }
            }
        }, myYear, myMonth, myDay);

        datePickerDialog.show();
    }

    @Override
    public void onEditDialogListener(Match match) {
        if (validateEdDialogListener()) {
            String sql = "UPDATE 'matches' SET teamA = '" + match.getTeamA() +
                    "', scoreA = " + match.getScoreA() + ", teamB = '" + match.getTeamB() + "', scoreB = " + match.getScoreB()
                    + ", label = '" + match.getLabel() + "', date = '" + match.getDate() + "' WHERE id = " + match.getId() + ";";
            MainActivity.soccerEventSDatabase.execSQL(sql);

            Toast.makeText(ViewMatch.this, "Match edited", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ViewMatch.this, "Input invalid", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean validateEdDialogListener() {
        View view = editMatchDialogFragment.getView();

        EditText teamA = view.findViewById(R.id.teamA_editext_edit_box);
        EditText scoreTeamA = view.findViewById(R.id.teamA_score_editext_edit_box);
        EditText teamB = view.findViewById(R.id.teamB_editext_edit_box);
        EditText scoreTeamB = view.findViewById(R.id.teamB_score_editext_edit_box);
        EditText label = view.findViewById(R.id.label_editext_edit_box);
        Button pickDateButton = view.findViewById(R.id.pick_date_btn_edit);


        // validate the data
        String teamA1 = teamA.getText().toString();
        String teamB1 = teamB.getText().toString();
        String label1 = label.getText().toString();
        String date = pickDateButton.getText().toString();

        if (!teamA1.equals("") && !teamB1.equals("") && !scoreTeamA.getText().toString().equals("")
                && !scoreTeamB.getText().toString().equals("") && !label1.equals("")
                && date.matches(".*\\d.*")) {

            int scoreA = Integer.parseInt(scoreTeamA.getText().toString());
            int scoreB = Integer.parseInt(scoreTeamB.getText().toString());

            match.setTeamA(teamA1);
            match.setTeamB(teamB1);
            match.setScoreA(scoreA);
            match.setScoreB(scoreB);
            match.setLabel(label1);
            match.setDate(date);

            // update view
            Intent i = new Intent(ViewMatch.this, ViewMatch.class);
            finish();
            overridePendingTransition(0, 0);
            startActivity(i);
            overridePendingTransition(0, 0);

            return true;
        }

        return false;
    }
}
