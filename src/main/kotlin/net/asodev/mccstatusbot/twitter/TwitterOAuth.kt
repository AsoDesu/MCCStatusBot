package net.asodev.mccstatusbot.twitter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import jakarta.annotation.PostConstruct
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.util.Base64

@Service
class TwitterOAuth(val objectMapper: ObjectMapper) {
    val client = OkHttpClient()
    final val file = File("./refresh-token.txt")

    var accessToken: String? = null
    var refreshToken = file.readText()
        set(value) {
            field = value
            file.writeText(value)
        }
    var tokenExpires: Long = 0

    @Value("\${spring.twitter.client-id}")
    lateinit var clientId: String
    @Value("\${spring.twitter.client-secret}")
    lateinit var clientSecret: String
    lateinit var appAuthorization: String

    @PostConstruct
    fun construct() {
        appAuthorization = Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())
        authoriseRequest() // Cache new access token
    }

    fun authoriseRequest(): String {
        if (tokenExpires <= System.currentTimeMillis()) accessToken = null
        if (accessToken == null) requestNewAccessToken()

        return "Bearer $accessToken"
    }

    fun requestNewAccessToken() {
        val formBody = FormBody.Builder()
            .addEncoded("grant_type", "refresh_token")
            .addEncoded("refresh_token", refreshToken)
            .build()
        val request = Request.Builder()
            .url("https://api.twitter.com/2/oauth2/token")
            .post(formBody)
            .addHeader("Authorization", "Basic $appAuthorization")
            .build()
        val response = client.newCall(request).execute()
        val string = response.body?.string() ?: throw IllegalStateException("Failed to get string from response")
        val data = objectMapper.readValue(string, OAuthResponse::class.java)

        refreshToken = data.refresh_token
        tokenExpires = System.currentTimeMillis() + data.expires_in
        accessToken = data.access_token
    }

}