package com.claudechrist.premiere_soccer_league_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewMatch extends AppCompatActivity {


    TextView scoreView;
    TextView dateView;
    TextView labelView;
    TextView toolBarScoreView;
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


        Toast.makeText(this, "" + MainActivity.intent.getItemPosition(), Toast.LENGTH_SHORT).show();


        String scoreToolbarText = "Test";
        toolBarScoreView.setText(scoreToolbarText);
    }
}
