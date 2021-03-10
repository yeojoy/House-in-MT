package me.yeojoy.hancahouse.main

import kotlinx.coroutines.*
import me.yeojoy.hancahouse.app.Constants
import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.repository.HouseNetworkRepository

class RentTabPagePresenter(private val view: TabPageContract.View<House>)
    : TabPageContract.Presenter<House> {

    private var items: MutableList<House> = mutableListOf()

    private val parentJob = Job()
    private val coroutineExceptionHandler: CoroutineExceptionHandler
            = CoroutineExceptionHandler { _, throwable ->
        coroutineScope.launch(Dispatchers.Main) {
            view.onFailGetItems()
        }

        GlobalScope.launch { println("Caught $throwable") }
    }

    private val coroutineScope = CoroutineScope(
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

    override fun itemAt(position: Int): House {
        return items[position]
    }

    override fun onViewCreated() {

    }

    override fun onViewDestroyed() {
        coroutineScope.cancel()
    }

    private suspend fun getItemsFromWebpage(): List<House> =
        withContext(Dispatchers.IO) {
            val houseNetworkRepository = HouseNetworkRepository()
            houseNetworkRepository.loadPage(1, Constants.TYPE_RENT)
        }

}

