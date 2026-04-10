package com.example.ex11042;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * @author Itay Shehter as8891@bs.amalnet.k12.il
 * @version 1.0
 * @since 26/1/2026
 * The class binds expense data to the RecyclerView for dynamic display.
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private OnItemLongClickListener longClickListener;

    /**
     * @author Itay Shehter as8891@bs.amalnet.k12.il
     * @version 1.0
     * @since 26/1/2026
     * The interface defines the callback for long click events on items.
     */
    public interface OnItemLongClickListener {
        /**
         * The method triggers when an item is long clicked.
         * <p>
         *
         * @param expense The expense object that was clicked
         */
        void onItemLongClick(Expense expense);
    }

    /**
     * The method constructs the adapter.
     * <p>
     *
     * @param expenseList The data set of expenses
     * @param longClickListener The listener for long click events
     */
    public ExpenseAdapter(List<Expense> expenseList, OnItemLongClickListener longClickListener) {
        this.expenseList = expenseList;
        this.longClickListener = longClickListener;
    }

    /**
     * The method inflates the view layout for each item.
     * <p>
     *
     * @param parent The parent view group
     * @param viewType The type of the view
     * @return The method returns a new view holder
     */
    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    /**
     * The method binds the data to the view holder elements.
     * <p>
     *
     * @param holder The view holder
     * @param position The position of the item in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.tvDescription.setText(expense.getDescription());
        holder.tvAmount.setText(String.valueOf(expense.getAmount()) + " ₪");
        holder.tvCategory.setText(expense.getCategory());
        holder.tvDate.setText(expense.getDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onItemLongClick(expense);
                return true;
            }
        });
    }

    /**
     * The method returns the total number of items in the list.
     * <p>
     *
     * @return The method returns the integer count
     */
    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    /**
     * @author Itay Shehter as8891@bs.amalnet.k12.il
     * @version 1.0
     * @since 26/1/2026
     * The class holds the view references for each list item.
     */
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription;
        TextView tvAmount;
        TextView tvCategory;
        TextView tvDate;

        /**
         * The method constructs the view holder.
         * <p>
         *
         * @param itemView The view of the item
         */
        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvItemDescription);
            tvAmount = itemView.findViewById(R.id.tvItemAmount);
            tvCategory = itemView.findViewById(R.id.tvItemCategory);
            tvDate = itemView.findViewById(R.id.tvItemDate);
        }
    }
}