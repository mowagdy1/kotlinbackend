package components.user

data class RegisterRequest(val userName: String,
                           val password: String)


data class User(
        val userName: String,
        val password: String,
        val email: String = ""
)
