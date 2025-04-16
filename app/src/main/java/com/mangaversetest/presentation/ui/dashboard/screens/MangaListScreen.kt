package com.mangaversetest.presentation.ui.dashboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.mangaversetest.R
import com.mangaversetest.domain.ManageModel
import com.mangaversetest.presentation.ui.dashboard.components.LoaderUI
import com.mangaversetest.presentation.ui.dashboard.components.MangaGridItem
import com.mangaversetest.presentation.ui.dashboard.viewmodel.MangaViewModel
import com.mangaversetest.utils.Utils.hasInternetConnection

@Composable
fun MangaListScreen(viewModel: MangaViewModel, callback: (ManageModel) -> Unit) {
    val mangaList by viewModel.mangaList.observeAsState(emptyList())
    val uiState by viewModel.uiState.observeAsState(MangaViewModel.UiState.Loading)
    val context = LocalContext.current
    val hasInternet = remember { hasInternetConnection(context) }
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        if (hasInternet) {
            viewModel.getAllManga(1)
        } else {
            viewModel.loadOfflineData(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.black))
    ) {
        when (uiState) {
            is MangaViewModel.UiState.Loading -> {
                LoaderUI(context.getString(R.string.loadingManga))
            }

            is MangaViewModel.UiState.Error -> {
                val message = (uiState as MangaViewModel.UiState.Error).message
                Text(
                    text = message ?: context.getString(R.string.somethingWentWrong),
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            is MangaViewModel.UiState.Success,
            is MangaViewModel.UiState.Paginating -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    state = gridState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(mangaList) { _, manga ->
                        MangaGridItem(manga = manga, callback)
                    }

                    if (uiState is MangaViewModel.UiState.Paginating) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            LoaderUI(context.getString(R.string.loadingMore))
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo }
            .collect { layoutInfo ->
                if (hasInternet) {
                    val totalItems = layoutInfo.totalItemsCount
                    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                    if (lastVisibleItem >= totalItems - 6) {
                        viewModel.loadNextPage()
                    }
                }
            }
    }
}