package modules.user

data class UserListingResponse(
        val _id: String,
        val name: String,
        val email: String
)

data class SingleUserResponse(
        val _id: String,
        val name: String,
        val email: String
)

