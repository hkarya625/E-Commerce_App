package com.himanshu_kumar.shoppingapp.ui.feature.category_list

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.himanshu_kumar.domain.model.ProductCategory
import com.himanshu_kumar.domain.model.ProductListModel
import com.himanshu_kumar.shoppingapp.R
import com.himanshu_kumar.shoppingapp.model.UiProductModel
import com.himanshu_kumar.shoppingapp.navigation.ProductDetails
import org.koin.androidx.compose.koinViewModel
@Composable
fun CategoryItemsListScreen(
    navController: NavController,
    category: Int,
    viewModel: CategoryItemsListViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    // Fetch products when category changes
    LaunchedEffect(category) {
        viewModel.getProductsWithCategory(category)
    }

    when (val state = uiState.value) {
        is CategoryItemsListUIEvents.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
                Spacer(Modifier.size(5.dp))
                Text(text = "Loading...",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
        is CategoryItemsListUIEvents.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.message, color = Color.Red)
            }
        }
        is CategoryItemsListUIEvents.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                // Top Bar
                CategoryTopBar(
                    title = state.data.firstOrNull()?.category?.name ?: "Category",
                    onBackClick = { navController.popBackStack() },
                    onSearchClick = {

                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Product List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.data) { item ->
                        ProductItem(
                            product = item,
                            onProductClicked = {
                                navController.navigate(ProductDetails(UiProductModel.fromProduct(item)))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryTopBar(
    title: String,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Back",
            modifier = Modifier
                .size(25.dp)
                .clickable { onBackClick() }
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Image(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "Search",
            modifier = Modifier
                .size(25.dp)
                .clickable { onSearchClick() }
        )
    }
}

@Composable
fun ProductItem(
    product: ProductListModel,
    onProductClicked: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onProductClicked()
            },
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = null,
                modifier = Modifier
                    .size(125.dp)
                    .padding(end = 8.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Rs.${product.price}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

