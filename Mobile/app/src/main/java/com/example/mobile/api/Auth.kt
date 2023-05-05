package com.example.mobile.api
import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
public class Auth {
    fun decodeJwtToken(token: String): Map<String, Any?>? {
        try {
            val jwt = JWT.decode(token)
            val claims = jwt.claims
            val resultMap = mutableMapOf<String, Any?>()
            for ((key, value) in claims) {
                resultMap[key] = value?.asString()
            }
            return resultMap
        } catch (e: JWTDecodeException) {
            println("Error decoding JWT token: ${e.message}")
        }
        return null
    }

}