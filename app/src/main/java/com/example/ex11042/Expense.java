package com.example.ex11042;

/**
 * @author Itay Shehter as8891@bs.amalnet.k12.il
 * @version 1.0
 * @since 26/03/2026
 * The class represents a single expense entity with all required fields and setter methods for updates.
 */
public class Expense {
    private int id;
    private String description;
    private double amount;
    private String category;
    private String date;

    /**
     * The method constructs a new Expense object without an id.
     * <p>
     *
     * @param description The description of the expense.
     * @param amount The cost amount of the expense.
     * @param category The category of the expense.
     * @param date The date of the expense in YYYY-MM-DD format.
     */
    public Expense(String description, double amount, String category, String date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    /**
     * The method constructs a new Expense object with an id.
     * <p>
     *
     * @param id The primary key id.
     * @param description The description of the expense.
     * @param amount The cost amount of the expense.
     * @param category The category of the expense.
     * @param date The date of the expense in YYYY-MM-DD format.
     */
    public Expense(int id, String description, double amount, String category, String date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    /**
     * The method gets the id of the expense.
     * <p>
     *
     * @return The integer id.
     */
    public int getId() {
        return id;
    }

    /**
     * The method sets the id of the expense.
     * <p>
     *
     * @param id The new id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * The method gets the description of the expense.
     * <p>
     *
     * @return The string description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * The method sets the description of the expense.
     * <p>
     *
     * @param description The new description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The method gets the amount of the expense.
     * <p>
     *
     * @return The double amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * The method sets the amount of the expense.
     * <p>
     *
     * @param amount The new amount to set.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * The method gets the category of the expense.
     * <p>
     *
     * @return The string category.
     */
    public String getCategory() {
        return category;
    }

    /**
     * The method sets the category of the expense.
     * <p>
     *
     * @param category The new category to set.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * The method gets the date of the expense.
     * <p>
     *
     * @return The string date.
     */
    public String getDate() {
        return date;
    }

    /**
     * The method sets the date of the expense.
     * <p>
     *
     * @param date The new date to set.
     */
    public void setDate(String date) {
        this.date = date;
    }
}