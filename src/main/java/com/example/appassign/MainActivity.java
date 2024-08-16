package com.example.appassign;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private ImageView logoImageView;

    private View toolbar;

    private TextView totalExpenseTextView;
    private Button addExpBtn;

    // Database variables
    private ExpenseDbHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        logoImageView = findViewById(R.id.logoImageView);
        totalExpenseTextView = findViewById(R.id.totalExpenseTextView);
        addExpBtn = findViewById(R.id.addExpenseButton);
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

        // Set click listener for navigation drawer button
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Set click listener for add expense button
        addExpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Add Expense page
                startActivity(new Intent(MainActivity.this, AddExpense.class));
            }
        });

        // Set navigation item selected listener
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize the database helper
        dbHelper = new ExpenseDbHelper(this);

        // Get a readable database
        database = dbHelper.getReadableDatabase();

        // Update total expense amount
        updateTotalExpenses();
    }

    private void updateTotalExpenses() {
        double totalExpenses = calculateTotalExpenses();
        String totalExpensesString = "$" + String.format("%.2f", totalExpenses);
        totalExpenseTextView.setText(totalExpensesString);
    }

    private double calculateTotalExpenses() {
        ExpenseDbHelper dbHelper = new ExpenseDbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String query = "SELECT SUM(" + ExpenseDbHelper.COLUMN_EXPENSE_AMOUNT + ") FROM " + ExpenseDbHelper.TABLE_EXPENSES;
        Cursor cursor = database.rawQuery(query, null);

        double totalExpenses = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            totalExpenses = cursor.getDouble(0);
            cursor.close();
        }

        return totalExpenses;
    }

    @Override
    public void onClick(View v) {
        // Handle navigation button clicks here
        int viewId = v.getId();

        if (viewId == R.id.navHome) {
            // Handle Home button click
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        } else if (viewId == R.id.navExpList) {
            // Handle Expense List button click
            startActivity(new Intent(MainActivity.this, ExpenseListActivity.class));
        } else if (viewId == R.id.navAddExp) {
            // Handle Add Expense button click
            startActivity(new Intent(MainActivity.this, AddExpense.class));
        } else if (viewId == R.id.navSetting) {
            // Handle Settings button click
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
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
