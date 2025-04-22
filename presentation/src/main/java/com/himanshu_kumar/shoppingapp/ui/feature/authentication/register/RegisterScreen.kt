package com.himanshu_kumar.shoppingapp.ui.feature.authentication.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.himanshu_kumar.shoppingapp.navigation.HomeScreen
import com.himanshu_kumar.shoppingapp.navigation.LoginScreen
import com.himanshu_kumar.shoppingapp.navigation.RegisterScreen
import com.himanshu_kumar.shoppingapp.ui.feature.authentication.login.LoginState
import com.himanshu_kumar.shoppingapp.ui.feature.authentication.login.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = koinViewModel()
){
    val registerState = viewModel.registerState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when(val state = registerState.value){
            is RegisterState.Success -> {
                LaunchedEffect(registerState.value) {
                    navController.navigate(HomeScreen)
                }
            }
            is RegisterState.Error -> {
                Text(text = state.message)
            }
            is RegisterState.Loading -> {
                CircularProgressIndicator()
                Text(text = "Loading...")
            }
            else ->{
                RegisterContent(
                    onRegisterClick = { email, password, name ->
                        viewModel.register(email, password, name)
                    },
                    onLoginClick = {
                        navController.popBackStack()
//                        navController.navigate(LoginScreen)
                    }
                )
            }
        }
    }
}

@Composable
fun RegisterContent(
    onRegisterClick:(String, String, String) -> Unit,
    onLoginClick:()->Unit
){
    val email = remember{ mutableStateOf("") }
    val password = remember{ mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Register",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp
        )
        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
            },
            label = { Text(text = "Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
            },
            label = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = name.value,
            onValueChange = {
                name.value = it
            },
            label = { Text(text = "Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Button(
            onClick = {
                onRegisterClick(email.value, password.value, name.value)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = email.value.isNotEmpty() && password.value.isNotEmpty() && name.value.isNotEmpty()
        ) {
            Text(text = "Register")
        }
        Text(
            text = "Already have an account? Login",
            modifier = Modifier.padding(vertical = 8.dp)
                .clickable {
                    onLoginClick()
                },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
