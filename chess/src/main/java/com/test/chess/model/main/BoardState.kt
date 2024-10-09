package com.test.chess.model.main

import com.test.chess.helpers.ChessConfigs.getPieceAt
import com.test.chess.core.model.Piece

data class BoardState(
    val board: List<Array<Piece?>> = emptyList(),
    val eatenPieces: List<EatenPiece> = emptyList(),
    val selectedPiece: SelectedPiece? = null,
    val isWhiteTurn: Boolean = true,
)

data class EatenPiece(
    val row: Int,
    val col: Int,
    val piece: Piece,
)
data class SelectedPiece(val row: Int, val col: Int)

fun SelectedPiece.getPiece() = getPieceAt(row, col)


