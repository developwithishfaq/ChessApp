package com.test.chess.model.pieces

import com.test.chess.core.model.Piece
import com.test.chess.core.model.PieceBase
import com.test.chess.core.model.Position
import com.test.chess.helpers.GeneralRules

class Queen(isWhitePlayer: Boolean) : PieceBase(isWhitePlayer, "Queen") {
    override fun canMove(
        from: Position,
        to: Position,
        board: List<Array<Piece?>>
    ): Boolean {
        return if (overAllChecks(from, to, board)) {
            GeneralRules.isDiagonalMove(from, to) || GeneralRules.isRookMove(
                from,
                to
            )
        } else {
            false
        }
    }
}