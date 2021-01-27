package app.config.app.domain.repository

import app.config.app.domain.User
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

internal object Users : LongIdTable() {
    val email: Column<String> = varchar("email", 200).uniqueIndex()
    val username: Column<String> = varchar("username", 100).uniqueIndex()
    val password: Column<String> = varchar("password", 150)
    val bio: Column<String?> = varchar("bio", 1000).nullable()
    val image: Column<String?> = varchar("image", 255).nullable()

    fun toDomain(row: ResultRow): User {
        return User(
            id = row[Users.id].value,
            email = row[Users.email],
            username = row[Users.username],
            password = row[Users.password],
            bio = row[Users.bio],
            image = row[Users.image]
        )
    }

    internal object Follows : Table() {
        val user: Column<Long> = long("user").primaryKey()
        val follower: Column<Long> = long("user_follower").primaryKey()
    }
}

class UserRepository(private val dataSource: DataSource) {

    init {
        transaction(Database.connect(dataSource)){
            SchemaUtils.create(Users)
            SchemaUtils.create(Users.Follows)
        }
    }

    fun create(user: User): Long? {
        return transaction(Database.connect(dataSource)) {
            Users.insertAndGetId { row ->
                row[Users.email] = user.email
                row[Users.username] = user.username!!
                row[Users.password] = user.password!!
                row[Users.bio] = user.bio
                row[Users.image] = user.image
            }.value
        }
    }

    fun findByEmail(email: String): User? {
        return transaction(Database.connect(dataSource)) {
            Users.select { Users.email eq email }
                .map { Users.toDomain(it) }
                .firstOrNull()
        }
    }
}