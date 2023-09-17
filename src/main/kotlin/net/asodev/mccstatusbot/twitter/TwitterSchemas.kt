package net.asodev.mccstatusbot.twitter

class OAuthResponse(
    val token_type: String,
    val expires_in: Long,
    val access_token: String,
    val scope: String,
    val refresh_token: String
)

class TweetRequest(
    val text: String
)