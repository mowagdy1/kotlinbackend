package modules.user

data class User(
        val _id: Long = 0L,
        val name: String = "",
        val email: String = "",
        val createdAt: Long = 0L)
