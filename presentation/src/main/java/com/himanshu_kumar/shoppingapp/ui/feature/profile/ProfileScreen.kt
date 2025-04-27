package com.himanshu_kumar.shoppingapp.ui.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.himanshu_kumar.domain.model.UserDomainModel
import com.himanshu_kumar.shoppingapp.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val isLoading = remember { mutableStateOf(true) }
    val user = remember { mutableStateOf<UserDomainModel?>(null) }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = uiState.value) {
            is ProfileScreenEvent.Success -> {
                user.value = state.userDetails
            }
            is ProfileScreenEvent.Error -> {
                Text(text = state.message)
            }
            is ProfileScreenEvent.Loading -> {
                CircularProgressIndicator()
            }
        }
    }

    if(user.value != null){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
        ) {
            Spacer(Modifier.size(90.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = user.value!!.avatar,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = user.value!!.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = user.value!!.email,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = "Settings",
                    modifier = Modifier
                        .size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            // List Items
            val detailsList = listOf(
                DetailsListItem(
                    icon = painterResource(id = R.drawable.ic_address),
                    title = "Address"
                ),
                DetailsListItem(
                    icon = painterResource(id = R.drawable.ic_wishlist),
                    title = "My Wishlist"
                ),
                DetailsListItem(
                    icon = painterResource(id = R.drawable.ic_payment),
                    title = "Payment method"
                ),
                DetailsListItem(
                    icon = painterResource(id = R.drawable.ic_logout),
                    title = "Log out"
                )
            )

            detailsList.forEach {
                ProfileListItem(icon = it.icon, title = it.title)
            }
        }
    }

}

@Composable
fun ProfileListItem(icon: Painter, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { }
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(18.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_right_arrow),
            contentDescription = "Arrow",
            modifier = Modifier.size(20.dp)
        )
    }
    Divider(
        modifier = Modifier.padding(horizontal = 8.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        thickness = 1.dp
    )
    Spacer(modifier = Modifier.height(25.dp))
}

data class DetailsListItem(
    val icon: Painter,
    val title: String
)

//@Preview(showBackground = true)
//@Composable
//fun ProfileScreenPreview() {
//    ProfileScreen()
//}
