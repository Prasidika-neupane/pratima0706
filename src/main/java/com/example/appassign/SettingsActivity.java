package com.example.appassign;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawerLayout;
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_NOTIFICATION_ENABLED = "notification_enabled";

    private Toolbar toolbar;
    private RadioGroup radioGroupCurrency;
    private Switch switchNotifications;

    private TextView textAppVersion;
    private EditText nameEditText;
    private Button saveChangesButton;

    private NavigationView navigationView;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private SharedPreferences sharedPreferences;
    private ExpenseDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        radioGroupCurrency = findViewById(R.id.radioGroupCurrency);
        switchNotifications = findViewById(R.id.switchNotifications);
        textAppVersion = findViewById(R.id.textAppVersion);
        nameEditText = findViewById(R.id.nameEditText);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        dbHelper = new ExpenseDbHelper(this);

        // Set up the initial state of the currency radio buttons
        RadioButton radioButtonAUD = findViewById(R.id.radioButtonAUD);
        RadioButton radioButtonUSD = findViewById(R.id.radioButtonUSD);
        RadioButton radioButtonNPR = findViewById(R.id.radioButtonNPR);
        RadioButton radioButtonINR = findViewById(R.id.radioButtonINR);

        // Set the initial state of notification switch based on user preferences
        boolean isNotificationsEnabled = sharedPreferences.getBoolean(KEY_NOTIFICATION_ENABLED, true);
        switchNotifications.setChecked(isNotificationsEnabled);

        // Set up the save changes button click listener
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        // Initialize the navigation drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = findViewById(R.id.toolbar);

        // Set up navigation drawer
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set navigation button click listeners
        Button homeButton = navigationView.findViewById(R.id.navHome);
        Button expenseListButton = navigationView.findViewById(R.id.navExpList);
        Button addExpenseButton = navigationView.findViewById(R.id.navAddExp);
        Button settingsButton = navigationView.findViewById(R.id.navSetting);

        homeButton.setOnClickListener(this);
        expenseListButton.setOnClickListener(this);
        addExpenseButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);

        //Set click listener for navigation drawer button
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });



        dbHelper = new ExpenseDbHelper(this);




    }

    private void saveChanges() {
        String newName = nameEditText.getText().toString().trim();

        // Validate the new name
        if (newName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the name in the database
        ExpenseDbHelper dbHelper = new ExpenseDbHelper(this);
        dbHelper.setUserName(this, newName);
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save the notification state when the activity is paused
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFICATION_ENABLED, switchNotifications.isChecked());
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        // Handle navigation button clicks here
        int viewId = v.getId();

        if (viewId == R.id.navHome) {
            // Handle Home button click
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        } else if (viewId == R.id.navExpList) {
            // Handle Expense List button click
            startActivity(new Intent(SettingsActivity.this, ExpenseListActivity.class));
        } else if (viewId == R.id.navAddExp) {
            // Handle Edit Expense button click
            startActivity(new Intent(SettingsActivity.this, AddExpense.class));
        } else if (viewId == R.id.navSetting) {
            // Handle Settings button click
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
        }

        // Close the navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Close the navigation drawer on back press
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
