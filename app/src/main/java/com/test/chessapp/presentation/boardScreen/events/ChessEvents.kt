package com.test.chessapp.presentation.boardScreen.events

sealed class ChessEvents {

    data class SelectPiece(val col: Int, val row: Int) : ChessEvents()
    data class MovePiece(val col: Int, val row: Int) : ChessEvents()

}