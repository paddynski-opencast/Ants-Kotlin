package ants.storage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param

interface PathStore : MongoRepository<Path, String> {
    @Query("SELECT p from Path p WHERE hash = :hash")
    fun findAllForHash(@Param("hash") hash: String): List<Path>
}
