package com.test.chess.helpers

import com.test.chess.core.model.Piece
import com.test.chess.core.model.Position
import com.test.chess.helpers.ChessExtension.isAnyEnemyAhead
import com.test.chess.helpers.ChessExtension.isAnyEnemyAheadInDiagonal
import com.test.chess.helpers.ChessExtension.isEnemyThere
import com.test.chess.model.pieces.getRowDistance
import com.test.chess.model.pieces.isBackwardMove
import com.test.chess.model.pieces.isDiagonalMove
import com.test.chess.model.pieces.isSameRowMove

object PawnHelper {
    fun isPawnMove(
        from: Position,
        to: Position,
        isWhite: Boolean,
        board: List<Array<Piece?>>
    ): Boolean {
        if (isBackwardMove(from, to) || isSameRowMove(from, to)) {
            ChessConfigs.logChess("Backward Or Same Row Move", isError = true)
            return false
        }

        val rowDistance = getRowDistance(fromRow = from.row, toRow = to.row)
        val allowedDistance = if (isWhite) {
            if (from.row == 1) 2 else 1
        } else {
            if (from.row == 6) 2 else 1
        }

        if (rowDistance > allowedDistance) {
            ChessConfigs.logChess("Row Distance Increasing, Distance: $rowDistance, allowed: $allowedDistance", isError = true)
            return false
        }

        val isDiagonal = isDiagonalMove(from, to)
        val isEnemy = isEnemyThere(to)

        return when {
            isDiagonal && isEnemy -> {
                ChessConfigs.logChess("isDiagonalMove=$isDiagonal, isEnemyThere=$isEnemy")
                true
            }
            !isDiagonal && !isAnyEnemyAhead(from) -> {
                ChessConfigs.logChess("No enemy ahead and not a diagonal move")
                true
            }
            isAnyEnemyAheadInDiagonal(from) && isEnemy -> {
                ChessConfigs.logChess("Enemy ahead in diagonal, isEnemyThere=$isEnemy")
                true
            }
            else -> false
        }
    }
}
