package net.asodev.mccstatusbot.twitter

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Service

@Service
class Twitter(val auth: TwitterOAuth, val objectMapper: ObjectMapper) {

    fun sendTweet(tweet: TweetRequest) {
        val client = OkHttpClient()
        val type = "application/json".toMediaType()
        val content = objectMapper.writeValueAsString(tweet)
        val body = content.toRequestBody(type)

        val request = Request.Builder()
            .url("https://api.twitter.com/2/tweets")
            .post(body)
            .addHeader("Authorization", auth.authoriseRequest())
            .build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            println("Failed to send tweet. ${response.code} -> ${response.body?.string()}")
        }
    }

}