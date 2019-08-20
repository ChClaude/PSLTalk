package com.claudechrist.premiere_soccer_league_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static SQLiteDatabase soccerEventSDatabase;
    static ArrayList<Match> matches;
    static CustomIntent intent;
    ListView matchesListView;
    private FirebaseAuth mAuth;
    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.delete_match_context_menu, menu);
            actionMode.setTitle("Delete Match");

            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete_match_action:
                    Toast.makeText(MainActivity.this, "Delete match action clicked", Toast.LENGTH_SHORT).show();
                    actionMode.finish();
                    return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
        }
    };

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
            matches = new ArrayList<>();

            Cursor c = soccerEventSDatabase.rawQuery("SELECT * FROM 'matches'", null);
            int teamAIndex = c.getColumnIndex("teamA");
            int scoreAIndex = c.getColumnIndex("scoreA");

            int teamBIndex = c.getColumnIndex("teamB");
            int scoreBIndex = c.getColumnIndex("scoreB");

            int labelIndex = c.getColumnIndex("label");
            int dateIndex = c.getColumnIndex("date");

            // adding the different matches to the arrayList
            while (c.moveToNext()) {

                matches.add(new Match(c.getString(teamAIndex), c.getString(teamBIndex),
                        c.getInt(scoreAIndex), c.getInt(scoreBIndex), c.getString(labelIndex), c.getString(dateIndex)));
            }

            CustomAdapter arrayAdapter = new CustomAdapter(this, matches);
            matchesListView.setAdapter(arrayAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }

        matchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new CustomIntent(MainActivity.this, ViewMatch.class, position);
                startActivity(intent);
            }
        });


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
            case R.id.log_out:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Logout", "User Logged out");
                                FirebaseUtil.attachListener();
                            }
                        });
                FirebaseUtil.detachListener();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        /*MenuItem item = menu.findItem(R.id.delete_match);
        item.getActionView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mActionMode != null)
                    return false;

                mActionMode = startSupportActionMode(mActionModeCallback);

                Toast.makeText(MainActivity.this, "long click working", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUtil.openReference(this);
        FirebaseUtil.attachListener();
    }

}
