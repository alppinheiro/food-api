package app.config.app.web

import app.config.app.web.controllers.*
import app.config.config.Roles
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.security.SecurityUtil.roles
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import java.lang.Appendable

class Router(
    private val userController: UserController,
    private val profileController: ProfileController,
    private val articleControler: ArticleControler,
    private val commentController: CommentController,
    private val tagController: TagController
) : KoinComponent {

    fun register(app: Javalin){
        val rolesOptionalAuthenticated = roles(Roles.ANYONE, Roles.AUTHENTICATED)

        app.routes{
            path("users"){
                post(userController::register, roles(Roles.ANYONE))
                post("login", userController::login, roles(Roles.ANYONE))
            }
            path("user"){
                get(userController::getCurrent, roles(Roles.AUTHENTICATED))
                put(userController::update, roles(Roles.AUTHENTICATED))
            }
            path("profiles/:username"){
                get(profileController::get, rolesOptionalAuthenticated)
                path("follow"){
                    post(profileController::follow, roles(Roles.AUTHENTICATED))
                    delete(profileController::unfolloow, roles(Roles.AUTHENTICATED))
                }
            }
            path("articles"){
                get("feed", articleControler::feed, roles(Roles.AUTHENTICATED))
                path(":slug"){
                    path("comments"){
                        post(commentController::add, roles(Roles.AUTHENTICATED))
                        get(commentController::findBySlug, rolesOptionalAuthenticated)
                        delete(":id", commentController::delete, roles(Roles.AUTHENTICATED))
                    }
                    path("favorite"){
                        post(articleControler::favorite, roles(Roles.AUTHENTICATED))
                        delete(articleControler::unfavorite, roles(Roles.AUTHENTICATED))
                    }
                    get(articleControler::get, rolesOptionalAuthenticated)
                    put(articleControler::update, roles(Roles.AUTHENTICATED))
                    delete(articleControler::delete, roles(Roles.AUTHENTICATED))
                }
                get(articleControler::findBy, rolesOptionalAuthenticated)
                post(articleControler::create, roles(Roles.AUTHENTICATED))
            }
            path("tags"){
                get(tagController::get, rolesOptionalAuthenticated)
            }
        }
    }
}