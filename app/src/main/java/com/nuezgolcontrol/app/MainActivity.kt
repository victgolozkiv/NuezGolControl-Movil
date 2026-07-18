package com.nuezgolcontrol.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nuezgolcontrol.app.ui.cosechas.CosechasScreen
import com.nuezgolcontrol.app.ui.cosechas.CosechasViewModel
import com.nuezgolcontrol.app.ui.cosechas.NuevaCosechaScreen
import com.nuezgolcontrol.app.ui.theme.NuezGolTheme
import com.nuezgolcontrol.app.ui.trabajadores.NuevoPagoTrabajadorScreen
import com.nuezgolcontrol.app.ui.trabajadores.TrabajadoresScreen
import com.nuezgolcontrol.app.ui.trabajadores.TrabajadoresViewModel
import com.nuezgolcontrol.app.ui.ventas.NuevaVentaScreen
import com.nuezgolcontrol.app.ui.ventas.VentasScreen
import com.nuezgolcontrol.app.ui.ventas.VentasViewModel
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as NuezGolApplication
        setContent {
            NuezGolTheme {
                NuezGolApp(repository = app.repository)
            }
        }
    }
}

private sealed class TopRoute(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Ventas : TopRoute("ventas", "Ventas", Icons.Default.PointOfSale)
    data object Cosechas : TopRoute("cosechas", "Cosechas", Icons.Default.Agriculture)
    data object Trabajadores : TopRoute("trabajadores", "Pagos", Icons.Default.Group)
}

private val topRoutes = listOf(TopRoute.Ventas, TopRoute.Cosechas, TopRoute.Trabajadores)

@Composable
fun NuezGolApp(repository: com.nuezgolcontrol.app.data.NuezRepository) {
    val navController = rememberNavController()
    val ventasVm: VentasViewModel = viewModel(factory = VentasViewModel.factory(repository))
    val cosechasVm: CosechasViewModel = viewModel(factory = CosechasViewModel.factory(repository))
    val trabajadoresVm: TrabajadoresViewModel = viewModel(factory = TrabajadoresViewModel.factory(repository))
    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination
    val showBottomBar = currentDestination?.route in topRoutes.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    topRoutes.forEach { item ->
                        val selected = currentDestination
                            ?.hierarchy
                            ?.any { it.route == item.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = TopRoute.Ventas.route,
            modifier = Modifier.padding(padding),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
        ) {
            composable(TopRoute.Ventas.route) {
                VentasScreen(
                    viewModel = ventasVm,
                    onNuevaVenta = { navController.navigate("nueva_venta") }
                )
            }
            composable("nueva_venta") {
                NuevaVentaScreen(
                    viewModel = ventasVm,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(TopRoute.Cosechas.route) {
                CosechasScreen(
                    viewModel = cosechasVm,
                    onNuevaCosecha = { navController.navigate("nueva_cosecha") }
                )
            }
            composable("nueva_cosecha") {
                NuevaCosechaScreen(
                    viewModel = cosechasVm,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(TopRoute.Trabajadores.route) {
                TrabajadoresScreen(
                    viewModel = trabajadoresVm,
                    onNuevoPago = { navController.navigate("nuevo_pago_trabajador") }
                )
            }
            composable("nuevo_pago_trabajador") {
                NuevoPagoTrabajadorScreen(
                    viewModel = trabajadoresVm,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
