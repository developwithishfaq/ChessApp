package com.test.chessapp.presentation.boardScreen.states

data class BoardState(
    val selectedPiece: SelectedPiece? = null,
    val isWhiteTurn: Boolean = true,
)

data class SelectedPiece(val row: Int, val col: Int)

