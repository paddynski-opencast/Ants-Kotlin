package ants.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = """
  Optimization method.

  One of:

    CS => Min cost then pick the shortest

    CL => Min cost then pick the longest

    SC => Shortest then pick the path with min cost

    LC => Longest then pick the path with min cost

    ES => Max cost then pick the shortest

    EL => Max cost then pick the longest

    SE => Shortest then pick the path with max cost

    LE => Longest then pick the path with max cost

""")
enum class Optimization {
  CS,
  CL,
  SC,
  LC,
  ES,
  EL,
  SE,
  LE
}