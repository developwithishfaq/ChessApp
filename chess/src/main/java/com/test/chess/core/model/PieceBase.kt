package com.test.chess.core.model

import com.test.chess.helpers.GeneralRules
import com.test.chess.model.pieces.Knight
import com.test.chess.model.pieces.Queen
import com.test.chess.model.pieces.Rook

abstract class PieceBase(
    isWhitePiece: Boolean,
    name: String
) : Piece(isWhitePiece, name) {

    fun overAllChecks(from: Position, to: Position, board: List<Array<Piece?>>): Boolean {
        val piece = board[from.row][from.col]
        val routeIsClear = when (piece) {
            is Rook -> {
                GeneralRules.isRookPathClear(from, to, board)
            }

            is Queen -> {
                GeneralRules.isQueenPathClear(from, to, board)
            }

            is Knight -> {
                true
            }

            else -> {
                true
            }
        }
        return routeIsClear
    }
}