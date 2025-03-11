package com.himanshu_kumar.data.di

import com.himanshu_kumar.data.network.NetworkServiceImpl
import com.himanshu_kumar.domain.network.NetworkService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module


// This module focuses on configuring and providing the network-related components of the application.
val networkModule = module {
    single {                                      // Koin declaration for a singleton instance
        HttpClient(CIO) {                         // Creates an HttpClient with the CIO engine
            install(ContentNegotiation) {         // Installs JSON content negotiation
                json(Json {                       // Configures JSON serialization/deserialization
                    prettyPrint = true            // Makes JSON output readable (for debugging)
                    isLenient = true              // Allows parsing of lenient JSON formats
                    ignoreUnknownKeys = true      // Ignores unknown JSON keys
                })
            }
            install(Logging) {                    // Installs HTTP request/response logging
                level = LogLevel.ALL              // Logs all HTTP traffic (for debugging)
            }
        }
    }
    single<NetworkService> {
        NetworkServiceImpl(get())
    }
}