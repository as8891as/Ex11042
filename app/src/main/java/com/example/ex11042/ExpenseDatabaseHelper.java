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
 * @since 26/1/2026
 * The class manages database creation, upgrades, and all CRUD operations.
 */
public class ExpenseDatabaseHelper extends SQLiteOpenHelper {

    /**
     * The method constructs the database helper.
     * <p>
     *
     * @param context The context of the application
     */
    public ExpenseDatabaseHelper(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    /**
     * The method creates the database tables when initialized for the first time.
     * <p>
     *
     * @param db The SQLite database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + DatabaseConstants.TABLE_EXPENSES + " (" +
                DatabaseConstants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseConstants.COLUMN_DESCRIPTION + " TEXT, " +
                DatabaseConstants.COLUMN_AMOUNT + " REAL, " +
                DatabaseConstants.COLUMN_CATEGORY + " TEXT, " +
                DatabaseConstants.COLUMN_DATE + " TEXT)";
        Log.d("SQL_QUERY", createTableQuery);
        db.execSQL(createTableQuery);
    }

    /**
     * The method upgrades the database if the version number increases.
     * <p>
     *
     * @param db The SQLite database instance
     * @param oldVersion The old version number
     * @param newVersion The new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_EXPENSES;
        Log.d("SQL_QUERY", dropTableQuery);
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    /**
     * The method adds a new expense to the database.
     * <p>
     *
     * @param expense The expense object to insert
     * @return The method returns the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.COLUMN_DESCRIPTION, expense.getDescription());
        values.put(DatabaseConstants.COLUMN_AMOUNT, expense.getAmount());
        values.put(DatabaseConstants.COLUMN_CATEGORY, expense.getCategory());
        values.put(DatabaseConstants.COLUMN_DATE, expense.getDate());
        String logQuery = "INSERT INTO " + DatabaseConstants.TABLE_EXPENSES + " VALUES (" +
                expense.getDescription() + ", " + expense.getAmount() + ", " +
                expense.getCategory() + ", " + expense.getDate() + ")";
        Log.d("SQL_QUERY", logQuery);
        long result = db.insert(DatabaseConstants.TABLE_EXPENSES, null, values);
        db.close();
        return result;
    }

    /**
     * The method retrieves all expenses sorted from newest to oldest.
     * <p>
     *
     * @return The method returns a list of all expenses
     */
    public List<Expense> getAllExpenses() {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseConstants.TABLE_EXPENSES +
                " ORDER BY " + DatabaseConstants.COLUMN_DATE + " DESC";
        Log.d("SQL_QUERY", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenseList;
    }

    /**
     * The method updates an existing expense in the database.
     * <p>
     *
     * @param expense The expense object with updated values
     * @return The method returns the number of rows affected
     */
    public int updateExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.COLUMN_DESCRIPTION, expense.getDescription());
        values.put(DatabaseConstants.COLUMN_AMOUNT, expense.getAmount());
        values.put(DatabaseConstants.COLUMN_CATEGORY, expense.getCategory());
        values.put(DatabaseConstants.COLUMN_DATE, expense.getDate());
        String logQuery = "UPDATE " + DatabaseConstants.TABLE_EXPENSES + " SET values WHERE " +
                DatabaseConstants.COLUMN_ID + " = " + expense.getId();
        Log.d("SQL_QUERY", logQuery);
        int result = db.update(DatabaseConstants.TABLE_EXPENSES, values,
                DatabaseConstants.COLUMN_ID + " = ?", new String[]{String.valueOf(expense.getId())});
        db.close();
        return result;
    }

    /**
     * The method deletes an expense from the database by id.
     * <p>
     *
     * @param id The ID of the expense to delete
     */
    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String logQuery = "DELETE FROM " + DatabaseConstants.TABLE_EXPENSES + " WHERE " +
                DatabaseConstants.COLUMN_ID + " = " + id;
        Log.d("SQL_QUERY", logQuery);
        db.delete(DatabaseConstants.TABLE_EXPENSES, DatabaseConstants.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * The method searches for an expense based on its description.
     * <p>
     *
     * @param searchString The string to search in the description
     * @return The method returns a list of expenses matching the description
     */
    public List<Expense> searchByDescription(String searchString) {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + DatabaseConstants.TABLE_EXPENSES +
                " WHERE " + DatabaseConstants.COLUMN_DESCRIPTION + " LIKE '%" + searchString + "%'" +
                " ORDER BY " + DatabaseConstants.COLUMN_DATE + " DESC";
        Log.d("SQL_QUERY", searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenseList;
    }

    /**
     * The method filters expenses strictly greater than a specific amount.
     * <p>
     *
     * @param minAmount The minimum amount threshold
     * @return The method returns a list of expenses above the specified amount
     */
    public List<Expense> filterByAmountGreaterThan(double minAmount) {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String filterQuery = "SELECT * FROM " + DatabaseConstants.TABLE_EXPENSES +
                " WHERE " + DatabaseConstants.COLUMN_AMOUNT + " > " + minAmount +
                " ORDER BY " + DatabaseConstants.COLUMN_AMOUNT + " ASC";
        Log.d("SQL_QUERY", filterQuery);
        Cursor cursor = db.rawQuery(filterQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenseList;
    }

    /**
     * The method calculates the total sum of expenses for a given month.
     * <p>
     *
     * @param yearMonth The year and month in YYYY-MM format
     * @return The method returns the total cumulative amount
     */
    public double getMonthlyTotal(String yearMonth) {
        double total = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        String sumQuery = "SELECT SUM(" + DatabaseConstants.COLUMN_AMOUNT + ") FROM " +
                DatabaseConstants.TABLE_EXPENSES + " WHERE " + DatabaseConstants.COLUMN_DATE +
                " LIKE '" + yearMonth + "-%'";
        Log.d("SQL_QUERY", sumQuery);
        Cursor cursor = db.rawQuery(sumQuery, null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }
}