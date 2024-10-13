package com.test.chess.core.model

import com.test.chess.helpers.ChessConfigs
import com.test.chess.helpers.ChessExtension.findKingPosition
import com.test.chess.helpers.GeneralRules
import com.test.chess.helpers.arrayAbale
import com.test.chess.model.pieces.Bishop
import com.test.chess.model.pieces.King
import com.test.chess.model.pieces.Knight
import com.test.chess.model.pieces.Queen
import com.test.chess.model.pieces.Rook
import kotlinx.coroutines.channels.ticker

abstract class PieceBase(
    isWhitePiece: Boolean,
    name: String
) : Piece(isWhitePiece, name) {

    fun overAllChecks(
        from: Position,
        to: Position,
        board: List<List<Piece?>>,
        showLogs: Boolean = false
    ): Boolean {
        val piece = board[from.row][from.col] ?: return false
        val routeIsClear = when (piece) {
            is Rook -> {
                GeneralRules.isRookPathClear(from, to, board)
            }

            is Queen -> {
                GeneralRules.isQueenPathClear(from, to, board)
            }

            is Bishop -> {
                GeneralRules.isThereAnyOneInDiagonal(from, to, board).not()
            }

            is Knight -> {
                true
            }

            else -> {
                true
            }
        }

        val isWhite = piece.isWhitePiece()
        // Find the position of the king of the current player
        val kingPosition = if (this is King) {
            to
        }else{
            findKingPosition(isWhite, board) ?: return false
        }
        // Simulate the move by creating a new board with the piece moved from 'from' to 'to'
        val newBoard = board.arrayAbale()
        newBoard[to.row][to.col] = newBoard[from.row][from.col]
        newBoard[from.row][from.col] = null

        // Check if the king is still in check after the move
        val kingIsInCheckAfterMove = isKingInCheck(
            kingPosition = kingPosition,
            isWhite = isWhite,
            board = newBoard,
            showLogs = showLogs
        )
        val overAll = routeIsClear && kingIsInCheckAfterMove.not()
        if (overAll.not()) {
            ChessConfigs.logChess(
                msg = "routeIsClear=$routeIsClear,kingIsInCheckAfterMove=$kingIsInCheckAfterMove,kingPosition=$kingPosition,isWhite=$isWhite",
                enable = showLogs,
                isError = true
            )
        } else {
            ChessConfigs.logChess(
                msg = "No Issue In Overall Checks routeIsClear and no checks",
                enable = showLogs
            )
        }
        return overAll
    }

    private fun isKingInCheck(
        kingPosition: Position,
        isWhite: Boolean,
        board: List<List<Piece?>>,
        showLogs: Boolean
    ): Boolean {
        for (row in board.indices) {
            for (col in board[row].indices) {
                val piece = board[row][col]
                // If there is an opponent piece that can move to the king's position, it's a check
                if (piece != null && piece.isWhitePiece() != isWhite) {
                    val newPosition = Position(row, col)
                    if (piece.canMove(newPosition, kingPosition, board)) {
                        ChessConfigs.logChess(
                            msg = "isKingInCheck Because $piece can move, newPosition=$newPosition,kingPosition=$kingPosition",
                            enable = showLogs
                        )
                        return true
                    }
                }
            }
        }
        return false
    }

    fun logData(showLogs: Boolean) {
        if (showLogs) {
            ChessConfigs.logChess("Piece = ${getName()},isWhite=${isWhitePiece()}", true)
        }
    }
}