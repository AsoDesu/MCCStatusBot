package net.asodev.mccstatusbot.twitter

import com.fasterxml.jackson.annotation.JsonInclude

class OAuthResponse(
    val token_type: String,
    val expires_in: Long,
    val access_token: String,
    val scope: String,
    val refresh_token: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
class TweetRequest(
    val text: String,
    val reply: ReplyRequest? = null
)
class ReplyRequest(
    val in_reply_to_tweet_id: String
)

class TweetResponse(
    val data: TweetResponseData
)
class TweetResponseData(
    val id: String,
    val text: String
)