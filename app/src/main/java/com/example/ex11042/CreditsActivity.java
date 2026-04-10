package com.example.ex11042;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Itay Shehter as8891@bs.amalnet.k12.il
 * @version 1.0
 * @since 26/1/2026
 * The activity displays the developer credits and version information.
 */
public class CreditsActivity extends AppCompatActivity {

    /**
     * The method is called when the activity starts. It sets the layout view.
     * <p>
     *
     * @param savedInstanceState The saved state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
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
            startActivity(new Intent(this, SearchFilterActivity.class));
            return true;
        } else if (id == R.id.action_credits) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}