package com.himanshu_kumar.shoppingapp.ui.feature.summary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.himanshu_kumar.domain.model.CartItemModel
import com.himanshu_kumar.domain.model.CartSummary
import com.himanshu_kumar.shoppingapp.BottomNavItem
import com.himanshu_kumar.shoppingapp.R
import com.himanshu_kumar.shoppingapp.model.UserAddress
import com.himanshu_kumar.shoppingapp.navigation.HomeScreen
import com.himanshu_kumar.shoppingapp.navigation.UserAddressRoute
import com.himanshu_kumar.shoppingapp.navigation.UserAddressWrapper
import com.himanshu_kumar.shoppingapp.ui.feature.cart.CartViewModel
import com.himanshu_kumar.shoppingapp.ui.feature.home.HomeScreen
import com.himanshu_kumar.shoppingapp.ui.feature.user_address.USER_ADDRESS_SCREEN
import com.himanshu_kumar.shoppingapp.utils.CurrencyUtils
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartSummaryScreen(
    navController: NavController,
    viewModel: CartSummaryViewModel = koinViewModel()
){
    val address = remember{ mutableStateOf<UserAddress?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val uiState = viewModel.uiState.collectAsState()

        LaunchedEffect(navController) {
            val savedState = navController.currentBackStackEntry?.savedStateHandle
            savedState?.getStateFlow(USER_ADDRESS_SCREEN, address.value)?.collect{ userAddress ->
                address.value = userAddress
            }
        }
        if(uiState.value !is CartSummaryEvent.PlaceOrder)
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ){
                Text(
                    text = "Cart Summary",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {

            when(val event = uiState.value){
                is CartSummaryEvent.Loading ->{
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        CircularProgressIndicator()
                        Text(text = "Loading...", style = MaterialTheme.typography.titleMedium)
                    }
                }
                is CartSummaryEvent.Error ->{
                    Text(text = event.message, style = MaterialTheme.typography.titleMedium)
                }
                is CartSummaryEvent.Success ->{
                    Column {
                        AddressBar(address = address.value?.toString()?:"", onClick = {
                            navController.navigate(UserAddressRoute(UserAddressWrapper(address.value)))
                        })
                        Spacer(modifier = Modifier.size(8.dp))
                        CartSummaryScreenContent(cartSummary = event.cartSummary)
                    }
                }

                is CartSummaryEvent.PlaceOrder -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_success),
                            contentDescription = null
                        )
                        Text(
                            text = "Order Placed Successfully",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.size(8.dp))
                        Button(onClick = {
                            navController.popBackStack(
                                HomeScreen,
                                inclusive = false
                            )
                        }) {
                            Text(
                                text = "Continue Shopping",
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                }
            }
        }

        if(uiState.value !is CartSummaryEvent.PlaceOrder){
            Button(onClick = {
                viewModel.placeOrder(address.value!!)
            },modifier = Modifier.fillMaxWidth(),
                enabled = address.value!=null
            ) {
                Text(
                    text = "Place Order",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
fun CartSummaryScreenContent(cartSummary: CartSummary){
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray.copy(alpha = 0.4f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ){
        item {
            Text(text = "Order Summary:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        items(cartSummary.data.items){ item->
            ProductRow(cartItemModel = item)
        }
        item {
            Column {
                AmountRow("Subtotal", cartSummary.data.subtotal)
                AmountRow("Tax", cartSummary.data.tax)
                AmountRow("Shipping", cartSummary.data.shipping)
                AmountRow("Discount", cartSummary.data.discount)
                AmountRow("Total", cartSummary.data.total)
            }
        }
    }
}

@Composable
fun ProductRow(cartItemModel:CartItemModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = cartItemModel.productName,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 14.sp
        )
        val price = CurrencyUtils.formatPrice(cartItemModel.price.toDouble())
        Text(
            text = "$price x ${cartItemModel.quantity} ",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 16.sp
        )
    }
}

@Composable
fun AmountRow(title:String, amount:Double){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge, fontSize = 14.sp)
        Text(text = CurrencyUtils.formatPrice(amount), style = MaterialTheme.typography.titleMedium, fontSize = 16.sp)
    }
}

@Composable
fun AddressBar(address:String, onClick:()->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick.invoke() }
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_address),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.4f)),
            contentScale = ContentScale.Inside
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            Text(
                text = "Shipping Address",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 16.sp
            )
            Text(
                text = address,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
