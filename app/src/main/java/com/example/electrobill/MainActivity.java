package com.example.electrobill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputElectricity, inputRebate;
    private Button btnCalculate, btnClear;
    private TextView tvKwh, tvTotalBill, tvTotalRebate, outputTotalCost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeViews();
        setButtonListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuAbout) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            showToast("About clicked");
            return true;
        } else if (selected == R.id.menuInstructions) {
            startActivity(new Intent(MainActivity.this, InstructionsActivity.class));
            showToast("Instructions clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeViews() {
        inputElectricity = findViewById(R.id.inputElectricity);
        inputRebate = findViewById(R.id.inputRebate);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);
        tvKwh = findViewById(R.id.tvKwh);
        tvTotalBill = findViewById(R.id.tvTotalBill);
        tvTotalRebate = findViewById(R.id.tvTotalRebate);
        outputTotalCost = findViewById(R.id.outputTotalCost);
    }


    private void setButtonListeners() {
        btnCalculate.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnCalculate) {
            handleCalculate();
        } else if (id == R.id.btnClear) {
            handleClear();
        }
    }

    private void handleCalculate() {
        String electricityInput = inputElectricity.getText().toString().trim();
        String rebateInput = inputRebate.getText().toString().trim();

        if (isInputValid(electricityInput, rebateInput)) {
            double electricity = Double.parseDouble(electricityInput);
            double rebateValue = Double.parseDouble(rebateInput);

            if (isRebateValid(rebateValue)) {
                double electricityBill = calculateElectricityBill(electricity);
                double finalCost = applyRebate(electricityBill, rebateValue);

                displayResults(electricityBill, finalCost);
            }
        }
    }

    private void handleClear() {
        inputElectricity.setText("");
        inputRebate.setText("");
        tvTotalBill.setText("Total Bill: ");
        tvTotalRebate.setText("Rebate Applied: "); // Reset rebate field
        outputTotalCost.setText("Final Cost After Rebate: ");
        inputElectricity.requestFocus();
        showToast("Fields cleared successfully");
    }


    private boolean isInputValid(String electricity, String rebate) {
        if (TextUtils.isEmpty(electricity) || TextUtils.isEmpty(rebate)) {
            showToast("Please fill in both electricity and rebate fields");
            return false;
        }
        try {
            Double.parseDouble(electricity);
            Double.parseDouble(rebate);
        } catch (NumberFormatException e) {
            showToast("Please enter valid numeric values");
            return false;
        }
        return true;
    }

    private boolean isRebateValid(double rebateValue) {
        if (rebateValue < 1 || rebateValue > 5) {
            showToast("Rebate must be between 0% and 5%");
            return false;
        }
        return true;
    }

    private double calculateElectricityBill(double electricity) {
        double totalCharge = 0.0;

        if (electricity <= 200) {
            totalCharge = electricity * 21.8;
        } else if (electricity <= 300) {
            totalCharge = (200 * 21.8) + ((electricity - 200) * 33.4);
        } else if (electricity <= 600) {
            totalCharge = (200 * 21.8) + (100 * 33.4) + ((electricity - 300) * 51.6);
        } else if (electricity <= 900) {
            totalCharge = (200 * 21.8) + (100 * 33.4) + (300 * 51.6) + ((electricity - 600) * 54.6);
        } else {
            totalCharge = (200 * 21.8) + (100 * 33.4) + (300 * 51.6) + (300 * 54.6) + ((electricity - 900) * 57.1);
        }

        return totalCharge;
    }

    private double applyRebate(double totalCharge, double rebate) {
        return totalCharge - (totalCharge * rebate / 100.0);
    }

    private void displayResults(double electricityBill, double finalCost) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        String formattedElectricityBill = decimalFormat.format(electricityBill);
        String formattedFinalCost = decimalFormat.format(finalCost);
        double rebateAmount = electricityBill - finalCost; // Calculate the rebate amount
        String formattedRebateAmount = decimalFormat.format(rebateAmount);

        tvTotalBill.setText(getString(R.string.total_bill_output, formattedElectricityBill));
        tvTotalRebate.setText(getString(R.string.total_rebate_output, formattedRebateAmount)); // Display rebate
        outputTotalCost.setText(getString(R.string.final_cost_output, formattedFinalCost));

        showToast("Calculation successful. Final cost: " + formattedFinalCost);
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}