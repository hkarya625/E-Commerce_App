package com.himanshu_kumar.shoppingapp.ui.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.himanshu_kumar.domain.model.Product
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()){
    val uiState = viewModel.uiState.collectAsState()

    when(uiState.value){
        is HomeScreenUIEvents.Loading->{
           CircularProgressIndicator()
        }
        is HomeScreenUIEvents.Success-> {
            val data = (uiState.value as HomeScreenUIEvents.Success).data

            LazyColumn{
                items(data){
                    product->
                    ProductItem(product)
                }
            }
        }
        is HomeScreenUIEvents.Error ->{
            Text(text = (uiState.value as HomeScreenUIEvents.Error).message)
        }
    }
}

@Composable
fun ProductItem(product: Product){
    Card(
        Modifier.padding(8.dp)
    ) {
        Text(text = product.title, modifier = Modifier.padding(8.dp))
    }
}