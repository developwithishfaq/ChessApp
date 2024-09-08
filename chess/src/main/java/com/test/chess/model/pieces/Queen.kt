package com.test.chess.model.pieces

import com.test.chess.model.Piece
import com.test.chess.model.Position

class Queen(isWhitePlayer: Boolean) : Piece(isWhitePlayer, "Queen") {
    override fun canMove(
        from: Position,
        to: Position,
        board: Array<Array<Piece?>>
    ): Boolean {
        // Implement movement logic
        return true
    }
}