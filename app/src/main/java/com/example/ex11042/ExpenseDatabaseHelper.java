package com.example.ex11042;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Itay Shehter as8891@bs.amalnet.k12.il
 * @version 1.0
 * @since 26/03/2026
 * The class manages SQLite database operations for the expenses, printing SQL queries to Logcat.
 */
public class ExpenseDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQL_DEBUG";
    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DATE = "date";

    /**
     * The method is the constructor for the ExpenseDatabaseHelper class.
     * <p>
     *
     * @param context The context used to open or create the database.
     */
    public ExpenseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * The method is called when the database is created for the first time. The method executes the table creation SQL.
     * <p>
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_DATE + " TEXT)";

        Log.d(TAG, "Creating table: " + createTable);
        db.execSQL(createTable);
    }

    /**
     * The method is called when the database needs to be upgraded. The method drops the old table and creates a new one.
     * <p>
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    /**
     * The method inserts a new expense record into the database.
     * <p>
     *
     * @param expense The expense object to insert.
     */
    public void addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, expense.getDescription());
        values.put(COLUMN_AMOUNT, expense.getAmount());
        values.put(COLUMN_CATEGORY, expense.getCategory());
        values.put(COLUMN_DATE, expense.getDate());

        Log.d(TAG, "Inserting expense: " + expense.getDescription() + ", Amount: " + expense.getAmount());
        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }

    /**
     * The method retrieves all expenses from the database.
     * <p>
     *
     * @return A list containing all the expense objects.
     */
    public List<Expense> getAllExpenses() {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EXPENSES;

        Log.d(TAG, "Executing query: " + query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                expenseList.add(new Expense(id, description, amount, category, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenseList;
    }

    /**
     * The method calculates the total amount of expenses for a specific month.
     * <p>
     *
     * @param month The month formatted as YYYY-MM.
     * @return The total sum of expenses for the given month.
     */
    public double getMonthlyTotal(String month) {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_DATE + " LIKE ?";

        Log.d(TAG, "Executing total query: " + query + " with month: " + month);
        Cursor cursor = db.rawQuery(query, new String[]{month + "%"});

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    /**
     * The method deletes an expense record from the database based on its ID.
     * <p>
     *
     * @param id The ID of the expense to be deleted.
     */
    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "Deleting expense with ID: " + id);
        db.delete(TABLE_EXPENSES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * The method updates an existing expense record in the database.
     * <p>
     *
     * @param expense The expense object containing updated information.
     */
    public void updateExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, expense.getDescription());
        values.put(COLUMN_AMOUNT, expense.getAmount());
        values.put(COLUMN_CATEGORY, expense.getCategory());
        values.put(COLUMN_DATE, expense.getDate());

        Log.d(TAG, "Updating expense ID " + expense.getId() + " with new values.");
        db.update(TABLE_EXPENSES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(expense.getId())});
        db.close();
    }
}