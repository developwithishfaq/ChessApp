package com.test.chess.model.pieces

import com.test.chess.core.model.Piece
import com.test.chess.core.model.PieceBase
import com.test.chess.core.model.Position
import com.test.chess.helpers.GeneralRules

class Rook(isWhitePlayer: Boolean) : PieceBase(
    isWhitePlayer,
    "Rook"
) {
    override fun canMove(
        from: Position,
        to: Position,
        board: List<Array<Piece?>>
    ): Boolean {
        // Implement movement logic
        return if (overAllChecks(from, to, board)) {
            GeneralRules.isHorizontalMove(from, to) || GeneralRules.isVerticalMove(from, to)
        } else {
            false
        }
    }
}