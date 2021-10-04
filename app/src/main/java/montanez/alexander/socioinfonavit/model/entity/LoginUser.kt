package montanez.alexander.socioinfonavit.model.entity

data class LoginUser(
    val email: String,
    val password: String
)
data class LoginObject(
    val user: LoginUser
)
