package net.asodev.mccstatusbot.twitter

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class Twitter(val auth: TwitterOAuth, val objectMapper: ObjectMapper) {
    val LOGGER = LoggerFactory.getLogger(this::class.java)

    // Twitter has a character limit
    private val CHARACTER_LIMIT = 280

    fun tweet(content: String, reply: ReplyRequest? = null, replyNumber: Int = 0) {
        var tweetContent = content
        var remainingContent = ""

        val words = tweetContent.split(" ").toMutableList()
        while (tweetContent.length >= CHARACTER_LIMIT) {
            remainingContent = "${words.removeLast()} $remainingContent"
            tweetContent = words.joinToString(" ")
        }
        if (tweetContent.isEmpty()) return

        try {
            val request = TweetRequest(tweetContent, reply)
            val response = sendTweet(request)

            if (remainingContent.isEmpty() || replyNumber > 5) return
            tweet(remainingContent, ReplyRequest(response.data.id), replyNumber+1)
        } catch (e: Exception) {
            LOGGER.error(e.message)
            return
        }
    }

    fun sendTweet(tweet: TweetRequest): TweetResponse {
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

        return response.use {
            if (!response.isSuccessful) {
                throw RuntimeException("Failed to send tweet. ${response.code} -> ${response.body?.string()}")
            } else {
                return@use objectMapper.readValue(response.body?.string(), TweetResponse::class.java)
            }
        }
    }

}