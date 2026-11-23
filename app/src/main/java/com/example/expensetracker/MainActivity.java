
package com.example.expensetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText etAmount, etDescription;
    Spinner spCategory;
    Button btnAddExpense;

    TextView tvTotalExpenses, tvNoTransactions;
    LinearLayout llTransactions;

    double totalExpenses = 0.0;

    ArrayList<String> categories = new ArrayList<>(
            Arrays.asList(
                    "Select Category",
                    "Food",
                    "Shopping",
                    "Travel",
                    "Education",
                    "Bills",
                    "Others"
            )
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect XML components with Java

        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        spCategory = findViewById(R.id.spCategory);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        tvTotalExpenses = findViewById(R.id.tvTotalExpenses);
        tvNoTransactions = findViewById(R.id.tvNoTransactions);
        llTransactions = findViewById(R.id.llTransactions);

        // Set category dropdown options

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spCategory.setAdapter(adapter);

        // Add Expense button functionality

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpense();
            }
        });
    }

    private void addExpense() {

        String amountText = etAmount.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Validate amount

        if (amountText.isEmpty()) {
            etAmount.setError("Please enter amount");
            etAmount.requestFocus();
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            etAmount.setError("Enter a valid amount");
            return;
        }

        if (Double.isNaN(amount) ||
                Double.isInfinite(amount) ||
                amount <= 0) {
            etAmount.setError("Amount must be greater than zero");
            return;
        }

        // Validate category

        String category = spCategory.getSelectedItem().toString();

        if (spCategory.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    this,
                    "Please select a category",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // Update total expenses

        totalExpenses += amount;

        NumberFormat currencyFormat =
                NumberFormat.getCurrencyInstance(
                        new Locale("en", "IN")
                );

        tvTotalExpenses.setText(
                currencyFormat.format(totalExpenses)
        );

        // Display transaction

        String transactionText = category;

        if (!description.isEmpty()) {
            transactionText += " - " + description;
        }

        transactionText += " : " + currencyFormat.format(amount);

        TextView transaction = new TextView(this);

        transaction.setText(transactionText);
        transaction.setTextSize(16);
        transaction.setPadding(12, 15, 12, 15);

        llTransactions.addView(transaction);

        // Hide empty message

        tvNoTransactions.setVisibility(View.GONE);

        // Clear input fields

        etAmount.setText("");
        etDescription.setText("");
        spCategory.setSelection(0);

        Toast.makeText(
                this,
                "Expense added successfully!",
                Toast.LENGTH_SHORT
        ).show();
    }
}
