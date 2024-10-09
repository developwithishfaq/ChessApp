package com.test.chess.helpers

import com.test.chess.core.model.Position
import com.test.chess.core.model.getColDifference
import com.test.chess.core.model.getRowDifference
import com.test.chess.helpers.GeneralRules.isDiagonalMove
import com.test.chess.helpers.GeneralRules.isHorizontalMove
import com.test.chess.helpers.GeneralRules.isVerticalMove

object PiecesHelpers {


    fun isRookMove(from: Position, to: Position): Boolean {
        return isVerticalMove(from, to) || isHorizontalMove(from, to)
    }

    fun isBishopMove(from: Position, to: Position): Boolean {
        return isDiagonalMove(from, to)
    }

    fun isQueenMove(from: Position, to: Position): Boolean {
        return isBishopMove(from, to) || isRookMove(from, to)
    }

    fun isKnightMove(from: Position, to: Position): Boolean {
        return if (isDiagonalMove(from, to).not() && isHorizontalMove(from, to).not()) {
            val rowDiff = from.getRowDifference(to)
            val colDiff = from.getColDifference(to)
            rowDiff in (-2..2) && colDiff in (-2..2)
        } else {
            false
        }
    }

    fun isKingMove(from: Position, to: Position): Boolean {
        return if (isVerticalMove(from, to)) {
            from.getRowDifference(to) == 1 && from.getColDifference(to) == 0
        } else if (isHorizontalMove(from, to)) {
            from.getRowDifference(to) == 0 && from.getColDifference(to) == 1
        } else if (isDiagonalMove(from, to)) {
            from.getColDifference(to) == 1 && from.getRowDifference(to) == 1
        } else {
            return false
        }
    }

}