package montanez.alexander.socioinfonavit.model.entity

class LoginError(userMessage: String) : Exception(userMessage)
class LogoutError(userMessage: String) : Exception(userMessage)
class EmptyInputException : Exception()
class SessionExpiredException : Exception()
class UnknownException(userMessage: String) : Exception(userMessage)