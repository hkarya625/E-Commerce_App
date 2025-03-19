package com.himanshu_kumar.data.network

import com.himanshu_kumar.data.model.DataProductModel
import com.himanshu_kumar.domain.model.Product
import com.himanshu_kumar.domain.network.NetworkService
import com.himanshu_kumar.domain.network.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import io.ktor.utils.io.errors.IOException


// This class is a network service that fetches product data from an API ,transforms it, and handles network request errors.

class NetworkServiceImpl(val client: HttpClient) : NetworkService {                                 // Implementation of NetworkService interface for fetching product data.

    private val baseUrl = "https://fakestoreapi.com"
    override suspend fun getProducts(category:String?): ResultWrapper<List<Product>> {              // Asynchronous function to retrieve a list of products.

        val url = if (category != null) {
            "$baseUrl/products/category/$category"                                                 // API endpoint for product data with category.
        } else {
            "$baseUrl/products"                                                                    // API endpoint for product data without category.
        }
        return makeWebRequest(
            url = url,                                                                              // API endpoint for product data.
            method = HttpMethod.Get,                                                                // HTTP GET method for retrieving data.
            mapper = { dataModels: List<DataProductModel> ->
                                                                                                    // Mapper function to transform DataProductModel to Product.
                dataModels.map { it.toProduct() }
            }
        )
    }

    override suspend fun getCategories(): ResultWrapper<List<String>> {
        val url = "$baseUrl/products/categories"
        return makeWebRequest<List<String>, List<String>>(
            url = url,
            method = HttpMethod.Get
        )
    }

    @OptIn(InternalAPI::class)                                                                      // Opt-in to use internal Ktor APIs (use with caution).
    suspend inline fun <reified T, R> makeWebRequest(
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
                    this.body = body                                                                // Sets the request body.
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