package com.example.ex11042;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Itay Shehter as8891@bs.amalnet.k12.il
 * @version 1.0
 * @since 26/1/2026
 * The activity is the main entry point, displaying the dynamic list of expenses, updates in real time, and shows monthly total.
 */
public class MainActivity extends AppCompatActivity implements ExpenseAdapter.OnItemLongClickListener {

    private RecyclerView rvExpensesList;
    private ExpenseAdapter adapter;
    private ExpenseDatabaseHelper dbHelper;
    private List<Expense> currentList;

    /**
     * The method is called when the activity starts. It initializes views and database.
     * <p>
     *
     * @param savedInstanceState The saved state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvExpensesList = findViewById(R.id.rvExpensesList);
        rvExpensesList.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new ExpenseDatabaseHelper(this);
    }

    /**
     * The method is called when the activity resumes. It refreshes the data dynamically.
     * <p>
     */
    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
        showMonthlyTotal();
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

    /**
     * The method retrieves updated data from the database and updates the RecyclerView.
     * <p>
     */
    private void refreshList() {
        currentList = dbHelper.getAllExpenses();
        adapter = new ExpenseAdapter(currentList, this);
        rvExpensesList.setAdapter(adapter);
    }

    /**
     * The method calculates and displays the current month's cumulative expenses via Toast.
     * <p>
     */
    private void showMonthlyTotal() {
        String currentMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
        double total = dbHelper.getMonthlyTotal(currentMonth);
        Toast.makeText(this, "סך הוצאות החודש: " + total + " ₪", Toast.LENGTH_LONG).show();
    }

    /**
     * The method triggers when an item is long clicked, showing a dialog for update or delete.
     * <p>
     *
     * @param expense The expense object clicked
     */
    @Override
    public void onItemLongClick(Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר פעולה");
        builder.setItems(new String[]{"מחק הוצאה"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    dbHelper.deleteExpense(expense.getId());
                    refreshList();
                    showMonthlyTotal();
                }
            }
        });
        builder.show();
    }
}