package com.claudechrist.premiere_soccer_league_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static SQLiteDatabase soccerEventSDatabase;

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

        try {
            soccerEventSDatabase = this.openOrCreateDatabase("Soccer Events", MODE_PRIVATE, null);
            soccerEventSDatabase.execSQL("CREATE TABLE IF NOT EXISTS 'match' (id INT PRIMARY KEY, teamA VARCHAR, sccoreA INT, teamB VARCHAR, sccoreB INT, label VARCHAR, date VARCHAR)");

            Cursor c = soccerEventSDatabase.rawQuery("SELECT * FROM 'match'", null);
            int idIndex = c.getColumnIndex("id");
            int teamAIndex = c.getColumnIndex("teamA");
            int teamBIndex = c.getColumnIndex("teamB");
            int dateIndex = c.getColumnIndex("date");

            Log.i("ID index", Integer.toString(idIndex));
            Log.i("Team A index", Integer.toString(teamAIndex));
            Log.i("Team B index", Integer.toString(teamBIndex));
            Log.i("Date index", Integer.toString(dateIndex));

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
