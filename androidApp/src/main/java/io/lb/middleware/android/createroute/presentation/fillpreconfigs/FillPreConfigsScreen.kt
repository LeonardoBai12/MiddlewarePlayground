package io.lb.middleware.android.createroute.presentation.fillpreconfigs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.android.core.presentation.components.GenericTopAppBar
import io.lb.middleware.android.core.presentation.showToast
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.createroute.fillpreconfigs.FillPreConfigsEvent
import io.lb.middleware.shared.presentation.createroute.fillpreconfigs.FillPreConfigsState
import io.lb.middleware.shared.presentation.createroute.fillpreconfigs.FillPreConfigsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillPreConfigsScreen(
    navController: NavHostController,
    state: FillPreConfigsState,
    eventFlow: CommonFlow<FillPreConfigsViewModel.UiEvent>,
    onEvent: (FillPreConfigsEvent) -> Unit
) {
    val context = LocalContext.current
    val currentBodyFieldIndex = remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = Screens.FILL_ROUTES) {
        eventFlow.collectLatest {
            when (it) {
                is FillPreConfigsViewModel.UiEvent.ShowError -> {
                    context.showToast(it.message)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(navController, "Step 2: Fill New Route Fields")
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = if (state.isLoading) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.primary
                },
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                onClick = {
                    if (state.isLoading.not()) {

                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Step"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // receber todos os old fields
            // dar opcao de colcoar uma nova chave, deixa uma pré estabelecida
            // ou ignorar
            // ir para proxima


            //concatenar

            // adicionar mais campos

            // DropDownMenu dos campos antigos


        }
    }
}
