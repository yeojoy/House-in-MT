package me.yeojoy.hancahouse

import kotlinx.coroutines.*
import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.repository.HouseNetworkRepository

class MainPresenter(view: MainContract.View) : MainContract.Presenter {

    private var houses: MutableList<House> = mutableListOf()
    private var view = view

    private val parentJob = Job()
    private val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        coroutineScope.launch(Dispatchers.Main) {
            // When there is an error, add a house object and return.
//            val house = House(99, "Error", "https://", "https://googl.com", "Yeojong", 0L, "2021-03-07", 0L, 0)
//            val houses = mutableListOf<House>()
//            houses.add(house)
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
            view.onGetHouses(false)
        }
    }

    override fun houseItemClicked(position: Int) {
        view.onItemClicked(houseAt(position))
    }

    override fun optionStartCrawlerClicked() {

    }

    override fun optionStopCrawlerClicked() {

    }

    override fun optionSettingClicked() {

    }

    override fun optionManageAlarmClicked() {

    }

    override fun optionDeleteAllClicked() {

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

    override fun listItemClicked() {

    }

    private suspend fun callWebpage(): List<House> =
        withContext(Dispatchers.IO) {
            val houseNetworkRepository = HouseNetworkRepository()
            houseNetworkRepository.loadPage(1)
        }

}

