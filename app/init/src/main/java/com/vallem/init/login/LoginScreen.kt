package com.vallem.init.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult.Companion.ACTION_INTENT_SENDER_REQUEST
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult.Companion.EXTRA_SEND_INTENT_EXCEPTION
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vallem.componentlibrary.ui.brand.SylphLogo
import com.vallem.componentlibrary.ui.button.SylphButton
import com.vallem.componentlibrary.ui.button.SylphTextButton
import com.vallem.componentlibrary.ui.classes.forIcons
import com.vallem.componentlibrary.ui.classes.pairWith
import com.vallem.componentlibrary.ui.input.SylphTextField
import com.vallem.componentlibrary.ui.input.SylphTextFieldState
import com.vallem.componentlibrary.ui.input.errorIf
import com.vallem.componentlibrary.ui.theme.ColorSystemBars
import com.vallem.init.components.GoogleSignInButton
import com.vallem.init.destinations.RegisterScreenDestination
import com.vallem.sylph.home.presentation.destinations.HomeScreenDestination
import com.vallem.sylph.shared.Routes
import com.vallem.sylph.shared.auth.FakeGoogleSignInClient
import com.vallem.sylph.shared.auth.GoogleSignInClient
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.extensions.launchCatching
import com.vallem.sylph.shared.util.showToastMessage
import kotlinx.coroutines.launch

@Destination(route = Routes.Screen.Login)
@Composable
fun LoginScreen(navigator: DestinationsNavigator, viewModel: LoginViewModel = hiltViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val formState = with(viewModel) {
        remember(email, validEmail, password, validPassword, validInput) {
            LoginFormState(
                email = email,
                validEmail = validEmail,
                password = password,
                validPassword = validPassword,
                validInput = validInput,
                hasGoogleFunctionality = hasGoogleFunctionality
            )
        }
    }

    ColorSystemBars()

    LaunchedEffect(viewModel.loginResult) {
        when (val result = viewModel.loginResult) {
            is Result.Success -> navigator.navigate(HomeScreenDestination)

            is Result.Failure -> snackbarHostState.showSnackbar(
                message = when (result.e) {
                    is FirebaseAuthInvalidCredentialsException -> "Invalid password"
                    is FirebaseAuthInvalidUserException -> "No user found for provided email"

                    else -> result.e.message.toString()
                },
            )

            else -> Unit
        }
    }

    LoginScreenContent(
        loginResult = viewModel.loginResult,
        state = formState,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        goToRegisterScreen = { navigator.navigate(RegisterScreenDestination) },
        googleSignInClient = viewModel.googleSignInClient,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LoginScreenContent(
    loginResult: Result<FirebaseUser>?,
    state: LoginFormState,
    onEvent: (LoginEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    googleSignInClient: GoogleSignInClient,
    goToRegisterScreen: () -> Unit,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val (emailIcon, passwordIcon) = MaterialTheme.colorScheme.run {
        onSurfaceVariant pairWith primary
    }.forIcons(Icons.Outlined.Person, Icons.Outlined.Email, Icons.Outlined.Lock)

    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(StartIntentSenderForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            if (result.data?.action == ACTION_INTENT_SENDER_REQUEST) {
                val exception = result.data?.getSerializableExtra(EXTRA_SEND_INTENT_EXCEPTION) as? Exception

                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = exception?.localizedMessage ?: "Erro ao entrar com a conta Google",
                        duration = SnackbarDuration.Short,
                    )
                }
            }

            return@rememberLauncherForActivityResult
        }

        runCatching {
            result.data?.let {
                val credentials = googleSignInClient.getCredentialsFromIntent(it)
                onEvent(LoginEvent.SignIn.WithGoogle(credentials))
            }
        }
    }

    var googleLoading by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) { pv ->
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(pv)
                .padding(16.dp)
        ) {
            SylphLogo()

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SylphTextField.SingleLine(
                    value = state.email,
                    onValueChange = { onEvent(LoginEvent.Update.Email(it)) },
                    placeholder = "Email",
                    leadingIcon = emailIcon,
                    state = SylphTextFieldState.errorIf(!state.validEmail),
                    helperText = "Email inválido".takeIf { !state.validEmail },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                )

                SylphTextField.Password(
                    value = state.password,
                    onValueChange = { onEvent(LoginEvent.Update.Password(it)) },
                    placeholder = "Password",
                    leadingIcon = passwordIcon,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(
                        onGo = { onEvent(LoginEvent.SignIn.WithCurrentData) },
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SylphButton.Pill(
                    label = "Login",
                    enabled = state.validInput,
                    isLoading = loginResult == Result.Loading,
                    onClick = {
                        onEvent(LoginEvent.SignIn.WithCurrentData)
                        keyboardController?.hide()
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(text = "Ainda não tem uma conta?")

                    FlowRow(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SylphTextButton(
                            label = "Criar conta",
                            onClick = goToRegisterScreen,
                        )

                        if (state.hasGoogleFunctionality) GoogleSignInButton(
                            isLoading = googleLoading,
                            onClick = {
                                scope.launchCatching(
                                    onError = {
                                        googleLoading = false
                                        context.showToastMessage(
                                            text = it.localizedMessage ?: "Erro desconhecido",
                                        )
                                    }
                                ) {
                                    googleLoading = true
                                    val res = googleSignInClient.getGoogleSignInResult()
                                    val intentSenderRequest = IntentSenderRequest
                                        .Builder(res.pendingIntent.intentSender)
                                        .build()

                                    googleLoading = false
                                    launcher.launch(intentSenderRequest)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreenContent(
        loginResult = null,
        state = LoginFormState("", false, "", false, false, true),
        onEvent = {},
        snackbarHostState = remember { SnackbarHostState() },
        goToRegisterScreen = {},
        googleSignInClient = FakeGoogleSignInClient(),
    )
}