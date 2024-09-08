package com.test.chess.model.pieces

import com.test.chess.model.Piece
import com.test.chess.model.Position

class Bishop(isWhitePlayer: Boolean) :
    Piece(isWhitePlayer, "Bishop") {

    override fun canMove(
        from: Position,
        to: Position,
        board: Array<Array<Piece?>>
    ): Boolean {
        // Implement movement logic
        return true
    }
}