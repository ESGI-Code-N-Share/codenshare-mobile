import com.example.code_n_share_mobile.models.User

data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val birthdate: String
)

data class RegisterResponse(
    val data: String
)

data class UserIdResponse(
    val userId: String
)


data class LoginRequest(
    val email: String,
    val password: String,
    val stayLogin: Boolean
)

data class LoginResponse(
    val data: User
)

data class LogoutResponse(
    val message: String
)

data class VerifyEmailResponse(
    val message: String
)
