package ants

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
open class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<Main>(*args)
        }
    }
}
