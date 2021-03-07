package me.yeojoy.hancahouse.detail

import kotlinx.coroutines.*
import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.model.HouseDetail
import me.yeojoy.hancahouse.repository.HouseDetailNetworkRepository
import me.yeojoy.hancahouse.util.WebPageManager

class DetailHousePresenter(view: DetailHouseContract.View) : DetailHouseContract.Presenter {

    private val view: DetailHouseContract.View = view
    private lateinit var house: House
    private lateinit var houseDetail: HouseDetail

    private val parentJob = Job()

    private val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        coroutineScope.launch(Dispatchers.Main) {
            System.out.println("There is an error to get House Detail information.")
        }

        GlobalScope.launch { println("Caught $throwable") }
    }


    private val coroutineScope = CoroutineScope(
            Dispatchers.Main + parentJob
                    + coroutineExceptionHandler
    )

    override fun setHouse(house: House) {
        this.house = house

        coroutineScope.launch(Dispatchers.Main) {
            houseDetail = getHouseDetailContents(house.title, house.detailUrl)

            val webPageManager = WebPageManager()
            houseDetail.contents?.let {
                view.onGetWholeContents(webPageManager.parseWholePage(this@DetailHousePresenter, it)) }
            view.onGetHouseDetail(houseDetail)
        }
    }

    override fun emailClicked(emailAddress: String) {
        view.onGetEmailAddress(emailAddress)
    }

    override fun telephoneNumberClicked(telephoneNumber: String) {
        view.onGetTelephoneNumber(telephoneNumber)
    }

    override fun onViewCreated() {

    }

    override fun onViewDestroyed() {
        coroutineScope.cancel()
    }

    private suspend fun getHouseDetailContents(title: String, detailUrl: String): HouseDetail =
            withContext(Dispatchers.IO) {
                val houseDetailNetworkRepositoy = HouseDetailNetworkRepository()
                houseDetailNetworkRepositoy.loadPage(title, detailUrl)
            }
}

