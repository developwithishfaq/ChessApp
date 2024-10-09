package com.test.chess.core.model

data class Position(
    val row: Int, val col: Int
)

fun Position.getRowDifference(position: Position): Int {
    return row - position.row
}

fun Position.getColDifference(position: Position): Int {
    return col - position.col
}
