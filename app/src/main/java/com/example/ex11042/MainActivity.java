package com.example.ex11042;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.OnItemLongClickListener {

    private RecyclerView rvExpensesList;
    private TextView tvEmptyStateMain;
    private ExpenseAdapter adapter;
    private ExpenseDatabaseHelper dbHelper;
    private List<Expense> currentList;
    private String tempEditDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvExpensesList = findViewById(R.id.rvExpensesList);
        tvEmptyStateMain = findViewById(R.id.tvEmptyStateMain);

        rvExpensesList.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new ExpenseDatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
        showMonthlyTotal();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_main) {
            return true;
        } else if (id == R.id.action_add) {
            startActivity(new Intent(this, AddExpenseActivity.class));
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

    private void refreshList() {
        currentList = dbHelper.getAllExpenses();

        if (currentList.isEmpty()) {
            tvEmptyStateMain.setVisibility(View.VISIBLE);
            rvExpensesList.setVisibility(View.GONE);
        } else {
            tvEmptyStateMain.setVisibility(View.GONE);
            rvExpensesList.setVisibility(View.VISIBLE);
            adapter = new ExpenseAdapter(currentList, this);
            rvExpensesList.setAdapter(adapter);
        }
    }

    private void showMonthlyTotal() {
        String currentMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
        double total = dbHelper.getMonthlyTotal(currentMonth);
        Toast.makeText(this, "סך הוצאות החודש: " + total + " ₪", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemLongClick(final Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר פעולה");
        builder.setItems(new String[]{"ערוך הוצאה", "מחק הוצאה"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showEditDialog(expense);
                } else if (which == 1) {
                    dbHelper.deleteExpense(expense.getId());
                    refreshList();
                    showMonthlyTotal();
                }
            }
        });
        builder.show();
    }

    private void showEditDialog(final Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ערוך הוצאה");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etDesc = new EditText(this);
        etDesc.setHint("תיאור");
        etDesc.setText(expense.getDescription());
        layout.addView(etDesc);

        final EditText etAmount = new EditText(this);
        etAmount.setHint("סכום");
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etAmount.setText(String.valueOf(expense.getAmount()));
        layout.addView(etAmount);

        final Spinner spinnerCategory = new Spinner(this);
        String[] categories = {"אוכל", "בילוי", "תחבורה", "חשבונות", "אחר"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(catAdapter);

        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(expense.getCategory())) {
                spinnerCategory.setSelection(i);
                break;
            }
        }
        layout.addView(spinnerCategory);

        tempEditDate = expense.getDate();
        final Button btnChangeDate = new Button(this);
        btnChangeDate.setText("תאריך: " + tempEditDate);
        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                String[] dateParts = tempEditDate.split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]) - 1;
                int day = Integer.parseInt(dateParts[2]);

                DatePickerDialog datePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int d) {
                        tempEditDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", y, m + 1, d);
                        btnChangeDate.setText("תאריך: " + tempEditDate);
                    }
                }, year, month, day);
                datePicker.show();
            }
        });
        layout.addView(btnChangeDate);

        builder.setView(layout);
        builder.setPositiveButton("שמור", null);
        builder.setNegativeButton("ביטול", null);

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDesc = etDesc.getText().toString().trim();
                String newAmountStr = etAmount.getText().toString().trim();
                String newCategory = spinnerCategory.getSelectedItem().toString();

                if (newDesc.isEmpty() || newAmountStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "נא למלא את כל השדות", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newAmountStr.length() > 12) {
                    Toast.makeText(MainActivity.this, "הסכום שהוזן גדול מדי", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newAmountStr.equals(".") || !newAmountStr.matches("^[0-9]*\\.?[0-9]*$")) {
                    Toast.makeText(MainActivity.this, "אנא הזן סכום תקין", Toast.LENGTH_SHORT).show();
                    return;
                }

                double newAmount = Double.parseDouble(newAmountStr);

                if (newAmount <= 0) {
                    Toast.makeText(MainActivity.this, "הסכום חייב להיות גדול מ-0", Toast.LENGTH_SHORT).show();
                    return;
                }

                expense.setDescription(newDesc);
                expense.setAmount(newAmount);
                expense.setCategory(newCategory);
                expense.setDate(tempEditDate);

                dbHelper.updateExpense(expense);
                refreshList();
                showMonthlyTotal();
                Toast.makeText(MainActivity.this, "ההוצאה עודכנה בהצלחה", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
    }
}