package com.example.singletoprepro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.singletoprepro.ui.theme.SingleTopReproTheme
import kotlinx.serialization.Serializable

@Serializable private data object A
@Serializable private data object B

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            val backStack = navController.currentBackStack.collectAsState()

            SingleTopReproTheme {
                Scaffold{ paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        NavHost(
                            navController = navController,
                            startDestination = A
                        ) {
                            composable<A> { MyScreen(name = "A") }
                            composable<B> { MyScreen(name = "B") }
                        }

                        MyNavItem(navController, A)
                        MyNavItem(navController, B)
                        MyBackStack(backStack)
                    }
                }
            }
        }
    }
}

@Composable
private fun <T : Any> MyNavItem(navController: NavHostController, route: T) {

    Button(onClick = {
        navController.navigate(route = route)
    }) {
        Text("Navigate ${route::class.simpleName!!} (no options) ")
    }

    Button(onClick = {
        navController.navigate(
            route = route,
            navOptions =  navOptions {
                popUpTo<A>{ saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        )
    }) {
        Text("Navigate ${route::class.simpleName!!} (popUpTo<A>, withSingleTop) ")
    }
}

@Composable
private fun MyBackStack(backStack: State<List<NavBackStackEntry>>) {
    Text("Current back stack")
    backStack.value.forEach {
        Text("Route: ${it.destination.route}")
    }
}

@Composable
private fun MyScreen(name: String) {
    Column {
        Text(name)
    }
}