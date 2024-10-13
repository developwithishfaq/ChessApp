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
        board: List<List<Piece?>>,
        showLogs: Boolean = false
    ): Boolean
}

fun Piece?.isWhitePiece() = this?.isWhitePiece() ?: false