package me.yeojoy.hancahouse

import kotlinx.coroutines.*
import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.repository.HouseNetworkRepository

class MainPresenter(view: MainContract.View) : MainContract.Presenter {

    private var houses: MutableList<House> = mutableListOf()
    private var view = view

    private val parentJob = Job()
    private val coroutineExceptionHandler: CoroutineExceptionHandler
            = CoroutineExceptionHandler { _, throwable ->
        coroutineScope.launch(Dispatchers.Main) {
            view.onGetHouses(true)
        }

        GlobalScope.launch { println("Caught $throwable") }
    }

    private val coroutineScope = CoroutineScope(
            Dispatchers.Main + parentJob
                    + coroutineExceptionHandler
    )

    override fun retrieveHouses() {
        coroutineScope.launch(Dispatchers.Main) {
            houses.clear()
            val hs = callWebpage()
            for (h in hs) {
                System.out.println(h.author)
            }
            houses.addAll(hs)
            view.onGetHouses(houses.isEmpty())
        }
    }

    override fun houseItemClicked(position: Int) {
        view.onItemClicked(houseAt(position))
    }

    override fun optionSettingClicked() {

    }

    override fun numberOfHouses() : Int {
        return houses.size
    }

    override fun houseAt(position: Int): House {
        return houses.get(position)
    }

    override fun onViewCreated() {

    }

    override fun onViewDestroyed() {
        coroutineScope.cancel()
    }

    private suspend fun callWebpage(): List<House> =
        withContext(Dispatchers.IO) {
            val houseNetworkRepository = HouseNetworkRepository()
            houseNetworkRepository.loadPage(1)
        }

}

