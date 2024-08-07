package edu.card.clarity.presentation.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import edu.card.clarity.presentation.common.CardBox
import edu.card.clarity.ui.theme.CardClarityTypography
import edu.card.clarity.ui.theme.DarkAccentBlue
import edu.card.clarity.ui.theme.LightBlue
import java.util.Locale


@Composable
fun HomeScreen(navController: NavController,
               viewModel: HomeScreenViewModel = hiltViewModel(), ) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        RewardsSummary(months = uiState.selectedMonths, userName = "Sophie Chan", rewardsSummary = uiState.rewardsSummary)

        LabelSelectorBar(
            labelItems = listOf("1 Month", "3 Month", "6 Month"),
            onLabelSelected = { months : Int ->
                viewModel.setSelectedMonths(months)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 48.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardBox(label = "My Cards") {
                navController.navigate("myCards")
            }
            CardBox(label = "Upcoming Payments") {
                navController.navigate("upcomingPayments")
            }
        }
    }
}


@Composable
fun RewardsSummary(months: Int, userName: String, rewardsSummary: List<RewardsSummaryItem>) {
    val maxData = rewardsSummary.maxOfOrNull { it.amount } ?: 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 12.dp),
            text = "Hi $userName 👋,",
            fontWeight = FontWeight.Bold,
            style = CardClarityTypography.titleLarge,
            color = Color.Black
        )
        Text(
            text = "Your rewards summary.",
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.Black
        )
    }

    if (months == 1) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.45f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (rewardsSummary.isNotEmpty()) {
                    String.format(Locale.CANADA, "+$%.2f", rewardsSummary[0].amount)
                } else {
                    "+$0.00"
                },
                fontWeight = FontWeight.Normal,
                fontSize = 80.sp,
                color = Color.Black
            )
            Text(
                text = "in savings this month!",
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    } else {
        if (rewardsSummary.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.45f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 250.dp, max = 324.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    rewardsSummary.forEach { item ->
                        Bar(month = item.month, value = item.amount, maxValue = maxData.toFloat(), totalBars = months)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.45f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No rewards data available",
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(LightBlue, shape = CircleShape)
                ) {
                    Text(
                        text = "💡",
                        fontSize = 48.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Start making purchases to see your rewards here!",
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }
    }
}

@Composable
fun Bar(
    month: String,
    value: Float,
    maxValue: Float,
    totalBars: Int
) {

    val barHeightFraction = 0.25 + (value / maxValue) * 0.75

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxHeight()
            .onGloballyPositioned {
            }
    ) {
        Box(
            modifier = Modifier
                .width(if (totalBars == 3) 100.dp else 50.dp)
                .fillMaxHeight(fraction = barHeightFraction.toFloat())
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format(Locale.CANADA, "+$%.2f", value),
                    fontSize = if (totalBars == 3) 16.sp else 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = if (totalBars == 3) 8.dp else 4.dp)
                )
                Text(
                    text = month,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(if (totalBars == 3) 4.dp else 2.dp))
    }
}

@Composable
fun LabelUi(
    text: String = "",
    selected: Boolean = false,
    labelTextStyle: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    backgroundColor: Color = Color.White,
    selectedBackgroundColor: Color = Color.Black,
    textColor: Color = Color.Black,
    selectedTextColor: Color = Color.White,
    roundedCornerShapeSize: Dp = 8.dp,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 8.dp,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(
                if (selected) selectedBackgroundColor else backgroundColor,
                RoundedCornerShape(roundedCornerShapeSize)
            )
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        Text(
            text = text,
            color = if (selected) selectedTextColor else textColor,
            style = labelTextStyle
        )
    }
}

@Composable
fun LabelSelectorBar(
    labelItems: List<String> = listOf(),
    barHeight: Dp = 56.dp,
    horizontalPadding: Dp = 8.dp,
    distanceBetweenItems: Dp = 18.dp,
    labelTextStyle: TextStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    backgroundColor: Color = Color.White,
    selectedBackgroundColor: Color = Color.LightGray,
    textColor: Color = Color.DarkGray,
    selectedTextColor: Color = Color.DarkGray,
    roundedCornerShapeSize: Dp = 8.dp,
    labelHorizontalPadding: Dp = 16.dp,
    labelVerticalPadding: Dp = 8.dp,
    onLabelSelected: (Int) -> Unit
) {
    val selectedLabel = rememberSaveable { mutableStateOf(labelItems.firstOrNull() ?: "") }
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(barHeight)
    ) {
        item { Spacer(modifier = Modifier.width(horizontalPadding)) }
        items(labelItems) { label ->
            LabelUi(
                text = label,
                selected = label == selectedLabel.value,
                labelTextStyle = labelTextStyle,
                backgroundColor = backgroundColor,
                selectedBackgroundColor = selectedBackgroundColor,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                roundedCornerShapeSize = roundedCornerShapeSize,
                horizontalPadding = labelHorizontalPadding,
                verticalPadding = labelVerticalPadding
            ) {
                selectedLabel.value = label
                onLabelSelected(label.removeSuffix(" Month").toInt())
            }
            Spacer(modifier = Modifier.width(distanceBetweenItems))
        }
        item { Spacer(modifier = Modifier.width(horizontalPadding)) }
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}
