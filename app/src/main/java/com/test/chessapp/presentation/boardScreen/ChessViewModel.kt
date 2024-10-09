package com.test.chessapp.presentation.boardScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.chess.core.model.Position
import com.test.chess.helpers.ChessConfigs
import com.test.chess.helpers.ChessConfigs.initializeBoard
import com.test.chess.helpers.Predictor.isNextPositionSafe
import com.test.chess.model.main.BoardState
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
                ChessConfigs.logChess("Event=Row:${events.row},Col:${events.col}, Piece Info=Name=${piece?.getName()},White=${piece?.isWhitePiece()}")
                if (piece != null) {
                    ChessConfigs.selectPiece(events.row, events.col)
                } else if (ChessConfigs.getSelectedPiece() != null) {
                    onEvent(ChessEvents.MovePiece(col = events.col, row = events.row))
                }
            }

            is ChessEvents.MovePiece -> {
                ChessConfigs.logChess("MovePiece Event=To:${events.row}*{${events.col}}")
                with(boardState.value) {
                    if (selectedPiece != null) {
                        val from = Position(selectedPiece!!.row, selectedPiece!!.col)
                        val boardCopy = boardState.value.board.toMutableList()
                        val to = Position(events.row, events.col)
                        isNextPositionSafe(from = from, to = to, board = boardCopy)
                        val moved = ChessConfigs.movePiece(from = from, to = to)
                        if (moved) {
                            ChessConfigs.unSelectPieces()
                        }
                    }
                }
            }
        }
    }

}