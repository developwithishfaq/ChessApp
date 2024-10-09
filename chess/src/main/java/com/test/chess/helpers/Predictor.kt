package com.test.chess.helpers

import com.test.chess.core.model.Piece
import com.test.chess.core.model.Position

object Predictor {
    fun isNextPositionSafe(
        from: Position,
        to: Position,
        board: List<Array<Piece?>>
    ): List<Position> {
        ChessConfigs.logChess("Enemy Positions Called")
        val enemyPositions = mutableListOf<Position>()
        // Get the color of the piece moving
        val movingPiece = board[from.row][from.col]
        if (movingPiece == null) {
            ChessConfigs.logChess("Enemy Positions movingPiece=null, From=$from,To=$to")
            return emptyList()
        }
        val isWhitePiece = movingPiece.isWhitePiece()
        // Check all pieces on the board for possible attacks on the target position
        for (row in board.indices) {
            for (col in board[row].indices) {
                val piece = board[row][col]
                if (piece != null && piece.isWhitePiece() != isWhitePiece) {
                    // Check if this enemy piece can attack the target position
                    val enemyPosition = Position(row, col)
                    if (piece.canMove(enemyPosition, to, board)) {
                        enemyPositions.add(enemyPosition)
                    }
                }
            }
        }
        ChessConfigs.logChess("Enemy Positions: $enemyPositions")
        return enemyPositions
    }


    fun findNextMoves(
        from: Position,
        board: List<Array<Piece?>>
    ): List<Position> {
        val piece = board[from.row][from.col]
        val positions = mutableListOf<Position>()

        for (pieceRow in board.indices) {
            for (pieceCol in board[pieceRow].indices) {
                val to = Position(pieceRow, pieceCol)
                if (piece?.canMove(from, to, board) == true) {
                    positions.add(to)
                }
            }
        }
        ChessConfigs.logChess("Next Moves: $positions")
        return positions
    }

}