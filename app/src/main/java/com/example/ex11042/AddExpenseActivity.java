package com.example.ex11042;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Itay Shehter as8891@bs.amalnet.k12.il
 * @version 1.0
 * @since 23/03/26
 * The activity provides a form to input and save a new expense into the database using a DatePickerDialog.
 */
public class AddExpenseActivity extends AppCompatActivity {

    private EditText etDescription, etAmount;
    private Spinner spinnerCategory;
    private Button btnSelectDate, btnSaveExpense;
    private ExpenseDatabaseHelper dbHelper;
    private String selectedDate;

    /**
     * The method is called when the activity starts. It sets up the UI, date picker dialog, and listeners.
     * <p>
     *
     * @param savedInstanceState The saved state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etDescription = findViewById(R.id.etDescription);
        etAmount = findViewById(R.id.etAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);

        dbHelper = new ExpenseDatabaseHelper(this);
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        btnSelectDate.setText(selectedDate);

        String[] categories = {"אוכל", "בילוי", "תחבורה", "חשבונות", "אחר"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnSaveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    /**
     * The method displays a date picker dialog to allow the user to select a date.
     * <p>
     */
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddExpenseActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                        btnSelectDate.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    /**
     * The method is called when a menu is created. It builds the menu based on the resources.
     * <p>
     *
     * @param menu The menu being created
     * @return The method returns whether the creation of the menu was successful
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * The method handles menu item selections.
     * <p>
     *
     * @param item The menu item selected
     * @return The method returns true if handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_main) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.action_add) {
            return true;
        } else if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchFilterActivity.class));
            return true;
        } else if (id == R.id.action_credits) {
            startActivity(new Intent(this, CreditsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * The method validates input fields and saves the expense to the database.
     * <p>
     */
    private void saveData() {
        String desc = etDescription.getText().toString();
        String amountStr = etAmount.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();

        if (desc.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "נא למלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        Expense expense = new Expense(desc, amount, category, selectedDate);
        dbHelper.addExpense(expense);

        Toast.makeText(this, "הוצאה נשמרה בהצלחה", Toast.LENGTH_SHORT).show();
        finish();
    }
}