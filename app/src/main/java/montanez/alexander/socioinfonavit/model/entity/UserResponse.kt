package montanez.alexander.socioinfonavit.model.entity

data class UserResponse(
    val id: Int?,
    val email: String?,
    val role: String?,
    val error: String?,
    var token: String?
)
