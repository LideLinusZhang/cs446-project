package edu.card.clarity.presentation.addCardScreen

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.card.clarity.presentation.common.CustomButton
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
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .semantics { contentDescription = "custom_card_info_form" },
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
            options = viewModel.rewardTypeOptionStrings,
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

        if (uiState.selectedRewardType == "Point Back") {
            TextField(
                label = "Point System Name",
                placeholderText = "Enter Point System Name",
                text = uiState.pointSystemName,
                onTextChange = viewModel::updatePointSystemName
            )

            TextField(
                label = "Point to Cash Conversion Rate",
                placeholderText = "Enter Conversion Rate (e.g., 0.01)",
                text = uiState.pointToCashConversionRate,
                onTextChange = viewModel::updatePointToCashConversionRate,
            )
        }

        CustomButton(
            text = "Add Card to My Cards",
            onClick = {
                viewModel.addCreditCard()
                navController.navigate("myCards")
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}
