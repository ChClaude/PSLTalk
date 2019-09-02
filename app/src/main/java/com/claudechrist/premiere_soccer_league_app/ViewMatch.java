package com.claudechrist.premiere_soccer_league_app;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewMatch extends AppCompatActivity {


    TextView scoreView;
    TextView dateView;
    TextView labelView;
    TextView toolBarScoreView;
    static Match match;
    private Button commentsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_match);

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


        int i = MainActivity.intent.getItemPosition();

        match = MainActivity.matches.get(i);
        String matchResult = match.getTeamA() + " " + match.getScoreA() + " : " + match.getScoreB() +
                " " + match.getTeamB();

        dateView.setText(match.getDate());
        scoreView.setText(matchResult);
        labelView.setText(match.getLabel());
        toolBarScoreView.setText(matchResult);

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
            DialogFragment editMatchDialogFragment = new EditMatchDialogFragment();

            editMatchDialogFragment.show(getSupportFragmentManager(), "Edit Match");
            Toast.makeText(this, "Edit clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
