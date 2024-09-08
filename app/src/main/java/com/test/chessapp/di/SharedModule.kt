package com.test.chessapp.di

import com.test.chessapp.presentation.boardScreen.ChessViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SharedModules = module {
    viewModel {
        ChessViewModel()
    }
}