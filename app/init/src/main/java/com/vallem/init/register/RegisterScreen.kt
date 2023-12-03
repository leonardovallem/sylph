package com.vallem.init.register

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.vallem.componentlibrary.ui.appbar.SylphTopBar
import com.vallem.componentlibrary.ui.button.SylphButton
import com.vallem.componentlibrary.ui.button.SylphButtonDefaults
import com.vallem.componentlibrary.ui.classes.forIcons
import com.vallem.componentlibrary.ui.classes.pairWith
import com.vallem.componentlibrary.ui.input.SylphTextField
import com.vallem.componentlibrary.ui.input.SylphTextFieldState
import com.vallem.componentlibrary.ui.input.errorIf
import com.vallem.componentlibrary.ui.theme.ColorSystemBars
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.TransFlagColors
import com.vallem.componentlibrary.util.ValidationRule
import com.vallem.init.destinations.OnboardingScreenDestination
import com.vallem.sylph.shared.Routes
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.presentation.components.ImageCropper
import com.vallem.sylph.shared.presentation.components.getOrNull
import com.vallem.sylph.shared.util.pickImage
import com.vallem.sylph.shared.util.resized
import kotlinx.coroutines.launch

@Destination(route = Routes.Screen.Register)
@Composable
fun RegisterScreen(
    navigator: DestinationsNavigator,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val state = with(viewModel) {
        remember(
            name,
            validName,
            email,
            validEmail,
            password,
            validPassword,
            passwordConfirmation,
            validPasswordConfirmation,
            validInput,
        ) {
            RegisterFormState(
                name = name,
                validName = validName,
                email = email,
                validEmail = validEmail,
                password = password,
                validPassword = validPassword,
                passwordConfirmation = passwordConfirmation,
                validPasswordConfirmation = validPasswordConfirmation,
                validInput = validInput,
            )
        }
    }

    RegisterScreen(
        navigator = navigator,
        signUpResult = viewModel.signUpResult,
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun RegisterScreen(
    navigator: DestinationsNavigator,
    signUpResult: Result<FirebaseUser>?,
    state: RegisterFormState,
    onEvent: (RegisterEvent) -> Unit,
) {
    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val imageCropper = rememberImageCropper()
    val snackbarHostState = remember { SnackbarHostState() }

    var pic by remember { mutableStateOf<ImageBitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        scope.launch {
            val result = imageCropper.crop(uri, context)
            result.getOrNull()?.let {
                onEvent(RegisterEvent.Update.Picture(it.resized(300, 300)))
                pic = it
            }
        }
    }

    val (nameIcon, emailIcon, passwordIcon) = MaterialTheme.colorScheme.run {
        onSurfaceVariant pairWith primary
    }.forIcons(Icons.Outlined.Person, Icons.Outlined.Email, Icons.Outlined.Lock)

    ColorSystemBars()

    LaunchedEffect(signUpResult) {
        when (signUpResult) {
            is Result.Success -> navigator.navigate(OnboardingScreenDestination)

            is Result.Failure -> snackbarHostState.showSnackbar(
                message = when (signUpResult.e) {
                    is FirebaseAuthUserCollisionException -> {
                        navigator.popBackStack()
                        "Parece que você já tem uma conta! Agora é só fazer o login."
                    }

                    else -> signUpResult.e.localizedMessage
                        ?.toString()
                        ?: "Erro desconhecido"
                },
            )

            else -> Unit
        }
    }

    ImageCropper(imageCropper)

    Scaffold(
        topBar = {
            SylphTopBar(
                title = "Criar conta",
                navigationIcon = {
                    IconButton(onClick = navigator::popBackStack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Voltar",
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                SylphButton.Elevated(
                    label = "Criar conta",
                    enabled = state.validInput,
                    onClick = {
                        keyboardController?.hide()
                        onEvent(RegisterEvent.SignUp)
                    },
                    colors = SylphButtonDefaults.elevatedColors(
                        container = TransFlagColors.Blue,
                        content = TransFlagColors.OnBlue
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(
                            MaterialTheme.typography.labelMedium
                                .toSpanStyle()
                                .copy(color = MaterialTheme.colorScheme.primary)
                        ) {
                            append("Já tem uma conta? Faça o login agora mesmo!")
                        }
                    },
                    onClick = { navigator.popBackStack() },
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        },
    ) { pv ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clip(CircleShape)
                    .size(128.dp)
                    .clickable(
                        role = Role.Button,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false, radius = 64.dp),
                        onClick = launcher::pickImage,
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                when (val bitmap = pic) {
                    null -> Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Escolher foto de perfil",
                        modifier = Modifier.size(104.dp)
                    )

                    else -> Image(
                        bitmap = bitmap,
                        contentDescription = "Alterar foto de perfil",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            SylphTextField.SingleLine(
                value = state.name,
                onValueChange = { onEvent(RegisterEvent.Update.Name(it)) },
                placeholder = "Nome",
                leadingIcon = nameIcon,
                state = SylphTextFieldState.errorIf(!state.validName),
                helperText = "Nome não pode ficar em branco".takeIf { !state.validName },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            )

            SylphTextField.SingleLine(
                value = state.email,
                onValueChange = { onEvent(RegisterEvent.Update.Email(it)) },
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
                onValueChange = { onEvent(RegisterEvent.Update.Password(it)) },
                placeholder = "Senha",
                leadingIcon = passwordIcon,
                state = SylphTextFieldState.errorIf(!state.validPassword),
                helperText = ValidationRule.Password.helperTextFor(state.password)
                    .takeIf { !state.validPassword },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            )

            SylphTextField.Password(
                value = state.passwordConfirmation,
                onValueChange = { onEvent(RegisterEvent.Update.PasswordConfirmation(it)) },
                placeholder = "Confirmação da senha",
                leadingIcon = passwordIcon,
                state = SylphTextFieldState.errorIf(!state.validPasswordConfirmation),
                helperText = "A confirmação precisa ser igual à senha"
                    .takeIf { !state.validPasswordConfirmation },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(onGo = {
                    keyboardController?.hide()
                    onEvent(RegisterEvent.SignUp)
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            )
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    SylphTheme {
        RegisterScreen(
            navigator = EmptyDestinationsNavigator,
            signUpResult = null,
            state = RegisterFormState("", true, "", true, "", true, "", true, false),
            onEvent = {},
        )
    }
}