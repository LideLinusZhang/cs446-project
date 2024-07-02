package edu.card.clarity.presentation.addCardScreen

import android.app.DatePickerDialog
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.card.clarity.presentation.common.DatePickerField
import edu.card.clarity.presentation.common.DropdownMenu
import edu.card.clarity.presentation.common.TextField
import java.util.Calendar

@Composable
fun CardInformationForm(
    navController: NavController,
    viewModel: CardInformationFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val statementDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                viewModel.updateMostRecentStatementDate(
                    year,
                    month,
                    dayOfMonth
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val paymentDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                viewModel.updateMostRecentPaymentDueDate(
                    year,
                    month,
                    dayOfMonth
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            label = "Card Name",
            placeholderText = "Enter Card Name",
            text = uiState.cardName,
            onTextChange = viewModel::updateCardName
        )
        DropdownMenu(
            label = "Card Network",
            options = viewModel.cardNetworkTypeStrings,
            selectedOption = uiState.selectedCardNetworkType,
            onOptionSelected = viewModel::updateSelectedCardNetworkType
        )
        DropdownMenu(
            label = "Reward Type",
            options = viewModel.rewardTypeStrings,
            selectedOption = uiState.selectedRewardType,
            onOptionSelected = viewModel::updateSelectedRewardType
        )
        DatePickerField(
            date = uiState.mostRecentStatementDate,
            label = "Most Recent Statement Date",
            onClick = statementDatePickerDialog::show
        )
        DatePickerField(
            date = uiState.mostRecentPaymentDueDate,
            label = "Most Recent Payment Due Date",
            onClick = paymentDatePickerDialog::show
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Payment Reminder:",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black
            )
            Switch(
                checked = uiState.isReminderEnabled,
                onCheckedChange = viewModel::updateReminderEnabled
            )
        }

        Button(
            onClick = {
                viewModel.addCreditCard()
                navController.navigate("myCards")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
        ) {
            Text(text = "Add Card to My Cards")
        }
    }
}