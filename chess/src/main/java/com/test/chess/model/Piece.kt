package com.test.chess.model

import com.test.chess.ChessConfigs

abstract class Piece(
    private val isWhitePiece: Boolean,
    private val name: String
) {

    fun getName(): String {
        return name
    }

    abstract fun canMove(
        from: Position,
        to: Position,
        board: Array<Array<Piece?>>
    ): Boolean
}