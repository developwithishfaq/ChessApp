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
                board = board
            )
        }
    }


    fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int) {
        movePiece(Position(fromRow, fromCol), Position(toRow, toCol))
    }

    fun movePiece(from: Position, to: Position): Boolean {
        logChess("--------------Moving Piece-----------------")
        logChess("From=$from")
        logChess("To=$to")
        val board = boardState.value.board.toMutableList()
        val piece = board[from.row][from.col] ?: return false // No piece at the starting position
        val canMove = piece.canMove(from, to, board)
        logChess("Name=${piece.getName()}")
        logChess("Can Move = $canMove", isError = canMove.not())
        if (canMove) {
            board[to.row][to.col] = piece
            board[from.row][from.col] = null
            boardState.update {
                it.copy(
                    board = board,
                    isWhiteTurn = boardState.value.isWhiteTurn.not()
                )
            }
        }
        return canMove
    }

    fun getPieceAt(row: Int, col: Int): Piece? = try {
        boardState.value.board[row][col]
    } catch (_: Exception) {
        null
    }

    fun getPiecesArray(): List<Array<Piece?>> {
        return boardState.value.board
    }


    fun logChess(msg: String, tag: String = "chessLogs", isError: Boolean = false) {
        if (isError) {
            Log.e(tag, "logChess=$msg")
        } else {
            Log.d(tag, "logChess=$msg")
        }
    }

    fun selectPiece(row: Int, col: Int) {
        val piece = getPieceAt(row, col)
        val pieceAvailable = piece != null
        if (pieceAvailable) {
            if (piece?.isWhitePiece() == true && ChessConfigs.isWhitePlayer()) {
                boardState.update {
                    it.copy(
                        selectedPiece = SelectedPiece(
                            row = row,
                            col = col
                        )
                    )
                }
            } else if (piece?.isWhitePiece()?.not() == true && ChessConfigs.isWhitePlayer().not()) {
                boardState.update {
                    it.copy(
                        selectedPiece = SelectedPiece(
                            row = row,
                            col = col
                        )
                    )
                }
            } else if (boardState.value.selectedPiece != null) {
                if (ChessConfigs.isWhitePlayer()) {
                    val selectedPiece = boardState.value.selectedPiece
                    if (piece?.isWhitePiece()?.not() == true) {
                        selectedPiece?.let {
                            val board = boardState.value.board.toMutableList()
                            val canMove = movePiece(
                                Position(
                                    row = selectedPiece.row,
                                    col = selectedPiece.col
                                ),
                                Position(
                                    row = row,
                                    col = col
                                )
                            )
                            if (canMove) {
                                val eatenPiece = boardState.value.eatenPieces.toMutableList()
                                board[selectedPiece.row][selectedPiece.col] = null
                                eatenPiece.add(
                                    EatenPiece(
                                        selectedPiece.row,
                                        selectedPiece.col,
                                        piece
                                    )
                                )
                                ChessConfigs.logChess("White Eaten Black ,Is White=${boardState.value.isWhiteTurn}")
                                boardState.update {
                                    it.copy(
                                        board = board,
                                        eatenPieces = eatenPiece,
                                        selectedPiece = null
                                    )
                                }
                            }
                        }
                    }
                } else if (ChessConfigs.isWhitePlayer().not()) {
                    val selectedPiece = boardState.value.selectedPiece
                    if (piece?.isWhitePiece() == true) {
                        selectedPiece?.let {
                            val board = boardState.value.board.toMutableList()
                            val eatenPiece = boardState.value.eatenPieces.toMutableList()
                            val canMove = movePiece(
                                Position(
                                    row = selectedPiece.row,
                                    col = selectedPiece.col
                                ),
                                Position(
                                    row = row,
                                    col = col
                                )
                            )
                            if (canMove) {
                                board[selectedPiece.row][selectedPiece.col] = null
                                eatenPiece.add(
                                    EatenPiece(
                                        selectedPiece.row,
                                        selectedPiece.col,
                                        piece
                                    )
                                )
                                ChessConfigs.logChess("Black Eaten White ,Is White=${boardState.value.isWhiteTurn}")
                                boardState.update {
                                    it.copy(
                                        board = board,
                                        eatenPieces = eatenPiece,
                                        selectedPiece = null
                                    )
                                }
                            }
                        }
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

