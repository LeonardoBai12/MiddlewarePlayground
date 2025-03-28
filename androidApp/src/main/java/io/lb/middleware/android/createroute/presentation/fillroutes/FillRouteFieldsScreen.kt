package io.lb.middleware.android.createroute.presentation.fillroutes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.android.core.presentation.components.GenericTopAppBar
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.createroute.fill_routes.FillRouteFieldsEvent
import io.lb.middleware.shared.presentation.createroute.fill_routes.FillRouteFieldsState
import io.lb.middleware.shared.presentation.createroute.fill_routes.FillRouteFieldsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillRouteFieldsScreen(
    navController: NavHostController,
    state: FillRouteFieldsState,
    eventFlow: CommonFlow<FillRouteFieldsViewModel.UiEvent>,
    onEvent: (FillRouteFieldsEvent) -> Unit
) {
    LaunchedEffect(key1 = Screens.FILL_ROUTES) {

    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(navController)
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Create Route Screen")
            // receber todos os old fields
            // dar opcao de colcoar uma nova chave, deixa uma pré estabelecida
            // ou ignorar
            // ir para proxima
        }
    }
}
