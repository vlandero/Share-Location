package com.example.mobile.DTOs

data class UserToBeStoredDTO(
    public val id: String,
    public val username: String,
    public val email: String,
    public val name: String,
    public val phone: String,
    public val photos: ArrayList<String>,
    public val about: String,
    public val location: String,
)
