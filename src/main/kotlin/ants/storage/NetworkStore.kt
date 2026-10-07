package ants.storage

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param

interface NetworkStore : MongoRepository<Network, String> {
    @Query("SELECT n from Network n WHERE hash = :hash")
    fun findAllForHash(@Param("hash") hash: String): List<Network>
}
