package edu.card.clarity.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.card.clarity.presentation.addCardScreen.AddCardScreen
import edu.card.clarity.presentation.HomeScreen
import edu.card.clarity.presentation.MyBenefitsScreen
import edu.card.clarity.presentation.MyCardsScreen
import edu.card.clarity.presentation.PurchaseScreen
import edu.card.clarity.presentation.UpcomingPaymentsScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavBar.Home.route
    ) {
        composable(BottomNavBar.Home.route) {
            HomeScreen(navController)
        }
        composable(route = BottomNavBar.AddCard.route) {
            AddCardScreen(navController)
        }
        composable(route = BottomNavBar.MyBenefits.route) {
            MyBenefitsScreen()
        }
        composable(route = BottomNavBar.Purchase.route) {
            PurchaseScreen()
        }
        composable("myCards") {
            MyCardsScreen()
        }
        composable("upcomingPayments") {
            UpcomingPaymentsScreen()
        }
    }
}