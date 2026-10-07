package ants.storage

import com.fasterxml.jackson.databind.ObjectMapper

class Path : Entity<ants.external.Path> {
    constructor() : super()

    constructor(mapper: ObjectMapper, hash: String, data: ants.external.Path) : super(mapper, hash, data)

    fun getObject(mapper: ObjectMapper): ants.external.Path =
        super.getObject(mapper, ants.external.Path::class.java) as ants.external.Path
}
