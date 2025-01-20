package com.cevdetkilickeser.ktorimplementation

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiClient {
    private val client: HttpClient = HttpClient(OkHttp) {
        defaultRequest { url("https://fakestoreapi.com/") }

        install(Logging){
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }

        install(ContentNegotiation){
            json(Json{
                isLenient = true //Tırnak boşluk gibi hataları görmezden gel
                ignoreUnknownKeys = true //Karşılık gelmeyen alanları görmezden gel, modelde olan kadarını dahil et
                prettyPrint = true //Json çıktısı daha okunaklı
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }

        install(DefaultRequest){
            header(HttpHeaders.ContentType, ContentType.Application.Json)

        }
    }

    suspend fun getProducts(): ApiResponse<List<Product>> {
        return apiRequest {
            client.get("products").body<List<Product>>()
        }
    }

    private inline fun <T> apiRequest(callback: () -> T): ApiResponse<T> {
        return try {
            ApiResponse.Success(data = callback())
        } catch (e:Exception) {
            ApiResponse.Error(error = e)
        }
    }

    sealed interface ApiResponse<T>{
        data class Success<T>(val data: T): ApiResponse<T>
        data class Error<T>(val error: Exception): ApiResponse<T>

        fun onSuccess(block: (T) -> Unit): ApiResponse<T>{
            if(this is Success) block(data)
            return this
        }

        fun onError(block: (Exception) -> Unit): ApiResponse<T>{
            if(this is Error) block(error)
            return this
        }
    }
}