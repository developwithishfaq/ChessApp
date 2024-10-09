package com.test.chess.model.pieces

import com.test.chess.core.model.Piece
import com.test.chess.core.model.PieceBase
import com.test.chess.core.model.Position
import com.test.chess.helpers.ChessConfigs
import com.test.chess.helpers.PawnHelper

class Pawn(private val isWhitePlayer: Boolean) : PieceBase(isWhitePlayer, "Pawn") {
    override fun canMove(from: Position, to: Position, board: List<Array<Piece?>>): Boolean {
        if (overAllChecks(from, to, board)) {
            return PawnHelper.isPawnMove(from, to, isWhitePlayer, board)
        } else {
            return false
        }
    }
}

fun getRowDistance(fromRow: Int, toRow: Int): Int {
    return if (ChessConfigs.isWhitePlayer()) {
        toRow - fromRow
    } else {
        fromRow - toRow
    }
}

fun getColumnDistance(fromCol: Int, toCol: Int): Int {
    return if (ChessConfigs.isWhitePlayer()) {
        toCol - fromCol
    } else {
        fromCol - toCol
    }
}

fun isBackwardMove(from: Position, to: Position): Boolean {
    return if (ChessConfigs.isWhitePlayer()) {
        to.row < from.row
    } else {
        from.row < to.row
    }
}

fun isSameRowMove(from: Position, to: Position): Boolean {
    return to.row == from.row
}

fun isDiagonalMove(from: Position, to: Position): Boolean {
    return if (ChessConfigs.isWhitePlayer()) {
        (from.row < to.row) && (from.col > to.col || from.col < to.col)
    } else {
        (from.row > to.row) && (from.col > to.col || from.col < to.col)
    }
}