package com.himanshu_kumar.data.network

import com.himanshu_kumar.data.model.request.AddToCartRequest
import com.himanshu_kumar.data.model.response.CartItem
import com.himanshu_kumar.data.model.response.CartResponse
import com.himanshu_kumar.data.model.response.CategoriesListResponse
import com.himanshu_kumar.data.model.response.ProductListResponse
import com.himanshu_kumar.domain.model.CartItemModel
import com.himanshu_kumar.domain.model.CartModel
import com.himanshu_kumar.domain.model.CategoriesListModel
import com.himanshu_kumar.domain.model.ProductListModel
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
import io.ktor.util.InternalAPI
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

    override suspend fun addProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/1"
//        return makeWebRequest(
//            url = url,
//            method = HttpMethod.Post,
//            body = AddToCartRequest.fromCartRequestModel(request),
//            mapper = { cartItem:CartResponse->
//                cartItem.toCartModel()
//            }
//        )

        val fakeCart = CartModel(
            data = listOf(
                CartItemModel(
                    id = 3,
                    productId = 12,
                    userId = 123,
                    name = "Tom Cruise",
                    price = 69,
                    imageUrl = "https://i.imgur.com/yVeIeDa.jpeg",
                    quantity = 2,
                    productName = "Classic Heather Gray Hoodie"
                )
            ),
            msg = "Product added to cart successfully"
        )
        return ResultWrapper.Success(fakeCart)
    }

    override suspend fun getCart(): ResultWrapper<CartModel> {
        val fakeCart = CartModel(
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
                ), CartItemModel(
                    id = 2,
                    productId = 40,
                    userId = 123,
                    name = "Tom Cruise",
                    price = 90,
                    imageUrl = "https://i.imgur.com/qNOjJje.jpeg",
                    quantity = 2,
                    productName = "Futuristic Silver and Gold High-Top Sneaker"
                ),
                CartItemModel(
                    id = 3,
                    productId = 14,
                    userId = 123,
                    name = "Tom Cruise",
                    price = 61,
                    imageUrl = "https://i.imgur.com/yVeIeDa.jpeg",
                    quantity = 2,
                    productName = "Classic Navy Blue Baseball Cap"
                ),
                CartItemModel(
                    id = 4,
                    productId = 49,
                    userId = 123,
                    name = "Tom Cruise",
                    price = 50,
                    imageUrl = "https://i.imgur.com/Lqaqz59.jpg",
                    quantity = 1,
                    productName = "Classic Navy Blue Baseball Cap"
                )
            ),
            msg = "Product added to cart successfully"
        )
        return ResultWrapper.Success(fakeCart)
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