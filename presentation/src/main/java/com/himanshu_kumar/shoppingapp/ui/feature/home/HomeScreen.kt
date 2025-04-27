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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.himanshu_kumar.domain.model.CategoriesListModel

import com.himanshu_kumar.domain.model.ProductListModel
import com.himanshu_kumar.domain.model.UserDomainModel
import com.himanshu_kumar.shoppingapp.R
import com.himanshu_kumar.shoppingapp.model.UiProductModel
import com.himanshu_kumar.shoppingapp.navigation.CartScreen
import com.himanshu_kumar.shoppingapp.navigation.CategoryItemsScreen
import com.himanshu_kumar.shoppingapp.navigation.ProductDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()){
    val uiState = viewModel.uiState.collectAsState()
    val uiDetails = viewModel.userDetails.collectAsState()
    val loading = remember{
        mutableStateOf(false)
    }
    val error = remember {
        mutableStateOf<String?>(null)
    }

    val feature = remember { mutableStateOf<List<ProductListModel>>(emptyList()) }
    val popular = remember { mutableStateOf<List<ProductListModel>>(emptyList()) }
    val categories = remember { mutableStateOf<List<CategoriesListModel>>(emptyList()) }
    val userDetails = remember { mutableStateOf<UserDomainModel?>(null) }

    Scaffold {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = Color.White
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
            uiDetails.value?.let {
                userDetails.value = it
            }
            HomeContent(
                userDetails.value,
                feature.value,
                popular.value,
                categories.value,
                loading.value,
                error.value,
                onClick = {
                    navController.navigate(ProductDetails(UiProductModel.fromProduct(it)))
                },
                onCartClicked = {
                    navController.navigate(CartScreen)
                },
                onCategoryClicked = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("categoryId", it)
                    navController.navigate(CategoryItemsScreen)
                }
            )
        }
    }
}


@Composable
fun ProfileHeader(userDetails: UserDomainModel?,onCartClicked:()->Unit){
    Box(
      modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 16.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ){
            AsyncImage(model = userDetails?.avatar, contentDescription = null, modifier = Modifier.size(48.dp), contentScale = ContentScale.Crop)
            Spacer(Modifier.size(8.dp))
            Column {
                Text(
                    text = "Hello,",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = userDetails?.name ?: "User",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Row(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.3f))
                    .padding(8.dp),
                contentScale = ContentScale.Inside
            )
            Spacer(Modifier.size(5.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_cart),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.3f))
                    .padding(8.dp)
                    .clickable {
                        onCartClicked()
                    },
                contentScale = ContentScale.Inside
            )
        }
    }
}


@Composable
fun HomeContent(
    userDetails:UserDomainModel?,
    featured: List<ProductListModel>,
    popularProducts: List<ProductListModel>,
    categories:List<CategoriesListModel>,
    isLoading:Boolean = false,
    errorMessages:String? = null,
    onClick:(ProductListModel)->Unit,
    onCartClicked:()->Unit,
    onCategoryClicked:(Int)->Unit
) {
    LazyColumn {
        item {
            ProfileHeader(userDetails,onCartClicked)
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

            if (categories.isNotEmpty() || featured.isNotEmpty() || popularProducts.isNotEmpty()){
                BannerSection()
                Spacer(Modifier.size(16.dp))
            }

            if(featured.isNotEmpty()){
                HomeProductRow(products = featured, title = "Featured Products", onClick = onClick)
                Spacer(Modifier.size(16.dp))
            }

            if(categories.isNotEmpty()){
                CategorySection(categories) {
                    onCategoryClicked(it)
                }
            }

            if(popularProducts.isNotEmpty()){
                HomeProductRecommendedRow(products = popularProducts, title = "Recommended", onClick = onClick)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerSection(){

    val pagerState = rememberPagerState(initialPage = 0)
    val bannerImages = listOf(
        painterResource(R.drawable.banner1),
        painterResource(R.drawable.banner3),
        painterResource(R.drawable.banner2),
    )

    LaunchedEffect(Unit) {                 // it launches the coroutine when the key is change for here unit means it only launches once
        while (true) {                     // infinite loop
            yield()                        // it avoids blocking other things
            delay(2600)
            pagerState.animateScrollToPage(                   // smoothly animate to next page
                page = (pagerState.currentPage + 1) % (pagerState.pageCount)     // repeat the page
            )
        }
    }
    Column {
        HorizontalPager(
            count = bannerImages.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .height(114.dp)
                .fillMaxWidth()
        ) { page ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                Image(
                    painter = bannerImages[page],
                    contentDescription = stringResource(R.string.image_slider),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(150.dp)
//            .padding(horizontal = 20.dp, vertical = 8.dp)
//            .clip(RoundedCornerShape(16.dp))
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.banner1),
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.Crop
//        )
//    }
}


@Composable
fun CategorySection(
    categories: List<CategoriesListModel>,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier.height(100.dp)
    ) {
        // Title Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        // LazyRow of categories
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories, key = { it.id }) { category ->

                val isVisible = remember { mutableStateOf(false) }
                LaunchedEffect(true) {
                    isVisible.value = true
                }
                AnimatedVisibility(visible = isVisible.value, enter = fadeIn()+ expandVertically()) {
                    CategoryItem(
                        category = category,
                        onClick = { onClick(category.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoriesListModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.LightGray.copy(alpha = 0.3f))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = category.image,
            contentDescription = null,
            modifier = Modifier.size(65.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1
        )
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
fun HomeProductRecommendedRow(products:List<ProductListModel>, title:String, onClick:(ProductListModel)->Unit){
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
                modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}