package com.himanshu_kumar.shoppingapp.ui.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.himanshu_kumar.domain.model.CategoriesListModel

import com.himanshu_kumar.domain.model.ProductListModel
import com.himanshu_kumar.shoppingapp.R
import com.himanshu_kumar.shoppingapp.model.UiProductModel
import com.himanshu_kumar.shoppingapp.navigation.ProductDetails
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()){
    val uiState = viewModel.uiState.collectAsState()
    val loading = remember{
        mutableStateOf(false)
    }
    val error = remember {
        mutableStateOf<String?>(null)
    }

    val feature = remember { mutableStateOf<List<ProductListModel>>(emptyList()) }
    val popular = remember { mutableStateOf<List<ProductListModel>>(emptyList()) }
    val categories = remember { mutableStateOf<List<CategoriesListModel>>(emptyList()) }

    Scaffold {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when(uiState.value){
                is HomeScreenUIEvents.Loading->{
                    loading.value = true
                    error.value = null
                }
                is HomeScreenUIEvents.Success-> {
                    val data = (uiState.value as HomeScreenUIEvents.Success)
                    feature.value = data.featured
                    popular.value = data.popularProducts
                    categories.value = data.categories
                    loading.value = false
                    error.value = null
                }
                is HomeScreenUIEvents.Error ->{
                    val errorMessage =  (uiState.value as HomeScreenUIEvents.Error).message
                    loading.value = false
                    error.value = errorMessage
                }
            }
            HomeContent(
                feature.value,
                popular.value,
                categories.value,
                loading.value,
                error.value,
                onClick = {
                    navController.navigate(ProductDetails(UiProductModel.fromProduct(it)))
                }
            )
        }
    }
}


@Composable
fun ProfileHeader(){
    Box(
      modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 16.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ){
            Image(painter = painterResource(id = R.drawable.ic_profile), contentDescription = null, modifier = Modifier.size(48.dp))
            Spacer(Modifier.size(8.dp))
            Column {
                Text(
                    text = "Hello,",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "John Doe",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterEnd)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.3f))
                .padding(8.dp),
            contentScale = ContentScale.Inside
        )
    }
}


@Composable
fun HomeContent(
    featured: List<ProductListModel>,
    popularProducts: List<ProductListModel>,
    categories:List<CategoriesListModel>,
    isLoading:Boolean = false,
    errorMessages:String? = null,
    onClick:(ProductListModel)->Unit
) {
    LazyColumn {
        item {
            ProfileHeader()
            Spacer(Modifier.size(16.dp))
            SearchBar(value = "", onTextChanged = {})
            Spacer(Modifier.size(16.dp))
        }
        item {
            if(isLoading){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                    Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
                }
            }

            errorMessages?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            if(categories.isNotEmpty()){
                LazyRow {
                    items(categories ,
                        key = {it.id}
                    ){category ->
                        val isVisible = remember { mutableStateOf(false) }
                        LaunchedEffect(true) {
                            isVisible.value = true
                        }
                        AnimatedVisibility(visible = isVisible.value, enter = fadeIn()+ expandVertically()) {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(8.dp)
                            )
                        }
                    }
                }
                Spacer(Modifier.size(16.dp))
            }
           if(featured.isNotEmpty()){
               HomeProductRow(products = featured, title = "Featured", onClick = onClick)
               Spacer(Modifier.size(16.dp))
           }
            if(popularProducts.isNotEmpty()){
                HomeProductRow(products = popularProducts, title = "Popular Products", onClick = onClick)
            }
        }
    }
}


@Composable
fun SearchBar(value:String, onTextChanged:(String)->Unit){
    TextField(
        value = value,
        onValueChange = onTextChanged,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = {
            Image(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f)
        ),
        placeholder = {
            Text(
                text = "Search for products",
                style = MaterialTheme.typography.bodySmall
            )
        }
    )
}

@Composable
fun HomeProductRow(products:List<ProductListModel>, title:String, onClick:(ProductListModel)->Unit){
    Column {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(
                    Alignment.CenterStart
                ),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.align(
                    Alignment.CenterEnd
                ),
                text = "View all",
                style = MaterialTheme.typography.bodyMedium,
                color =  MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.size(8.dp))
        LazyRow {
            items(products, key = {it.id}){ product ->
                val isVisible = remember { mutableStateOf(false) }
                LaunchedEffect(true) {
                    isVisible.value = true
                }
                AnimatedVisibility(visible = isVisible.value, enter = fadeIn()+ expandVertically()) {
                    ProductItem(
                        product,
                        onClick = onClick
                    )
                }
            }
        }
    }
}


@Composable
fun ProductItem(product: ProductListModel, onClick:(ProductListModel)->Unit){
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(width = 126.dp, height = 144.dp)
            .clickable { onClick(product) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray.copy(alpha = 0.3f)                                     // alpha -> transparency
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = product.images[0],
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}