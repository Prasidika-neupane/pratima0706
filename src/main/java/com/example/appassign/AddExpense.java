package com.example.appassign;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AddExpense extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private View toolbar;

    private EditText expenseNameEditText;
    private EditText expenseAmountEditText;
    private Button addExpenseButton;


    private Button seeListButton;

    private ExpenseDbHelper dbHelper;
    private SQLiteDatabase database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Initialize the database helper
        dbHelper = new ExpenseDbHelper(this);

        // Get a writable database
        database = dbHelper.getWritableDatabase();

        // Find views
        expenseNameEditText = findViewById(R.id.expenseNameEditText);
        expenseAmountEditText = findViewById(R.id.expenseAmountEditText);
        addExpenseButton = findViewById(R.id.addExpenseButton);



        // Set click listener for the add expense button
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the expense name and amount
                String expenseName = expenseNameEditText.getText().toString();
                String expenseAmount = expenseAmountEditText.getText().toString();

                // Insert the expense into the database
                insertExpense(expenseName, expenseAmount);
            }

        });

        seeListButton = findViewById(R.id.listExpenseButton);

        seeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the list page
                startActivity(new Intent(AddExpense.this, ExpenseListActivity.class));
            }
        });
        // Initialize the navigation drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = findViewById(R.id.toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

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
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        dbHelper = new ExpenseDbHelper(this);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // Handle navigation button clicks here
        int viewId = v.getId();

        if (viewId == R.id.navHome) {
            // Handle Home button click
            startActivity(new Intent(AddExpense.this, MainActivity.class));
        } else if (viewId == R.id.navExpList) {
            // Handle Expense List button click
            startActivity(new Intent(AddExpense.this, ExpenseListActivity.class));
        } else if (viewId == R.id.navAddExp) {
            // Handle Add Expense button click
            startActivity(new Intent(AddExpense.this, AddExpense.class));
        } else if (viewId == R.id.navSetting) {
            // Handle Settings button click
            startActivity(new Intent(AddExpense.this, SettingsActivity.class));
        }

        // Close the navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //Update user name



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void insertExpense(String expenseName, String expenseAmount) {
        ContentValues values = new ContentValues();
        values.put(ExpenseDbHelper.COLUMN_EXPENSE_NAME, expenseName);
        values.put(ExpenseDbHelper.COLUMN_EXPENSE_AMOUNT, expenseAmount);

        // Insert the new expense record into the database
        long newRowId = database.insert(ExpenseDbHelper.TABLE_EXPENSES, null, values);

        if (newRowId != -1) {
            // Insertion successful
            Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show();

            // Perform any additional operations
        } else {
            // Error occurred during insertion
            Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();

            // Handle the error
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection
        dbHelper.close();
    }
}
