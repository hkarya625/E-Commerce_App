package com.himanshu_kumar.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.himanshu_kumar.data.model.request.AddressOrderModel
import com.himanshu_kumar.data.model.request.LoginRequest
import com.himanshu_kumar.data.model.request.RegisterRequest
import com.himanshu_kumar.data.model.request.TokenRequest
import com.himanshu_kumar.data.model.response.CartItem
import com.himanshu_kumar.data.model.response.CartSummaryResponse
import com.himanshu_kumar.data.model.response.CategoriesListResponse
import com.himanshu_kumar.data.model.response.OrderListData
import com.himanshu_kumar.data.model.response.OrdersListResponse
import com.himanshu_kumar.data.model.response.PlaceOrderResponse
import com.himanshu_kumar.data.model.response.ProductListResponse
import com.himanshu_kumar.data.model.response.RegisterResponse
import com.himanshu_kumar.data.model.response.Summary
import com.himanshu_kumar.data.model.response.TokenResponse
import com.himanshu_kumar.data.model.response.UserResponse
import com.himanshu_kumar.domain.model.AddressDomainModel
import com.himanshu_kumar.domain.model.CartItemModel
import com.himanshu_kumar.domain.model.CartModel
import com.himanshu_kumar.domain.model.CartSummary
import com.himanshu_kumar.domain.model.CategoriesListModel
import com.himanshu_kumar.domain.model.OrderProductItem
import com.himanshu_kumar.domain.model.OrdersData
import com.himanshu_kumar.domain.model.OrdersListModel
import com.himanshu_kumar.domain.model.ProductListModel
import com.himanshu_kumar.domain.model.UserDomainModel
import com.himanshu_kumar.domain.model.request.AddCartRequestModel
import com.himanshu_kumar.domain.network.NetworkService
import com.himanshu_kumar.domain.network.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException


// This class is a network service that fetches product data from an API ,transforms it, and handles network request errors.

class NetworkServiceImpl(private val client: HttpClient) : NetworkService {                                 // Implementation of NetworkService interface for fetching product data.

    private val baseUrl = "https://api.escuelajs.co/api/v1"
    override suspend fun getProducts(category:Int?): ResultWrapper<List<ProductListModel>> {              // Asynchronous function to retrieve a list of products.

        val url = if (category != null) {
            "$baseUrl/products/?categoryId=$category"                                                 // API endpoint for product data with category.
        } else {
            "$baseUrl/products"                                                                    // API endpoint for product data without category.
        }
        val data = makeWebRequest(
            url = url,                                                                              // API endpoint for product data.
            method = HttpMethod.Get,                                                                // HTTP GET method for retrieving data.
            mapper = { dataModels:List<ProductListResponse> ->
                dataModels.map { it.toProductList() }                                              // Maps the list of data models to a list of product models.
            }
        )
        return makeWebRequest(
            url = url,                                                                              // API endpoint for product data.
            method = HttpMethod.Get,                                                                // HTTP GET method for retrieving data.
            mapper = { dataModels:List<ProductListResponse> ->
                dataModels.map { it.toProductList() }                                              // Maps the list of data models to a list of product models.
            }
        )
    }

    override suspend fun getCategories(): ResultWrapper<List<CategoriesListModel>> {
        val url = "$baseUrl/categories"

        return makeWebRequest<List<CategoriesListResponse>, List<CategoriesListModel>>(
            url = url,
            method = HttpMethod.Get,
            mapper = { categories: List<CategoriesListResponse> ->
                categories.map { it.toCategoryListModel() }
            }
        )
    }


    // for testing purpose only
    private var fakeCart = CartModel(
        data = listOf(
            CartItemModel(
                id = 1,
                productId = 37,
                userId = 123,
                name = "Tom Cruise",
                price = 33,
                imageUrl = "https://i.imgur.com/9qrmE1b.jpeg",
                quantity = 2,
                productName = "Chic Summer Denim Espadrille Sandals"
            ),
            CartItemModel(
                id = 2,
                productId = 7,
                userId = 123,
                name = "Tom Cruise",
                price = 79,
                imageUrl = "https://i.imgur.com/mp3rUty.jpeg",
                quantity = 1,
                productName = "Classic Comfort Drawstring Joggers"
            )
        ),
        msg = "Product added to cart successfully"
    )
//    private var fakeCart:CartModel = CartModel(
//        data = emptyList(),
//        msg = "empty list"
//    )
    override suspend fun addProductToCart(request: AddCartRequestModel, userId:Long): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/$userId"
//        return makeWebRequest(
//            url = url,
//            method = HttpMethod.Post,
//            body = AddToCartRequest.fromCartRequestModel(request),
//            mapper = { cartItem:CartResponse->
//                cartItem.toCartModel()
//            }
//        )
//        fakeCart = fakeCart.copy(
//            data = fakeCart.data.
//        )
        val newItem = CartItemModel(
            id = fakeCart.data.size + 1,
            productId = request.productId,
            userId = request.userId,
            name = "",
            price = request.price,
            imageUrl = null,
            quantity =  request.quantity,
            productName = request.productName,
        )
        val updatedData = fakeCart.data + newItem  // creates a new list with the new item
        fakeCart = fakeCart.copy(
            data = updatedData,
            msg = "Product added to cart successfully"
        )
        return ResultWrapper.Success(fakeCart)
    }




    override suspend fun getCart(userId:Long): ResultWrapper<CartModel> {
//        val url = "$baseUrl/cart/$userId"
//        return makeWebRequest(
//            url = url,
//            method = HttpMethod.Get,
//            mapper = { cartItem:CartResponse->
//                cartItem.toCartModel()
//            }
//        )
        Log.d("fakeCart",fakeCart.toString())
        return ResultWrapper.Success(fakeCart)
    }

    override suspend fun updateQuantity(cartItemModel: CartItemModel, userId:Long): ResultWrapper<CartModel> {
//        val url = "$baseUrl/cart/$userId/${cartItemModel.id}"
//        return makeWebRequest(
//            url = url,
//            method = HttpMethod.Put,
//            body = AddToCartRequest(
//                productId = cartItemModel.productId,
//                productName = cartItemModel.productName,
//                price = cartItemModel.price,
//                quantity = cartItemModel.quantity,
//                userId = cartItemModel.userId
//            ),
//            mapper = { cartItem:CartResponse->
//                cartItem.toCartModel()
//            }
//        )
        fakeCart =   fakeCart.copy(
            data = fakeCart.data.mapIndexed { _, item ->
                if (item.id == cartItemModel.id) item.copy(quantity = cartItemModel.quantity)
                else item
            }
        )
        return ResultWrapper.Success(fakeCart)
    }

    override suspend fun deleteItem(cartItemId: Int, userId: Long): ResultWrapper<CartModel> {
//        val url = "$baseUrl/cart/$userId/$cartItemId"
//        return makeWebRequest(
//            url = url,
//            method = HttpMethod.Delete,
//            mapper = { cartItem:CartResponse->
//                cartItem.toCartModel()
//            }
//        )
        fakeCart =   fakeCart.copy(
            data = fakeCart.data.filter { it.id != cartItemId }
        )
        return ResultWrapper.Success(fakeCart)
    }

    override suspend fun getCartSummary(userId: Long): ResultWrapper<CartSummary> {
//        val url = "$baseUrl/cart/$userId/summary"
//        return makeWebRequest(
//            url = url,
//            method = HttpMethod.Get,
//            mapper = { cartSummary: CartSummaryResponse ->
//                cartSummary.toCartSummary()
//            }
//        )
        val fakeSummaryData = CartSummaryResponse(
            data = Summary(
                discount = 49.95,
                items = testing(fakeCart.data).first,
                shipping = 5.0,
                subtotal = testing(fakeCart.data).second,
                tax = 99.9,
                total = testing(fakeCart.data).second+5.0+99.9-49.95
            ),
            msg = "Checkout Summary"
        ).toCartSummary()
        return ResultWrapper.Success(fakeSummaryData)
    }

    override suspend fun placeOrder(address: AddressDomainModel, userId: Long): ResultWrapper<Long> {
//        val dataModel = AddressOrderModel.fromDomainAddress(address)
//        val url = "$baseUrl/orders/$userId"
//        return makeWebRequest(
//            url = url,
//            method = HttpMethod.Post,
//            body = dataModel,
//            mapper = { orderRes: PlaceOrderResponse ->
//                orderRes.data.id
//            }
//        )
        return ResultWrapper.Success(12345)
    }

    override suspend fun getOrderList(userId:Long): ResultWrapper<OrdersListModel> {
//        val url = "$baseUrl/orders/$userId"
//        return makeWebRequest(
//            url = url,
//            method = HttpMethod.Get,
//            mapper = { orderResponse: OrdersListResponse ->
//                orderResponse.toDomainResponse()
//            }
//        )
        val data = OrdersListModel(
            data = listOf(
                OrdersData(
                    id = 1,
                    items = listOf(
                        OrderProductItem(
                            id = 1,
                            orderId = 12,
                            price = 59.0,
                            productId = 37,
                            productName = "Chic Summer Denim Espadrille Sandals",
                            quantity = 2,
                            userId = 123
                        )
                    ),
                    orderDate = "2024-10-12",
                    status = "Pending",
                    totalAmount = 1200.0,
                    userId = 123
                ),
                OrdersData(
                    id = 2,
                    items = listOf(
                        OrderProductItem(
                            id = 2,
                            orderId = 13,
                            price = 60.0,
                            productId = 79,
                            productName = "Classic Comfort Drawstring Joggers",
                            quantity = 2,
                            userId = 123
                        )
                    ),
                    orderDate = "2024-10-15",
                    status = "Pending",
                    totalAmount = 1000.0,
                    userId = 123
                ),
                OrdersData(
                    id = 3,
                    items = listOf(
                        OrderProductItem(
                            id = 2,
                            orderId = 13,
                            price = 60.0,
                            productId = 79,
                            productName = "Shoes",
                            quantity = 2,
                            userId = 123
                        )
                    ),
                    orderDate = "2024-11-01",
                    status = "Cancelled",
                    totalAmount = 645.0,
                    userId = 123
                ),
                OrdersData(
                    id = 4,
                    items = listOf(
                        OrderProductItem(
                            id = 2,
                            orderId = 13,
                            price = 60.0,
                            productId = 79,
                            productName = "T-Shirt",
                            quantity = 2,
                            userId = 123
                        )
                    ),
                    orderDate = "2023-10-15",
                    status = "Delivered",
                    totalAmount = 896.0,
                    userId = 123
                )
            ),
            msg = "OrderList"
        )
        return ResultWrapper.Success(data)
    }



    override suspend fun login(email: String, password: String): ResultWrapper<UserDomainModel> {
        val urlForToken = "$baseUrl/auth/login"
        val accessTokenResult = makeWebRequest<TokenResponse, String>(
            url = urlForToken,
            method = HttpMethod.Post,
            body = TokenRequest(email, password),
            mapper = { tokenResponse -> tokenResponse.access_token }
        )

        if (accessTokenResult is ResultWrapper.Failure) {
            return ResultWrapper.Failure(accessTokenResult.message)
        }

        val accessToken = (accessTokenResult as? ResultWrapper.Success)?.value
            ?: return ResultWrapper.Failure("Token retrieval failed")

        val urlForProfile = "$baseUrl/auth/profile"

        return makeWebRequest<UserResponse, UserDomainModel>(
            url = urlForProfile,
            method = HttpMethod.Get,
            headers = mapOf("Authorization" to "Bearer $accessToken"),
            mapper = { userResponse -> userResponse.toUserDomainModel() }
        )
    }


    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): ResultWrapper<UserDomainModel> {
        val url = "$baseUrl/users/"
        val requestBody = mapOf(
            "email" to email,
            "password" to password,
            "name" to name,
            "avatar" to ""
        )
        val data = makeWebRequest(
            url = url,
            method = HttpMethod.Post,
            body = RegisterRequest(email, password, name),
            mapper = { registerResponse: RegisterResponse ->
                registerResponse.toUserDomainModel()
            }
        )
        Log.d("register",data.toString())
        return data
    }

    private fun testing(item:List<CartItemModel>):Pair<List<CartItem>,Double>{
        val list:List<CartItem> = item.map{
            CartItem(
                id = it.id,
                productId = it.productId,
                productName = it.productName,
                price = it.price,
                userId = it.userId,
                name = it.name,
                imageUrl = null,
                quantity = it.quantity,
            )
        }
        val total = item.sumOf { it.price * it.quantity }.toDouble()
        return Pair(list,total)
    }


    // Opt-in to use internal Ktor APIs (use with caution).
    private suspend inline fun <reified T, R> makeWebRequest(
        url: String,                                                                                // API endpoint URL.
        method: HttpMethod,                                                                         // HTTP method (GET, POST, etc.).
        body: Any? = null,                                                                          // Request body (for POST, PUT, etc.).
        headers: Map<String, String>? = emptyMap(),                                                 // Request headers.
        parameters: Map<String, String>? = emptyMap(),                                              // Query parameters.
        noinline mapper: ((T) -> R)? = null                                                         // Mapper function to transform response data.
    ): ResultWrapper<R> {
        // Generic function for making HTTP requests.
        // Uses Ktor's HttpClient to send requests.
        // Handles query parameters, headers, body, and content type.
        // Applies mapper function if provided.
        // Returns ResultWrapper to indicate success or failure.

        return try {
            val response = client.request(url) {
                // Configures the HTTP request.
                this.method = method                                                                // Sets the HTTP method.

                // Apply query parameters
                url {
                    this.parameters.appendAll(Parameters.build {
                        parameters?.forEach { (key, value) ->
                            append(key, value)                                                      // Adds query parameters to the URL.
                        }
                    })
                }

                // Apply headers
                headers?.forEach { (key, value) ->
                    header(key, value)                                                              // Adds headers to the request.
                }

                // set body for post, put etc.
                if (body != null) {
                    setBody(body)                                                              // Sets the request body.
                }

                // set content type
                contentType(ContentType.Application.Json)                                           // Sets the content type to JSON.
            }.body<T>()                                                                             // Retrieves the response body and converts it to type T.

            val result: R = mapper?.invoke(response) ?: response as R                               // Applies mapper or casts the response.
            ResultWrapper.Success(result)                                                           // Returns success with the result.
        } catch (e: ClientRequestException) {
            // Handles client-side errors (e.g., 4xx errors).
            ResultWrapper.Failure(e.message)
        } catch (e: ServerResponseException) {
            // Handles server-side errors (e.g., 5xx errors).
            ResultWrapper.Failure(e.message)
        } catch (e: IOException) {
            // Handles network I/O errors.
            ResultWrapper.Failure(e.message ?: "Unknown error")
        } catch (e: Exception) {
            // Handles other exceptions.
            ResultWrapper.Failure(e.message ?: "Unknown error")
        }
    }
}