package com.example.ex11042;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

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

    public ExpenseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

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

    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "Deleting expense with ID: " + id);
        db.delete(TABLE_EXPENSES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

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