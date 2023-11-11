package com.example.hydro_chingying

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hydro_chingying.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalculate.setOnClickListener {
            calculateBill()
        }

        binding.btnReset.setOnClickListener {
            resetForm()
        }
    }

    private fun calculateBill() {
        val morningUsage = binding.etMorningUsage.text.toString().toDoubleOrNull() ?: 0.0
        val eveningUsage = binding.etEveningUsage.text.toString().toDoubleOrNull() ?: 0.0
        val isRenewableEnergy = binding.switchRenewable.isChecked

        if (morningUsage == 0.0 || eveningUsage == 0.0) {
            showError("Please enter both morning and evening usage.")
            return
        }

        val morningCost = morningUsage * 0.132
        val eveningCost = eveningUsage * 0.094
        val totalUsageCost = morningCost + eveningCost

        val environmentalRebate = if (isRenewableEnergy) -totalUsageCost * 0.09 else 0.0

        val subtotal = totalUsageCost + environmentalRebate
        val salesTax = subtotal * 0.13
        val totalAmount = subtotal + salesTax

        showReceipt(morningCost, eveningCost, totalUsageCost, environmentalRebate, subtotal, salesTax, totalAmount)
    }

    private fun resetForm() {
        binding.etMorningUsage.text.clear()
        binding.etEveningUsage.text.clear()
        binding.switchRenewable.isChecked = false
        binding.tvReceipt.text = ""
    }

    private fun showReceipt(
        morningCost: Double,
        eveningCost: Double,
        totalUsageCost: Double,
        environmentalRebate: Double,
        subtotal: Double,
        salesTax: Double,
        totalAmount: Double
    ) {
        val receiptText = """
            Morning Usage Cost: $$morningCost
            Evening Usage Cost: $$eveningCost
            Total Usage Cost: $$totalUsageCost
            Environmental Rebate: $$environmentalRebate
            Subtotal: $$subtotal
            Sales Tax: $$salesTax
            YOU MUST PAY: $$totalAmount
        """.trimIndent()

        binding.tvReceipt.text = receiptText
    }

    private fun showError(message: String) {
        val snackbar = Snackbar.make(binding.rootLayout, "ERROR: $message", Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}
