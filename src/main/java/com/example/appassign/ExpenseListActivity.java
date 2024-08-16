package com.example.appassign;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private View toolbar;

    private ListView expenseListView;
    private ExpenseListAdapter expenseListAdapter;

    private ExpenseDbHelper dbHelper;
    private SQLiteDatabase database;

    private List<Expense> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        // Initialize the database helper
        dbHelper = new ExpenseDbHelper(this);

        // Get a readable database
        database = dbHelper.getReadableDatabase();

        // Find views
        expenseListView = findViewById(R.id.expenseListView);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        // Set click listener for navigation drawer button
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });



        dbHelper = new ExpenseDbHelper(this);



        // Initialize the navigation drawer
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set navigation item selected listener
        navigationView.setNavigationItemSelectedListener(this);

        // Populate the expense list
        expenseList = fetchExpensesFromDatabase();

        // Initialize the expense list adapter
        expenseListAdapter = new ExpenseListAdapter(this, expenseList);

        // Set the adapter for the expense list view
        expenseListView.setAdapter(expenseListAdapter);

        // Set item click listener for the expense list view
        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Handle expense item click
                Expense expense = expenseList.get(position);
                Toast.makeText(ExpenseListActivity.this, "Clicked: " + expense.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set item long click listener for the expense list view
        expenseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Handle long press on expense item
                Expense expense = expenseList.get(position);
                Toast.makeText(ExpenseListActivity.this, "Long Pressed: " + expense.getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // Set click listener for navigation buttons
        Button navHome = navigationView.findViewById(R.id.navHome);
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExpenseListActivity.this, MainActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        Button navExpList = navigationView.findViewById(R.id.navExpList);
        navExpList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExpenseListActivity.this, "You are already on the Expense List page.", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        Button navAddExp = navigationView.findViewById(R.id.navAddExp);
        navAddExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExpenseListActivity.this, AddExpense.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        Button navSetting = navigationView.findViewById(R.id.navSetting);
        navSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExpenseListActivity.this, SettingsActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

    }

    private List<Expense> fetchExpensesFromDatabase() {
        List<Expense> expenses = new ArrayList<>();

        // Define the columns to retrieve from the database
        String[] projection = {
                ExpenseDbHelper.COLUMN_ID,
                ExpenseDbHelper.COLUMN_EXPENSE_NAME,
                ExpenseDbHelper.COLUMN_EXPENSE_AMOUNT
        };

        // Execute the query to fetch all expenses
        Cursor cursor = database.query(
                ExpenseDbHelper.TABLE_EXPENSES,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        // Iterate through the cursor and create Expense objects
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ExpenseDbHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDbHelper.COLUMN_EXPENSE_NAME));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(ExpenseDbHelper.COLUMN_EXPENSE_AMOUNT));

            Expense expense = new Expense(id, name, amount);
            expenses.add(expense);
        }

        // Close the cursor
        cursor.close();

        return expenses;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item clicks here
        int itemId = item.getItemId();

        if (itemId == R.id.navHome) {
            // Handle Home button click
            startActivity(new Intent(ExpenseListActivity.this, MainActivity.class));
        } else if (itemId == R.id.navExpList) {
            // Handle Expense List button click (already in ExpenseListActivity)
            Toast.makeText(this, "You are already on the Expense List page.", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.navAddExp) {
            // Handle Add Expense button click
            startActivity(new Intent(ExpenseListActivity.this, AddExpense.class));
        } else if (itemId == R.id.navSetting) {
            // Handle Settings button click
            startActivity(new Intent(ExpenseListActivity.this, SettingsActivity.class));
        }

        // Close the navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
