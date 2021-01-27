package app.config.config

import app.config.app.config.ModulesConfig
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import app.config.app.web.ErrorExceptionMapping
import app.config.app.web.Router
import io.javalin.Javalin
import io.javalin.plugin.json.JavalinJackson
import org.eclipse.jetty.server.Server
import org.koin.core.KoinProperties
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.koin.standalone.getProperty
import org.koin.standalone.inject
import java.text.SimpleDateFormat

class AppConfig : KoinComponent {

    private val authConfig: AuthConfig by inject()
    private val router: Router by inject()

    fun setup(): Javalin{

        StandAloneContext.startKoin(
            ModulesConfig.allModules,
            KoinProperties(true,true)
        )

        this.configureMapper()

        val app = Javalin.create { config ->
            config.apply {
                enableWebjars()
                enableCorsForAllOrigins()
                contextPath = getProperty("context")
                addStaticFiles("/swagger")
                addSinglePageRoot("","/swagger/swagger-ui.html")
                server{
                    Server(getProperty("server_port") as Int)
                }
            }
        }.events{
            it.serverStopping{
                StandAloneContext.stopKoin()
            }
        }
        authConfig.configure(app)
        router.register(app)
        ErrorExceptionMapping.register(app)
        return  app
    }


    private fun configureMapper() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        JavalinJackson.configure(
            jacksonObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setDateFormat(dateFormat)
                .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
        )
    }
}