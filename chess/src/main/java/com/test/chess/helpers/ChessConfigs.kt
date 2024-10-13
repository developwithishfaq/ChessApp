package com.test.chess.helpers

import android.util.Log
import com.test.chess.core.model.Piece
import com.test.chess.core.model.Position
import com.test.chess.model.main.BoardState
import com.test.chess.model.main.EatenPiece
import com.test.chess.model.main.SelectedPiece
import com.test.chess.model.pieces.Bishop
import com.test.chess.model.pieces.King
import com.test.chess.model.pieces.Knight
import com.test.chess.model.pieces.Pawn
import com.test.chess.model.pieces.Queen
import com.test.chess.model.pieces.Rook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object ChessConfigs {

    val boardState = MutableStateFlow(BoardState())

    fun isWhitePlayer(): Boolean {
        return boardState.value.isWhiteTurn
    }

    fun initializeBoard() {
        val board = List(8) {
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
            it.copy(
                board = board.map { model ->
                    model.map { subModel ->
                        subModel
                    }
                }
            )
        }
    }


    fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int) {
        movePiece(Position(fromRow, fromCol), Position(toRow, toCol))
    }

    fun movePiece(from: Position, to: Position): Boolean {
        val board = boardState.value.board.arrayAbale()
        val piece = board[from.row][from.col] ?: return false // No piece at the starting position
        val canMove = piece.canMove(from = from, to = to, board = board, showLogs = true)
        logChess("Moving Piece:from=$from,to=$to,canMove:$canMove,piece=$piece", true)
        if (canMove) {
            board[to.row][to.col] = piece
            board[from.row][from.col] = null
            boardState.update {
                it.copy(
                    board = board,
                )
            }
        }
        return canMove
    }

    fun changeTurn() {
        boardState.update {
            it.copy(
                isWhiteTurn = boardState.value.isWhiteTurn.not()
            )
        }
    }

    fun getPieceAt(row: Int, col: Int): Piece? = try {
        boardState.value.board[row][col]
    } catch (_: Exception) {
        null
    }

    fun getPieceAt(position: Position): Piece? = try {
        getPieceAt(position.row, position.col)
    } catch (_: Exception) {
        null
    }

    fun getPiecesArray(): List<List<Piece?>> {
        return boardState.value.board
    }


    fun logChess(
        msg: String,
        enable: Boolean = false,
        tag: String = "chessLogs",
        isError: Boolean = false
    ) {
        if (enable) {
            if (isError) {
                Log.e(tag, "logChess=$msg")
            } else {
                Log.d(tag, "logChess=$msg")
            }
        }
    }

    fun selectPiece(row: Int, col: Int, isWhite: Boolean) {
        val piece = getPieceAt(row, col)!!
        // If they are same
        if (piece.isWhitePiece() == isWhite) {
            boardState.update {
                it.copy(
                    selectedPiece = SelectedPiece(
                        row = row,
                        col = col
                    )
                )
            }
        } else if (boardState.value.selectedPiece != null) {
            val selectedPiece = boardState.value.selectedPiece
            selectedPiece?.let {
                val from = Position(selectedPiece.row, selectedPiece.col)
                val to = Position(row, col)
                val canMove = movePiece(
                    from = from,
                    to = to,
                )
                if (canMove) {
                    val eatenPiece = boardState.value.eatenPieces.toMutableList()
                    eatenPiece.add(
                        EatenPiece(
                            row = to.row,
                            col = to.col,
                            piece = piece
                        )
                    )
                    boardState.update {
                        it.copy(
                            eatenPieces = eatenPiece
                        )
                    }
                }
            }
        }
    }

    fun unSelectPieces() {
        boardState.update {
            it.copy(
                selectedPiece = null
            )
        }
    }

    fun getSelectedPiece() = boardState.value.selectedPiece

}

