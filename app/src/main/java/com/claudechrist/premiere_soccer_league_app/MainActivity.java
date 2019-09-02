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
import android.widget.AbsListView;
import android.widget.AdapterView;
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
    private ArrayList<Integer> selectedItems;

    // action mode callback
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
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        matchesListView = findViewById(R.id.matchesListView);
        selectedItems = new ArrayList<>();

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
            int iDIndex = c.getColumnIndex("id");

            // adding the different matches to the arrayList
            while (c.moveToNext()) {

                matches.add(new Match(c.getString(teamAIndex), c.getString(teamBIndex),
                        c.getInt(scoreAIndex), c.getInt(scoreBIndex), c.getString(labelIndex),
                        c.getString(dateIndex), c.getInt(iDIndex)));
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

        matchesListView.setLongClickable(true);

        matchesListView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mActionMode != null) {
                    return false;
                }

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startSupportActionMode(mActionModeCallback);
                return true;
            }
        });

        matchesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        matchesListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
                if (checked) {
                    selectedItems.add(position);
                } else {
                    selectedItems.remove((Integer) position);
                }

                Toast.makeText(MainActivity.this, "Position: " + position +
                        (checked ? " selected" : " deselected"), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.delete_match_context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an <code><a href="/reference/android/view/ActionMode.html#invalidate()">invalidate()</a></code> request
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.delete_match_action:
                        deleteSelectedItems(selectedItems);
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);

            }
        });

        matchesListView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mActionMode != null)
                    return false;

                selectedItems.clear();
                mActionMode = startSupportActionMode(mActionModeCallback);
                return true;
            }
        });

    }

    public void deleteSelectedItems(ArrayList<Integer> positions) {
        for (int i : positions) {
            Match match = matches.get(i);
            soccerEventSDatabase.execSQL("DELETE FROM 'matches' WHERE id=" + match.getId());
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
