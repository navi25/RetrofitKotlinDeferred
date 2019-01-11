package io.navendra.retrofitkotlindeferred.data


data class PlaceholderUsers(
    val id:Int,
    val name: String,
    val username: String,
    val email: String,
    val address: PlaceholderAddress,
    val phone: String,
    val website: String,
    val company: PlaceholderCompany
)

/**
 * Helper data classes for PlaceholderUsers
 */
data class PlaceholderAddress(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: PlaceholderGeo
)

data class PlaceholderGeo(
    val lat: String,
    val lng: String
)

data class PlaceholderCompany(
    val name: String,
    val catchPhrase: String,
    val bs: String
)