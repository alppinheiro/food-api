package app.config.app.domain.service

import app.config.app.domain.User
import app.config.app.domain.repository.UserRepository
import app.config.app.utils.Cipher
import app.config.app.utils.JwtProvider
import app.config.config.Roles
import io.javalin.http.HttpResponseException
import io.javalin.http.UnauthorizedResponse
import org.eclipse.jetty.http.HttpStatus
import java.util.*

class UserService(private val jwtProvider: JwtProvider, private val userRepository: UserRepository) {

    private val base64Encoder = Base64.getEncoder()

    fun create(user: User): User {
        userRepository.findByEmail(user.email).takeIf { it != null }?.apply {
            throw HttpResponseException(HttpStatus.BAD_REQUEST_400,
                "Email already registered!")
        }
        userRepository.create(user.copy(password = String(base64Encoder.encode(Cipher.encrypt(user.password)))))
        return user.copy(token = generateJwtToken(user))
    }

    fun authenticate(user: User): User {
        val userFound = userRepository.findByEmail(user.email)
        if (userFound?.password == String(base64Encoder.encode(Cipher.encrypt(user.password)))) {
            return userFound.copy(token = generateJwtToken(userFound))
        }
        throw UnauthorizedResponse("email or password invalid!")
    }

    private fun generateJwtToken(user: User): String? {
        return jwtProvider.createJWT(user, Roles.AUTHENTICATED)
    }
}