package com.example.mobile.DTOs

import java.io.Serializable

data class UserToBeStoredDTO(
    public val id: String,
    public val username: String,
    public val email: String,
    public val name: String,
    public val age: String,
    public val phone: String,
    public val photos: ArrayList<String>,
    public val about: String,
    public val location: String,
) : Serializable
