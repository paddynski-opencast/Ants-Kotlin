package ants.storage

import com.fasterxml.jackson.databind.ObjectMapper

class Network : Entity<ants.external.Network> {
    constructor() : super()

    constructor(mapper: ObjectMapper, hash: String, data: ants.external.Network) : super(mapper, hash, ants.external.Network(data))

    fun getObject(mapper: ObjectMapper): ants.external.Network =
        super.getObject(mapper, ants.external.Network::class.java) as ants.external.Network
}
