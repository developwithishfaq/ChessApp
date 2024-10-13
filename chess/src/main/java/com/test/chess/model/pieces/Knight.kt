package com.test.chess.model.pieces

import com.test.chess.core.model.Piece
import com.test.chess.core.model.PieceBase
import com.test.chess.core.model.Position
import com.test.chess.helpers.GeneralRules.isValidKnightMove

class Knight(isWhitePlayer: Boolean) :
    PieceBase(isWhitePlayer, "Knight") {
    override fun canMove(
        from: Position,
        to: Position,
        board: List<List<Piece?>>,
        showLogs: Boolean
    ): Boolean {
        return overAllChecks(from, to, board) && isValidKnightMove(from, to)
    }
}