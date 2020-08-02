package base

import java.util.*
import kotlin.math.abs

//fun generateUniqueId(): String = UUID.randomUUID().toString().replace("-", "")
fun generateUniqueId(): Long = abs(UUID.randomUUID().mostSignificantBits)

class EmptyRequest
class EmptyResponse
