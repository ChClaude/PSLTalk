package com.claudechrist.premiere_soccer_league_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    Calendar c;
    DatePickerDialog datePickerDialog;
    String date;
    private Button pickDateBtn;
    private EditText teamAEditText;
    private EditText teamBEditText;
    private EditText labelEditText;
    private int scoreA;
    private int scoreB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_match);

        Toolbar toolbar = findViewById(R.id.viewMatchToolBar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_close);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        teamAEditText = findViewById(R.id.teamAEditText);
        teamBEditText = findViewById(R.id.teamBEditText);
        labelEditText = findViewById(R.id.labelEditText);

        pickDateBtn = findViewById(R.id.pickDateBtn);

        final ScrollableNumberPicker horizontalNumberPicker_1 = findViewById(R.id.number_picker_horizontal_scroll_1);
        final ScrollableNumberPicker horizontalNumberPicker_2 = findViewById(R.id.number_picker_horizontal_scroll_2);

        horizontalNumberPicker_1.setListener(new ScrollableNumberPickerListener() {
            @Override
            public void onNumberPicked(int value) {
                scoreA = value;
            }
        });

        horizontalNumberPicker_2.setListener(new ScrollableNumberPickerListener() {
            @Override
            public void onNumberPicked(int value) {
                scoreB = value;
            }
        });


    }

    /**
     * Add the check menu icon to the tool bar
     * in AddActivity
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);

        return true;
    }

    public void pickDate(View view) {
        c = Calendar.getInstance();

        int myDay = c.get(Calendar.DAY_OF_MONTH);
        int myMonth = c.get(Calendar.MONTH);
        int myYear = c.get(Calendar.YEAR);


        datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
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


                date = dayOfMonth + " " + myMonth + " " + year;
                pickDateBtn.setText(date);
            }
        }, myYear, myMonth, myDay);


        datePickerDialog.show();

    }

    public void validateAndInsertMatch(MenuItem item) {
        boolean successful = false;
        String _teamA = teamAEditText.getText().toString();
        String _teamB = teamBEditText.getText().toString();
        String _label = labelEditText.getText().toString();
        String _pickDateStr = pickDateBtn.getText().toString();

        if (!_teamA.equals("") && !_teamB.equals("") &&
                !_label.equals("") && _pickDateStr.matches(".*\\d.*")) {

            MainActivity.soccerEventSDatabase.execSQL("INSERT INTO 'matches' (teamA, scoreA, teamB, " +
                    "scoreB, label, date) VALUES ('" + _teamA + "'," + scoreA + ", '" + _teamB + "' ," + scoreB + ", '" +
                    _label + "' , '" + _pickDateStr + "')");

            successful = true;
        }

        if (successful) {
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Make sure to fill in the necessary details", Toast.LENGTH_SHORT).show();
        }
    }

}
