package com.test.chess.model.pieces

import com.test.chess.ChessConfigs.isAnyEnemyAhead
import com.test.chess.ChessConfigs.isAnyEnemyAheadInDiagonal
import com.test.chess.model.Piece
import com.test.chess.model.Position

class Pawn(isWhitePlayer: Boolean) : Piece(isWhitePlayer, "Pawn") {
    override fun canMove(from: Position, to: Position, board: Array<Array<Piece?>>): Boolean {
        return when {
            !isAnyEnemyAhead(from, board) && !isAnyEnemyAheadInDiagonal(from, board) -> true
            isAnyEnemyAhead(from, board) && !isAnyEnemyAheadInDiagonal(from, board) -> false
            isAnyEnemyAheadInDiagonal(from, board) -> true
            isAnyEnemyAhead(from, board) -> true
            else -> false
        }
    }
}
