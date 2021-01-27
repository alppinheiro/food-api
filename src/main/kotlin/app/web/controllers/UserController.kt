package app.config.app.web.controllers

import app.config.app.domain.UserDTO
import app.config.app.domain.service.UserService
import app.config.app.ext.isEmailValid
import io.javalin.http.Context


class UserController(private val userService: UserService) {

    fun login(ctx: Context){
        ctx.bodyValidator<UserDTO>()
            .check({ it.user?.email?.isEmailValid() ?: true })
            .check({ !it.user?.password.isNullOrBlank() })
            .get().user?.also { user ->
                userService.authenticate(user).apply{
                    ctx.json(UserDTO(this))
                }
            }
    }

    fun register(ctx: Context){
        ctx.bodyValidator<UserDTO>()
            .check({ it.user?.email?.isEmailValid() ?: true })
            .check({ !it.user?.password.isNullOrBlank() })
            .check({ !it.user?.username.isNullOrBlank() })
            .get().user?.also { user ->
                userService.create(user).apply {
                    ctx.json(UserDTO(this))
                }
            }
    }

    fun getCurrent(ctx: Context){

    }

    fun update(ctx: Context){

    }

}