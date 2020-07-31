package modules.user

class EmptyRequest

data class UserRegisterRequest(
        val name: String = "",
        val email: String = ""
)

data class UserUpdateRequest(
        val name: String = "",
        val email: String = ""
)
