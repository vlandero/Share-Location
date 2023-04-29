package com.example.mobile.DTOs

data class UserToBeStoredDTO(
    public val Id: String,
    public val Username: String,
    public val Email: String,
    public val Name: String,
    public val Phone: String,
    public val Photos: ArrayList<String>,
    public val About: String,
    public val Location: String,
)
