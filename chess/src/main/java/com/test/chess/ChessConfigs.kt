package com.test.chess

import com.test.chess.model.Piece
import com.test.chess.model.Position
import com.test.chess.model.pieces.Bishop
import com.test.chess.model.pieces.King
import com.test.chess.model.pieces.Knight
import com.test.chess.model.pieces.Pawn
import com.test.chess.model.pieces.Queen
import com.test.chess.model.pieces.Rook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object ChessConfigs {

    val boardState = MutableStateFlow(Array(8) { Array<Piece?>(8) { null } })

    fun initializeBoard() {
        val board = Array(8) {
            Array<Piece?>(8) {
                null
            }
        }
        // Add pawns
        for (i in 0..7) {
            board[1][i] = Pawn(true)  // White pawns
            board[6][i] = Pawn(false) // Black pawns
        }
        // Add rooks
        board[0][0] = Rook(true)
        board[0][7] = Rook(true)
        board[7][0] = Rook(false)
        board[7][7] = Rook(false)
        // Add knights
        board[0][1] = Knight(true)
        board[0][6] = Knight(true)
        board[7][1] = Knight(false)
        board[7][6] = Knight(false)
        // Add bishops
        board[0][2] = Bishop(true)
        board[0][5] = Bishop(true)
        board[7][2] = Bishop(false)
        board[7][5] = Bishop(false)
        // Add queens
        board[0][3] = Queen(true)   // White queen
        board[7][3] = Queen(false)  // Black queen
        // Add kings
        board[0][4] = King(true)    // White king
        board[7][4] = King(false)   // Black king

        boardState.update {
            board
        }
    }


    fun movePiece(from: Position, to: Position, board: Array<Array<Piece?>>) {
        val piece = board[from.row][from.col] ?: return // No piece at the starting position
        if (piece.canMove(from, to, board)) {
            board[to.row][to.col] = piece
            board[from.row][from.col] = null
            boardState.update {
                board
            }
        }
    }

    fun getPieceAt(row: Int, col: Int): Piece? = boardState.value[row][col]
    fun getPieces(): List<Array<Piece?>> {
        return boardState.value.toList()
    }

    fun getPiecesArray(): Array<Array<Piece?>> {
        return boardState.value
    }

    fun isAnyEnemyAhead(from: Position, board: Array<Array<Piece?>>): Boolean {
        return board[from.row + 1][from.col] != null
    }

    fun isAnyEnemyAheadInDiagonal(from: Position, board: Array<Array<Piece?>>): Boolean {
        return ChessConfigs.getPieceAt(from.row + 1, from.col - 1) != null ||
                ChessConfigs.getPieceAt(from.row + 1, from.col + 2) != null
    }

}

