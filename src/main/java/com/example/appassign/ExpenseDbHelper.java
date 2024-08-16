package com.example.appassign;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "expense.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EXPENSE_NAME = "expense_name";
    public static final String COLUMN_EXPENSE_AMOUNT = "expense_amount";

    // User table name and column names
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "user_name";

    // Create expenses table query
    private static final String CREATE_TABLE_EXPENSES = "CREATE TABLE " + TABLE_EXPENSES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_EXPENSE_NAME + " TEXT, " +
            COLUMN_EXPENSE_AMOUNT + " REAL);";

    // Create user table query
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " (" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_NAME + " TEXT);";

    public ExpenseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EXPENSES);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the tables if they exist and create new ones
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public Expense getExpenseById(SQLiteDatabase database, int expenseId) {
        database = this.getReadableDatabase();

        Cursor cursor = database.query(
                TABLE_EXPENSES,
                null,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(expenseId)},
                null,
                null,
                null
        );

        Expense expense = null;
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_NAME));
            @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_EXPENSE_AMOUNT));

            expense = new Expense(name, amount);
        }

        if (cursor != null) {
            cursor.close();
        }

        return expense;
    }

    public List<Expense> getAllExpenses() {
        SQLiteDatabase database = this.getReadableDatabase();

        List<Expense> expenses = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_EXPENSES,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_NAME));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_EXPENSE_AMOUNT));

                Expense expense = new Expense(name, amount);
                expenses.add(expense);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return expenses;
    }

    public long addExpense(int id, String name, double amount) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_EXPENSE_NAME, name);
        values.put(COLUMN_EXPENSE_AMOUNT, amount);

        return database.insert(TABLE_EXPENSES, null, values);
    }

    @SuppressLint("Range")
    public String getUserName() {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(
                TABLE_USER,
                null,
                null,
                null,
                null,
                null,
                null
        );

        String userName = null;
        if (cursor != null && cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
        }

        if (cursor != null) {
            cursor.close();
        }

        return userName;
    }
    public void setUserName(Context context, String newName) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, newName);

        // Insert the new user name
        long newRowId = database.insert(TABLE_USER, null, values);

        if (newRowId != -1) {
            // Insertion was successful
            Toast.makeText(context, "Name added successfully", Toast.LENGTH_SHORT).show();

        } else {
            // Insertion failed
            Toast.makeText(context, "Failed to add name", Toast.LENGTH_SHORT).show();
        }
    }



}
