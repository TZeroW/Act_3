package com.example.act3.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.act3.ui.EventViewModel
import com.example.act3.ui.screens.AddEventScreen
import com.example.act3.ui.screens.EventListScreen

sealed class Screen(val route: String) {
    object EventList : Screen("event_list")
    object AddEvent : Screen("add_event?eventId={eventId}") {
        fun createRoute(eventId: String? = null): String {
            return if (eventId != null) "add_event?eventId=$eventId" else "add_event"
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    eventViewModel: EventViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.EventList.route
    ) {
        composable(Screen.EventList.route) {
            EventListScreen(
                viewModel = eventViewModel,
                onNavigateToAddEvent = {
                    navController.navigate(Screen.AddEvent.createRoute())
                },
                onEditEvent = { event ->
                    navController.navigate(Screen.AddEvent.createRoute(event.id))
                }
            )
        }

        composable(
            route = Screen.AddEvent.route,
            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            AddEventScreen(
                viewModel = eventViewModel,
                eventId = eventId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
