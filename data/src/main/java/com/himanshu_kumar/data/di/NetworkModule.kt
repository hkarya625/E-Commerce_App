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

val networkModule = module {
    single {
        HttpClient(CIO){                              // cio is an engine used for http client
            install(ContentNegotiation){              // configures JSON serialization and deserialization
                json(Json {
                    prettyPrint = true                // Formats JSON output in a readable way.
                    isLenient = true                  // Allows parsing of non-standard JSON (e.g., missing quotes).
                    ignoreUnknownKeys = true          // Ignores extra JSON fields not present in the Kotlin data class.
                })
            }
            install(Logging){                         // enables logging of HTTP requests and responses, useful for debugging.
                level = LogLevel.ALL                  // logs everything
            }
        }
    }

    single<NetworkService> {
        NetworkServiceImpl(get())
    }
}