package io.say.better.client.oauth.google.dto

class GoogleOauth2Result private constructor() {
    init {
        throw IllegalStateException("Utility class")
    }

    data class GoogleUserInfo(
        val sub : String,
        val name : String,
        val email : String,
    )
}
