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
        board: List<List<Piece?>>,
        showLogs: Boolean
    ): Boolean {
        if (isBackwardMove(from, to, isWhite) || isSameRowMove(from, to)) {
            ChessConfigs.logChess(
                msg = "isBackwardMove=${
                    isBackwardMove(
                        from,
                        to,
                        isWhite
                    )
                },isSameRowMove=${isSameRowMove(from, to)}",
                enable = showLogs
            )
            return false
        }

        val rowDistance = getRowDistance(
            fromRow = from.row, toRow = to.row,
            isWhitePlayer = isWhite
        )
        val allowedDistance = if (isWhite) {
            if (from.row == 1) 2 else 1
        } else {
            if (from.row == 6) 2 else 1
        }

        if (rowDistance > allowedDistance) {
            ChessConfigs.logChess(
                msg = "rowDistance > allowedDistance=$rowDistance > $allowedDistance",
                enable = showLogs
            )
            return false
        }

        val isDiagonal = isDiagonalMove(from, to)
        val isEnemy = isEnemyThere(to)

        val canMove = when {
            isDiagonal && isEnemy -> {
                /*
                ChessConfigs.logChess(
                    msg = "isDiagonal && isEnemy,isThatOneAOneMove=${
                        GeneralRules.isThatOneAOneMove(
                            from,
                            to
                        )
                    }",
                    enable = showLogs
                )*/
                if (GeneralRules.isThatOneAOneMove(from, to).not()) {
                    return false
                }
                true
            }

            !isDiagonal && !isAnyEnemyAhead(from) -> {
                true
            }

            isAnyEnemyAheadInDiagonal(from) && isEnemy -> {
                /*
                ChessConfigs.logChess(
                    "isAnyEnemyAheadInDiagonal(from) && isEnemy,isThatOneAOneMove=${
                        GeneralRules.isThatOneAOneMove(
                            from = from,
                            to = to
                        )
                    }", showLogs
                )*/
                if (GeneralRules.isThatOneAOneMove(from, to).not()) {
                    return false
                }
                true
            }

            else -> false
        }
        if (canMove) {
        }
        return canMove
    }
}
