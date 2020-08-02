package modules.user

data class UserListingResponse(
        val _id: Long,
        val name: String,
        val email: String
)

data class SingleUserResponse(
        val _id: Long,
        val name: String,
        val email: String
)
