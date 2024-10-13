package com.test.chessapp.presentation.boardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.chess.core.model.Piece
import com.test.chess.core.model.Position
import com.test.chess.helpers.Predictor.findNextMoves
import com.test.chessapp.presentation.boardScreen.events.ChessEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChessScreen(
    viewModel: ChessViewModel = koinViewModel()
) {

    val state by viewModel.boardState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(
                    if (state.isWhiteTurn) {
                        Color.Green
                    } else {
                        Color.Gray
                    }
                )
        )
        Spacer(modifier = Modifier.height(20.dp))
        LazyRow {
            items(state.eatenPieces.filter { it.piece.isWhitePiece() }) { item ->
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.piece.getName()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            itemsIndexed(state.board) { row, item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp, vertical = 3.dp)
                    ) {
                        item.forEachIndexed { index, item ->
                            val selected =
                                (row == state.selectedPiece?.row) && (index == state.selectedPiece?.col)
                            ChessPiece(
                                piece = item,
                                selected = selected,
                                position = Position(row = row, col = index),
                                onClick = {
                                    viewModel.onEvent(
                                        ChessEvents.SelectPiece(
                                            col = index,
                                            row = row,
                                        )
                                    )
                                }
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .background(Color.Black)
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyRow {
            items(state.eatenPieces.filter { it.piece.isWhitePiece().not() }) { item ->
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.piece.getName()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(
                    if (state.isWhiteTurn.not()) {
                        Color.Green
                    } else {
                        Color.Gray
                    }
                )
        )
    }

}

@Composable
fun RowScope.ChessPiece(piece: Piece?, position: Position, selected: Boolean, onClick: () -> Unit) {
    val color = if (piece?.isWhitePiece() == true) {
        Color.White
    } else {
        Color.Black
    }
    val selectedModifier = if (selected) {
        Modifier.background(Color.Green, RoundedCornerShape(8.dp))
    } else {
        Modifier.border(
            1.dp,
            color,
            RoundedCornerShape(8.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(horizontal = 1.dp, vertical = 3.dp)
            .then(selectedModifier)
            .clickable {
                onClick.invoke()
            }
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = piece?.getName() ?: "*",
            fontSize = 9.sp,
            color = color
        )
        Text(
            text = "${position.row} * ${position.col}",
            fontSize = 8.sp,
            color = color
        )
        piece?.let {
            if (piece.isWhitePiece()) {
                Text(
                    text = "White",
                    color = color
                )
            } else {
                Text(
                    text = "Black",
                    color = color
                )
            }
        }
    }
}
