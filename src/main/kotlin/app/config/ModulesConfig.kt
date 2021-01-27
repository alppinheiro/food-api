package app.config.app.config

import app.config.app.domain.repository.UserRepository
import app.config.app.domain.service.UserService
import app.config.app.utils.JwtProvider
import app.config.app.web.Router
import app.config.app.web.controllers.*
import app.config.config.AppConfig
import app.config.config.AuthConfig
import org.koin.dsl.module.module

object ModulesConfig {
    private val configModule = module {
        single { AppConfig() }
        single { JwtProvider() }
        single { AuthConfig(get()) }
        single {
            DbConfig(getProperty("jdbc.url"), getProperty("db.username"), getProperty("db.password")).getDataSource()
        }
        single { Router(get(),get(),get(), get(), get()) }
    }

    private val userModule = module {
        single { UserController(get()) }
        single { UserService(get(),get()) }
        single { UserRepository(get()) }
    }

    private val articleControler = module {
        single { ArticleControler() }
    }

    private val profileController = module {
        single { ProfileController() }
    }

    private val commentController = module {
        single { CommentController() }
    }

    private val tagModule = module {
        single { TagController() }
    }

    internal val allModules =
        listOf(ModulesConfig.configModule, ModulesConfig.userModule, ModulesConfig.articleControler,
        ModulesConfig.commentController, ModulesConfig.tagModule, ModulesConfig.profileController)
}