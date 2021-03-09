package me.yeojoy.hancahouse.main

import kotlinx.coroutines.*
import me.yeojoy.hancahouse.model.Item
import me.yeojoy.hancahouse.repository.ItemNetworkRepository

class SellingTabPagePresenter(view: TabPageContract.View<Item>) : TabPageContract.Presenter<Item> {

    private var items: MutableList<Item> = mutableListOf()
    private var view = view

    private val parentJob = Job()
    private val coroutineExceptionHandler: CoroutineExceptionHandler
            = CoroutineExceptionHandler { _, throwable ->
        coroutineScope.launch(Dispatchers.Main) {
            view.onFailGetItems()
        }

        GlobalScope.launch { println("Caught $throwable") }
    }

    val coroutineScope = CoroutineScope(
            Dispatchers.Main + parentJob
                    + coroutineExceptionHandler
    )

    override fun retrieveItems() {
        coroutineScope.launch(Dispatchers.Main) {
            items.clear()
            val itemsFromWeb = getItemsFromWebpage()
            items.addAll(itemsFromWeb)
            if (items.isEmpty()) view.onFailGetItems() else view.onSuccessGetItems()
        }
    }

    override fun itemClicked(position: Int) {
        view.onClickItem(itemAt(position))
    }

    override fun numberOfItem(): Int {
        return items.size
    }

    override fun itemAt(position: Int): Item {
        return items.get(position)
    }

    override fun onViewCreated() {

    }

    override fun onViewDestroyed() {
        coroutineScope.cancel()
    }

    private suspend fun getItemsFromWebpage(): List<Item> =
        withContext(Dispatchers.IO) {
            val itemNetworkRepository = ItemNetworkRepository()
            itemNetworkRepository.loadPage(1)
        }

}

