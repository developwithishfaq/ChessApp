package com.test.chessapp.presentation.boardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.test.chess.model.Piece
import com.test.chessapp.presentation.boardScreen.events.ChessEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChessScreen(
    viewModel: ChessViewModel = koinViewModel()
) {

    val state by viewModel.boardState.collectAsState()
    val viewModelState by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        itemsIndexed(state) { row, item ->
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
                            (row == viewModelState.selectedPiece?.row) && (index == viewModelState.selectedPiece?.col)
                        ChessPiece(
                            piece = item,
                            selected = selected,
                            onClick = {
                                viewModel.onEvent(
                                    ChessEvents.SelectPiece(
                                        index,
                                        row,
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

}

@Composable
fun RowScope.ChessPiece(piece: Piece?, selected: Boolean, onClick: () -> Unit) {

    val selectedModifier = if (selected) {
        Modifier.background(Color.Green, RoundedCornerShape(8.dp))
    } else {
        Modifier.border(1.dp, Color.Black, RoundedCornerShape(8.dp))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(horizontal = 1.dp, vertical = 3.dp)
            .then(selectedModifier)
            .clickable {
                onClick.invoke()
            }
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = piece?.getName() ?: "*",
            fontSize = 9.sp,
            color = if (selected) {
                Color.White
            } else {
                Color.Black
            }
        )
    }
}
