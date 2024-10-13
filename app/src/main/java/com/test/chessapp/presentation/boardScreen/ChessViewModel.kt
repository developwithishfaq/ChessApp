package com.test.chessapp.presentation.boardScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.chess.core.model.Position
import com.test.chess.helpers.ChessConfigs
import com.test.chess.helpers.ChessConfigs.initializeBoard
import com.test.chess.helpers.GeneralRules
import com.test.chess.helpers.Predictor.isNextPositionSafe
import com.test.chess.model.main.BoardState
import com.test.chess.model.pieces.King
import com.test.chessapp.presentation.boardScreen.events.ChessEvents
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ChessViewModel : ViewModel() {

    val boardState = ChessConfigs.boardState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        BoardState()
    )

    //
    init {
        initializeBoard()
    }

    fun onEvent(events: ChessEvents) {
        when (events) {
            is ChessEvents.SelectPiece -> {
                val piece = ChessConfigs.getPieceAt(row = events.row, col = events.col)
                if (piece != null) {
                    ChessConfigs.selectPiece(events.row, events.col, boardState.value.isWhiteTurn)
                } else if (ChessConfigs.getSelectedPiece() != null) {
                    onEvent(ChessEvents.MovePiece(col = events.col, row = events.row))
                }
            }

            is ChessEvents.MovePiece -> {
                with(boardState.value) {
                    if (selectedPiece != null) {
                        val from = Position(selectedPiece!!.row, selectedPiece!!.col)
                        val boardCopy = boardState.value.board.toMutableList()
                        val to = Position(events.row, events.col)
                        val piece = ChessConfigs.getPieceAt(from)
                        val canMove = if (piece is King) {
                            val enemyPositions =
                                isNextPositionSafe(from = from, to = to, board = boardCopy)
                            if (enemyPositions.isEmpty()) {
                                ChessConfigs.logChess("King Cannot Move")
                            }
                            enemyPositions.isEmpty()
                        } else {
                            true
                        }
                        if (canMove) {
                            val isPawn =
                                ChessConfigs.getPieceAt(from)?.canMove(from, to, board) ?: false
                            if (isPawn) {
                                if (
                                    GeneralRules.isInEnemyLine(
                                        isWhite = boardState.value.isWhiteTurn,
                                        position = to
                                    )
                                ) {

                                }
//                                val moved = ChessConfigs.movePiece(from = from, to = to)
//                                if (moved) {
//
//                                }
                            } else {
                                val moved = ChessConfigs.movePiece(from = from, to = to)
                                if (moved) {
                                    ChessConfigs.changeTurn()
                                    ChessConfigs.unSelectPieces()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}