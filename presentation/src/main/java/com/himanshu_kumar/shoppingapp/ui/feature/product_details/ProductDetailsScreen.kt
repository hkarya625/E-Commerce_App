package com.himanshu_kumar.shoppingapp.ui.feature.product_details

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.himanshu_kumar.domain.model.ProductListModel
import com.himanshu_kumar.shoppingapp.R
import com.himanshu_kumar.shoppingapp.model.UiProductModel
import com.himanshu_kumar.shoppingapp.ui.feature.home.ProductItem
import org.koin.androidx.compose.koinViewModel
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProductDetailsScreen(
    navController: NavController,
    product: UiProductModel,
    viewModel: ProductDetailsViewModel = koinViewModel()
) {
    val uiState = viewModel.state.collectAsState()
    val loading = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // MAIN CONTENT
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 12.dp)
        ) {
            item {
                // 🖼️ Image slider
                val pagerState = rememberPagerState()
                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                ) {
                    HorizontalPager(
                        count = product.images.size,
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = product.images[page],
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Pager indicator (small dots at bottom center)
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    )

                    // Back Button
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(8.dp)
                            .align(Alignment.TopStart)
                            .clickable {
                                if (!navController.popBackStack()) {
                                    navController.navigateUp()
                                }
                            }
                    )

                    // Favorite Button
                    val isFavorite = remember { mutableStateOf(false) }
                    Image(
                        painter = painterResource(id = R.drawable.ic_favorite),
                        contentDescription = "Favorite",
                        colorFilter = ColorFilter.tint(
                            if (isFavorite.value) Color.Red else Color.Black.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier
                            .padding(16.dp)
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(8.dp)
                            .align(Alignment.TopEnd)
                            .clickable { isFavorite.value = !isFavorite.value }
                    )
                }
            }

            item {
                // 📝 Product Details
                ProductDetailsContent(product = product, viewModel = viewModel)
            }

            item {
                viewModel.getSimilarProducts(product.categoryId)
                val similarProducts = viewModel.similarProducts.collectAsState()
                SimilarProducts(products = similarProducts.value)
            }
        }

        // LOADING Overlay
        if (loading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = "Adding to cart...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }
        }
    }

    // EFFECT for API Result
    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is ProductDetailsState.Success -> {
                loading.value = false
                Toast.makeText(
                    navController.context,
                    (uiState.value as ProductDetailsState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ProductDetailsState.Error -> {
                loading.value = false
                Toast.makeText(
                    navController.context,
                    (uiState.value as ProductDetailsState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ProductDetailsState.Loading -> loading.value = true
            else -> loading.value = false
        }
    }
}

@Composable
fun SimilarProducts(products:List<ProductListModel> ){
    Column {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text(
                text = "Similar Products",
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
                AnimatedVisibility(visible = isVisible.value, enter = fadeIn() + expandVertically()) {
                    Item(
                        product,
                        onClick = {}
                    )
                }
            }
        }
    }
}
@Composable
fun Item(product: ProductListModel, onClick:(ProductListModel)->Unit){
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
                modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
@Composable
fun ProductDetailsContent(product: UiProductModel, viewModel: ProductDetailsViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = "Rating"
            )
            Spacer(Modifier.width(4.dp))
            Text("4.5", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.width(16.dp))
            Text("100 Reviews", style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(16.dp))
        Text("Description", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(product.description, style = MaterialTheme.typography.bodySmall)

        Spacer(Modifier.height(16.dp))
        Text("Size", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Row {
            repeat(4) { index ->
                SizeItem(size = "${index + 1}", isSelected = index == 0) { }
            }
        }

        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { viewModel.addProductToCart(product) },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text("Buy Now")
            }
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = { viewModel.addProductToCart(product) },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_order),
                    contentDescription = "Add to cart"
                )
            }
        }
    }
}

@Composable
fun SizeItem(size:String, isSelected:Boolean, onClick:()->Unit){
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color =  Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(if (isSelected) colorResource(R.color.button_color) else Color.Transparent)
            .padding(8.dp)
            .clickable {
                onClick()
            }
    ){
        Text(
            text = size,
            color = if (isSelected) Color.White else Color.Black,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
