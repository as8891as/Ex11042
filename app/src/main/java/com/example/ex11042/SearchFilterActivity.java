package com.example.ex11042;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Itay Shehter as8891@bs.amalnet.k12.il
 * @version 1.0
 * @since 23/03/26
 * The activity handles searching expenses by min/max amount, category filter, and sorting.
 */
public class SearchFilterActivity extends AppCompatActivity {

    private EditText etMinPrice, etMaxPrice;
    private Spinner spinnerFilterCategory, spinnerSortBy;
    private Button btnApplyFilter;
    private RecyclerView rvFilteredExpenses;
    private ExpenseDatabaseHelper dbHelper;

    /**
     * The method is called when the activity starts. It configures the UI, spinners, and search logic.
     * <p>
     *
     * @param savedInstanceState The saved state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);

        etMinPrice = findViewById(R.id.etMinPrice);
        etMaxPrice = findViewById(R.id.etMaxPrice);
        spinnerFilterCategory = findViewById(R.id.spinnerFilterCategory);
        spinnerSortBy = findViewById(R.id.spinnerSortBy);
        btnApplyFilter = findViewById(R.id.btnApplyFilter);
        rvFilteredExpenses = findViewById(R.id.rvFilteredExpenses);

        rvFilteredExpenses.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new ExpenseDatabaseHelper(this);

        String[] categories = {"הכל", "אוכל", "בילוי", "תחבורה", "חשבונות", "אחר"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerFilterCategory.setAdapter(catAdapter);

        String[] sortOptions = {"ללא מיון", "מחיר עולה", "מחיר יורד"};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions);
        spinnerSortBy.setAdapter(sortAdapter);

        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applySearchAndFilter();
            }
        });
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
            startActivity(new Intent(this, AddExpenseActivity.class));
            return true;
        } else if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_credits) {
            startActivity(new Intent(this, CreditsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * The method filters and sorts the expenses based on user input and updates the RecyclerView.
     * <p>
     */
    private void applySearchAndFilter() {
        List<Expense> allExpenses = dbHelper.getAllExpenses();
        List<Expense> filteredList = new ArrayList<>();

        String minPriceStr = etMinPrice.getText().toString();
        String maxPriceStr = etMaxPrice.getText().toString();
        String selectedCategory = spinnerFilterCategory.getSelectedItem().toString();
        String selectedSort = spinnerSortBy.getSelectedItem().toString();

        double min = minPriceStr.isEmpty() ? 0 : Double.parseDouble(minPriceStr);
        double max = maxPriceStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceStr);

        for (Expense e : allExpenses) {
            boolean isPriceValid = e.getAmount() >= min && e.getAmount() <= max;
            boolean isCategoryValid = selectedCategory.equals("הכל") || e.getCategory().equals(selectedCategory);

            if (isPriceValid && isCategoryValid) {
                filteredList.add(e);
            }
        }

        if (selectedSort.equals("מחיר עולה")) {
            Collections.sort(filteredList, new Comparator<Expense>() {
                @Override
                public int compare(Expense e1, Expense e2) {
                    return Double.compare(e1.getAmount(), e2.getAmount());
                }
            });
        } else if (selectedSort.equals("מחיר יורד")) {
            Collections.sort(filteredList, new Comparator<Expense>() {
                @Override
                public int compare(Expense e1, Expense e2) {
                    return Double.compare(e2.getAmount(), e1.getAmount());
                }
            });
        }

        ExpenseAdapter adapter = new ExpenseAdapter(filteredList, new ExpenseAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Expense expense) {
            }
        });
        rvFilteredExpenses.setAdapter(adapter);
    }
}