package com.test.chess.core.model

abstract class Piece(
    private val isWhitePiece: Boolean,
    private val name: String
) {
    fun isWhitePiece() = isWhitePiece

    fun getName(): String {
        return name
    }

    abstract fun canMove(
        from: Position,
        to: Position,
        board: List<Array<Piece?>>
    ): Boolean
}