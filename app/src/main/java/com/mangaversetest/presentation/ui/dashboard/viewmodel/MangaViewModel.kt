package com.mangaversetest.presentation.ui.dashboard.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mangaversetest.domain.ManageModel
import com.mangaversetest.repository.db.ManageDBRepo
import com.mangaversetest.repository.manga.MangaRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(
    private val mangaRepo: MangaRepo,
    private val mangaDBRepo: ManageDBRepo
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private var isDataLoaded = false

    sealed class UiState {
        object Loading : UiState()
        object Paginating : UiState()
        data class Success(val data: List<ManageModel>) : UiState()
        data class Error(val message: String?) : UiState()
    }

    private val _uiState = MutableLiveData<UiState>(UiState.Loading)
    val uiState: LiveData<UiState> get() = _uiState

    private val _mangaList = MutableLiveData<List<ManageModel>>(emptyList())
    val mangaList: LiveData<List<ManageModel>> get() = _mangaList

    private var currentPage = 1
    private var isLoadingMore = false

    fun getAllManga(pageNo: Int, isPagination: Boolean = false) {
        if (!isPagination && isDataLoaded) return

        if (isPagination && isLoadingMore) return

        if (isPagination) {
            isLoadingMore = true
            _uiState.postValue(UiState.Paginating)
        } else {
            _uiState.postValue(UiState.Loading)
        }

        disposable.add(
            mangaRepo.getAllManga(pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccess ->
                    if (onSuccess.isSuccessful && onSuccess.body()?.code == 200) {
                        val newItems = onSuccess.body()?.data.orEmpty()
                        val current = _mangaList.value.orEmpty()
                        val updatedList = current + newItems

                        if (updatedList.isNotEmpty()) {
                            mangaDBRepo.insertAll(updatedList)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                        }

                        _mangaList.postValue(updatedList)
                        _uiState.postValue(UiState.Success(updatedList))

                        currentPage = pageNo
                        isLoadingMore = false
                        isDataLoaded = true
                    } else {
                        _uiState.postValue(UiState.Error("Unexpected error"))
                        isLoadingMore = false
                    }
                }, { error ->
                    _uiState.postValue(UiState.Error(error.message))
                    isLoadingMore = false
                })
        )
    }


    fun loadNextPage() {
        getAllManga(currentPage + 1, isPagination = true)
    }


    fun loadOfflineData(context: Context) {

        disposable.add(
            mangaDBRepo.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data ->
                    val list = data.map {
                        ManageModel(
                            authors = it.authors,
                            genres = it.genres,
                            id = it.id,
                            nsfw = it.nsfw,
                            status = it.status,
                            subTitle = it.subTitle,
                            summary = it.summary,
                            thumb = it.thumb,
                            title = it.title,
                            totalChapter = it.totalChapter,
                            type = it.type,
                        )
                    }

                    _mangaList.postValue(list)
                    _uiState.postValue(UiState.Success(list))
                }, { error ->
                })
        )


    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
