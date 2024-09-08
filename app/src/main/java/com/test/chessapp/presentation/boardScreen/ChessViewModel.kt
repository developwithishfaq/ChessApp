package com.test.chessapp.presentation.boardScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.chess.ChessConfigs
import com.test.chess.ChessConfigs.initializeBoard
import com.test.chess.model.Position
import com.test.chess.model.pieces.Pawn
import com.test.chessapp.presentation.boardScreen.events.ChessEvents
import com.test.chessapp.presentation.boardScreen.states.BoardState
import com.test.chessapp.presentation.boardScreen.states.SelectedPiece
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ChessViewModel : ViewModel() {

    val boardState = ChessConfigs.boardState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyArray()
    )
    private val _state = MutableStateFlow(BoardState())
    val state = _state.asStateFlow()

    init {
        initializeBoard()
    }

    fun onEvent(events: ChessEvents) {
        when (events) {
            is ChessEvents.SelectPiece -> {
                if (ChessConfigs.getPieceAt(events.row, events.col) != null) {
                    _state.update {
                        it.copy(
                            selectedPiece = SelectedPiece(
                                row = events.row,
                                col = events.col
                            )
                        )
                    }
                } else if (state.value.selectedPiece != null) {
                    onEvent(ChessEvents.MovePiece(events.row, events.col))
                }
            }

            is ChessEvents.MovePiece -> {
                with(state.value) {
                    if (selectedPiece != null) {
                        val piece = ChessConfigs.getPieceAt(
                            selectedPiece.row,
                            selectedPiece.col,
                        )
                        if (piece is Pawn) {
                            val canMove = piece.canMove(
                                from = Position(selectedPiece.row, selectedPiece.col),
                                to = Position(events.row, events.col),
                                board = ChessConfigs.getPiecesArray()
                            )
                            if (canMove) {
                                ChessConfigs.movePiece(
                                    from = Position(selectedPiece.row, selectedPiece.col),
                                    to = Position(events.row, events.col),
                                    board = ChessConfigs.getPiecesArray()
                                )
                                _state.update {
                                    it.copy(
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

}