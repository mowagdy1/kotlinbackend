package commons

import kotlin.Exception

class BadRequestException : Exception()  //400
class UnauthorizedException : Exception()  //401
class ForbiddenException : Exception()  //403
class NotFoundException : Exception()  //404
class ConflictException : Exception()  //409

