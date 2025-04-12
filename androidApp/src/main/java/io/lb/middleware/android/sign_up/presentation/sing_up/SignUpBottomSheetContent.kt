package io.lb.middleware.android.sign_up.presentation.sing_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.lb.middleware.android.R
import io.lb.middleware.android.core.presentation.components.DefaultTextButton
import io.lb.middleware.android.core.presentation.components.DefaultTextField

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun SignUpBottomSheetContent(
    onSignInWithEmailAndPassword: (String, String, String, String, String) -> Unit,
    isLoading: Boolean,
) {
    val email = remember {
        mutableStateOf("")
    }
    val phone = remember {
        mutableStateOf("")
    }
    val name = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val repeatedPassword = remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7F),
        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SignInTextFields(
                email = email,
                name = name,
                phone = phone,
                password = password,
                repeatedPassword = repeatedPassword,
                isLoading = isLoading
            )

            DefaultTextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 72.dp, vertical = 16.dp),
                text = if (isLoading) {
                    stringResource(id = R.string.signing_up)
                } else {
                    stringResource(id = R.string.sign_up)
                },
                enabled = isLoading.not(),
                onClick = {
                    keyboardController?.hide()
                    onSignInWithEmailAndPassword(
                        name.value,
                        email.value,
                        phone.value,
                        password.value,
                        repeatedPassword.value,
                    )
                },
            )
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun SignInTextFields(
    email: MutableState<String>,
    phone: MutableState<String>,
    name: MutableState<String>,
    password: MutableState<String>,
    repeatedPassword: MutableState<String>,
    isLoading: Boolean
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultTextField(
            modifier = Modifier.padding(vertical = 8.dp),
            text = name.value,
            isEnabled = isLoading.not(),
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Person"
                )
            },
            label = "User Name",
            onValueChange = {
                name.value = it
            }
        )

        DefaultTextField(
            modifier = Modifier.padding(vertical = 8.dp),
            text = email.value,
            isEnabled = isLoading.not(),
            icon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email"
                )
            },
            label = "Email",
            onValueChange = {
                email.value = it
            }
        )

        DefaultTextField(
            modifier = Modifier.padding(vertical = 8.dp),
            text = phone.value,
            isEnabled = isLoading.not(),
            keyboardType = KeyboardType.Phone,
            icon = {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = "Phone"
                )
            },
            label = "Phone",
            onValueChange = {
                phone.value = it
            }
        )

        DefaultTextField(
            modifier = Modifier.padding(vertical = 8.dp),
            text = password.value,
            isEnabled = isLoading.not(),
            isPassword = true,
            icon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "LockIcon"
                )
            },
            label = stringResource(id = R.string.password),
            keyboardType = KeyboardType.Password,
            onValueChange = {
                password.value = it
            }
        )

        DefaultTextField(
            modifier = Modifier.padding(vertical = 8.dp),
            text = repeatedPassword.value,
            isEnabled = isLoading.not(),
            isPassword = true,
            icon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "LockIcon"
                )
            },
            label = stringResource(id = R.string.repeat_password),
            keyboardType = KeyboardType.Password,
            onValueChange = {
                repeatedPassword.value = it
            }
        )
    }
}
