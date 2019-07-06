package com.claudechrist.premiere_soccer_league_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static SQLiteDatabase soccerEventSDatabase;
    ListView matchesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddAndEditActivity.class);
                startActivity(intent);
            }
        });

        matchesListView = findViewById(R.id.matchesListView);


        try {
            soccerEventSDatabase = this.openOrCreateDatabase("Soccer Events", MODE_PRIVATE, null);
            soccerEventSDatabase.execSQL("CREATE TABLE IF NOT EXISTS 'matches' (id INTEGER PRIMARY KEY, " +
                    "teamA VARCHAR, scoreA INT, teamB VARCHAR, scoreB INT, label VARCHAR, date VARCHAR)");

            // arrayList that takes in the element to display in the list
            ArrayList<String> matches = new ArrayList<>();

            Cursor c = soccerEventSDatabase.rawQuery("SELECT * FROM 'matches'", null);
            int teamAIndex = c.getColumnIndex("teamA");
            int scoreAIndex = c.getColumnIndex("scoreA");

            int teamBIndex = c.getColumnIndex("teamB");
            int scoreBIndex = c.getColumnIndex("scoreB");

            int labelIndex = c.getColumnIndex("label");
            int dateIndex = c.getColumnIndex("date");

            // adding the different matches to the arrayList
            while (c.moveToNext()) {

                matches.add((new Match(c.getString(teamAIndex), c.getString(teamBIndex),
                        c.getInt(scoreAIndex), c.getInt(scoreBIndex), c.getString(labelIndex), c.getString(dateIndex))).toString());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, matches);
            matchesListView.setAdapter(arrayAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_match:
                Toast.makeText(this, "search icon clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.app_settings:
                Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.new_match:
                Toast.makeText(this, "new match clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete_match:
                Toast.makeText(this, "delete match clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }
}
