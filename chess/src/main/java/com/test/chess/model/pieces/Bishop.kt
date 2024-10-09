package com.test.chess.model.pieces

import com.test.chess.core.model.Piece
import com.test.chess.core.model.PieceBase
import com.test.chess.core.model.Position
import com.test.chess.helpers.GeneralRules

class Bishop(isWhitePlayer: Boolean) :
    PieceBase(isWhitePlayer, "Bishop") {

    override fun canMove(
        from: Position,
        to: Position,
        board: List<Array<Piece?>>
    ): Boolean {
        return if (overAllChecks(from, to, board)) {
            GeneralRules.isDiagonalMove(from, to)
        } else {
            false
        }
    }
}