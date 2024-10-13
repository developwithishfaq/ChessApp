package com.test.chess.helpers

import com.test.chess.core.model.Piece
import com.test.chess.core.model.Position
import com.test.chess.helpers.ChessConfigs.boardState
import com.test.chess.helpers.ChessConfigs.getPieceAt
import com.test.chess.helpers.ChessConfigs.isWhitePlayer
import com.test.chess.model.pieces.King

object ChessExtension {

    fun findKingPosition(isWhite: Boolean, board: List<List<Piece?>>): Position? {
        for (row in board.indices) {
            for (col in board[row].indices) {
                val piece = board[row][col]
                if (piece is King && piece.isWhitePiece() == isWhite) {
                    return Position(row, col)
                }
            }
        }
        return null
    }


    fun isAnyEnemyAhead(from: Position): Boolean {
        return try {
            val piece = if (isWhitePlayer()) {
                boardState.value.board[from.row + 1][from.col]
            } else {
                boardState.value.board[from.row - 1][from.col]
            }
            when {
                piece?.isWhitePiece() == true && ChessConfigs.isWhitePlayer() -> {
                    return false
                }

                ChessConfigs.isWhitePlayer() && piece?.isWhitePiece()?.not() == true -> {
                    return true
                }
            }
            piece != null
        } catch (_: Exception) {
            false
        }
    }

    fun isEnemyThere(to: Position): Boolean {
        val enemy = ChessConfigs.getPieceAt(to.row, to.col)
        return enemy.isEnemyPiece()
    }

    fun areAbsoluteValuesEqual(value1: Int, value2: Int): Boolean {
        return Math.abs(value1) == Math.abs(value2)
    }

    private fun Piece?.isEnemyPiece(): Boolean {
        return if (this == null) {
            false
        } else {
            if (ChessConfigs.isWhitePlayer() && this.isWhitePiece()) {
                false
            } else if (ChessConfigs.isWhitePlayer().not() && this.isWhitePiece().not() == true) {
                false
            } else {
                true
            }
        }
    }

    fun isAnyEnemyAheadInDiagonal(from: Position): Boolean {
        val isWhitePlayer = ChessConfigs.isWhitePlayer()

        val pieceOne = if (isWhitePlayer) {
            getPieceAt(from.row + 1, from.col - 1)
        } else {
            getPieceAt(from.row - 1, from.col + 1)
        }
        val pieceTwo = if (isWhitePlayer) {
            getPieceAt(from.row + 1, from.col + 2)
        } else {
            getPieceAt(from.row - 1, from.col - 2)
        }
        return if (isWhitePlayer) {
            pieceTwo?.isWhitePiece()?.not() == true || pieceOne?.isWhitePiece()
                ?.not() == true
        } else {
            pieceTwo?.isWhitePiece() == true || pieceOne?.isWhitePiece() == true
        }
    }
}