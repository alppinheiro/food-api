package app.config.app.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

class DbConfig(jdbcUrl: String, username: String, password: String) {
    private val dataSource: DataSource

    init {
        dataSource = HikariConfig().let {
            hikariConfig ->
            hikariConfig.jdbcUrl = jdbcUrl
            hikariConfig.username = username
            hikariConfig.password = password
            HikariDataSource(hikariConfig)
        }
    }

    fun getDataSource(): DataSource {
        return dataSource
    }
}