package com.example.electrobill;

import android.content.Intent;
import android.net.Uri; // Import Uri for opening URLs
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {

    Button btnGitHub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Back button functionality
        Button btnBackToMain = findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to the main activity
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // GitHub button functionality
        btnGitHub = findViewById(R.id.btnGitHub); // Initialize the GitHub button

        btnGitHub.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.github_foreground,0,0,0);
        btnGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the GitHub URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yucinaa"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuAbout) {
            Toast.makeText(this, "You are already on the About page.", Toast.LENGTH_SHORT).show();
            // No intent is needed since the user is already on AboutActivity
            return true;
        } else if (selected == R.id.menuInstructions) {
            Toast.makeText(this, "Instructions clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AboutActivity.this, InstructionsActivity.class);
            startActivity(intent); // Navigate to InstructionsActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
