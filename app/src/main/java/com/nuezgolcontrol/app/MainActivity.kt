package com.nuezgolcontrol.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Dashboard
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
import com.nuezgolcontrol.app.ui.resumen.ResumenFinancieroScreen
import com.nuezgolcontrol.app.ui.resumen.ResumenFinancieroViewModel
import com.nuezgolcontrol.app.ui.resumen.ResumenConFiltrosViewModel
import com.nuezgolcontrol.app.ui.resumen.EstadisticasViewModel
import com.nuezgolcontrol.app.ui.theme.NuezGolTheme
import com.nuezgolcontrol.app.ui.trabajadores.NuevoPagoTrabajadorScreen
import com.nuezgolcontrol.app.ui.trabajadores.TrabajadoresScreen
import com.nuezgolcontrol.app.ui.trabajadores.TrabajadoresViewModel
import com.nuezgolcontrol.app.ui.ventas.NuevaVentaScreen
import com.nuezgolcontrol.app.ui.ventas.VentasScreen
import com.nuezgolcontrol.app.ui.ventas.VentasViewModel
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import kotlinx.coroutines.launch

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
    data object Resumen : TopRoute("resumen", "Resumen", Icons.Default.Dashboard)
    data object Ventas : TopRoute("ventas", "Ventas", Icons.Default.PointOfSale)
    data object Cosechas : TopRoute("cosechas", "Cosechas", Icons.Default.Agriculture)
    data object Trabajadores : TopRoute("trabajadores", "Pagos", Icons.Default.Group)
}

private val topRoutes = listOf(TopRoute.Resumen, TopRoute.Ventas, TopRoute.Cosechas, TopRoute.Trabajadores)

@Composable
fun NuezGolApp(repository: com.nuezgolcontrol.app.data.NuezRepository) {
    val navController = rememberNavController()
    val resumenVm: ResumenFinancieroViewModel = viewModel(factory = ResumenFinancieroViewModel.factory(repository))
    val resumenConFiltrosVm: ResumenConFiltrosViewModel = viewModel(factory = ResumenConFiltrosViewModel.factory(repository))
    val estadisticasVm: EstadisticasViewModel = viewModel(factory = EstadisticasViewModel.factory(repository))
    val ventasVm: VentasViewModel = viewModel(factory = VentasViewModel.factory(repository))
    val cosechasVm: CosechasViewModel = viewModel(factory = CosechasViewModel.factory(repository))
    val trabajadoresVm: TrabajadoresViewModel = viewModel(factory = TrabajadoresViewModel.factory(repository))

    NavHost(
        navController = navController,
        startDestination = "main_tabs",
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
    ) {
        composable("main_tabs") {
            val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = { topRoutes.size })
            val scope = androidx.compose.runtime.rememberCoroutineScope()
            
            Scaffold(
                bottomBar = {
                    NavigationBar(
                        containerColor = Color(0xFF0D0D0D),
                        tonalElevation = 0.dp
                    ) {
                        topRoutes.forEachIndexed { index, item ->
                            val selected = pagerState.targetPage == index
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            ) { padding ->
                androidx.compose.foundation.pager.HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    beyondViewportPageCount = 2
                ) { page ->
                    when (page) {
                        0 -> ResumenFinancieroScreen(
                            viewModel = resumenVm,
                            filtrosViewModel = resumenConFiltrosVm,
                            estadisticasViewModel = estadisticasVm
                        )
                        1 -> VentasScreen(
                            viewModel = ventasVm,
                            onNuevaVenta = { navController.navigate("nueva_venta") }
                        )
                        2 -> CosechasScreen(
                            viewModel = cosechasVm,
                            onNuevaCosecha = { navController.navigate("nueva_cosecha") }
                        )
                        3 -> TrabajadoresScreen(
                            viewModel = trabajadoresVm,
                            onNuevoPago = { navController.navigate("nuevo_pago_trabajador") }
                        )
                    }
                }
            }
        }
        composable("nueva_venta") {
            NuevaVentaScreen(
                viewModel = ventasVm,
                onBack = { navController.popBackStack() }
            )
        }
        composable("nueva_cosecha") {
            NuevaCosechaScreen(
                viewModel = cosechasVm,
                onBack = { navController.popBackStack() }
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
