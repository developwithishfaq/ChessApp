package com.test.chess.helpers

import com.test.chess.core.model.Piece
import com.test.chess.core.model.Position
import kotlin.math.abs

object GeneralRules {
    fun isVerticalMove(from: Position, to: Position): Boolean {
        return from.col == to.col
    }

    fun isHorizontalMove(from: Position, to: Position): Boolean {
        return from.row == to.row
    }

    fun isRookMove(from: Position, to: Position): Boolean {
        return isVerticalMove(from, to) || isHorizontalMove(from, to)
    }

    fun isRookPathClear(from: Position, to: Position, board: List<Array<Piece?>>): Boolean {
        return if (isHorizontalMove(from, to)) {
            isPathClearHorizontally(from, to, board)
        } else if (isVerticalMove(from, to)) {
            isPathClearVertically(from, to, board)
        } else {
            false
        }
    }

    fun isQueenPathClear(from: Position, to: Position, board: List<Array<Piece?>>): Boolean {
        ChessConfigs.logChess("isQueenPathClear from=$from,to=$to")
        return if (isHorizontalMove(from, to) || isVerticalMove(from, to)) {
            isRookPathClear(from, to, board)
        } else if (isDiagonalMove(from, to)) {
            isThereAnyOneInDiagonal(from, to, board).not()
        } else {
            false
        }
    }

    fun isDiagonalMove(from: Position, to: Position): Boolean {
        return Math.abs(from.row - to.row) == Math.abs(from.col - to.col)
    }

    fun isThereAnyOneInDiagonal(
        from: Position,
        to: Position,
        board: List<Array<Piece?>>
    ): Boolean {
        return try {
            if (isDiagonalMove(from, to)) {
                val rowIncrement = if (to.row > from.row) 1 else -1
                val colIncrement = if (to.col > from.col) 1 else -1
                var currentRow = from.row + rowIncrement
                var currentCol = from.col + colIncrement
                while (currentRow != to.row && currentCol != to.col) {
                    if (board[currentRow][currentCol] != null) {
                        return true
                    }
                    currentRow += rowIncrement
                    currentCol += colIncrement
                }
                false // No pieces in between
            } else {
                false
            }
        } catch (_: Exception) {
            false
        }
    }

    fun isPathClearVertically(
        from: Position,
        to: Position,
        board: List<Array<Piece?>>
    ): Boolean {
        if (from.col != to.col) return false
        val direction = if (to.row > from.row) 1 else -1
        var currentRow = from.row + direction
        while (currentRow != to.row) {
            if (board[currentRow][from.col] != null) {
                return false // A piece is blocking the path
            }
            currentRow += direction
        }

        return true // No pieces are blocking the path
    }

    fun isPathClearHorizontally(
        from: Position,
        to: Position,
        board: List<Array<Piece?>>
    ): Boolean {
        if (from.row != to.row) return false
        val direction = if (to.col > from.col) 1 else -1
        var currentCol = from.col + direction
        while (currentCol != to.col) {
            if (board[from.row][currentCol] != null) {
                return false // A piece is blocking the path
            }
            currentCol += direction
        }
        return true
    }


    fun isFrontMoveOverall(from: Position, to: Position, isWhite: Boolean): Boolean {
        return isFrontDiagonalMove(from, to, isWhite) || isFrontVerticalMove(from, to, isWhite)
    }

    fun isFrontDiagonalMove(from: Position, to: Position, isWhite: Boolean): Boolean {
        return if (isDiagonalMove(from, to)) {
            if (isWhite) {
                from.row > to.row
            } else {
                from.row < to.row
            }
        } else {
            false
        }
    }

    fun isFrontVerticalMove(from: Position, to: Position, isWhite: Boolean): Boolean {
        return if (isVerticalMove(from, to)) {
            if (isWhite) {
                from.row > to.row
            } else {
                from.row < to.row
            }
        } else {
            false
        }
    }


    fun isValidKnightMove(from: Position, to: Position): Boolean {
        val rowDiff = abs(from.row - to.row)
        val colDiff = abs(from.col - to.col)
        // Knight moves in an "L" shape: (2, 1) or (1, 2)
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)
    }

    fun isKingMove(
        from: Position,
        to: Position,
        board: List<Array<Piece?>>,
        isWhite: Boolean
    ): Boolean {
        val canMove =
            isDiagonalMove(from, to) || isVerticalMove(from, to) || isHorizontalMove(from, to)
        if (canMove) {
            val dx = abs(from.row - to.row)
            val dy = abs(from.col - to.col)
            if (dx <= 1 && dy <= 1) {
                val destinationPiece = board[to.row][to.col]
                return if (destinationPiece == null) {
                    true
                } else {
                    isWhite && destinationPiece.isWhitePiece().not()
                }
            }
            return false
        } else {
            return false
        }
    }
}