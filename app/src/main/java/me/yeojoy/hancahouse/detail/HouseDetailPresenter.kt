package me.yeojoy.hancahouse.detail

import kotlinx.coroutines.*
import me.yeojoy.hancahouse.BuildConfig
import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.model.ItemDetail
import me.yeojoy.hancahouse.repository.ItemDetailNetworkRepository
import me.yeojoy.hancahouse.util.WebPageManager

class HouseDetailPresenter(view: ItemDetailContract.View<House>) : ItemDetailContract.Presenter<House> {

    private val view: ItemDetailContract.View<House> = view
    private lateinit var house: House
    private lateinit var houseDetail: ItemDetail

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

    override fun setItem(house: House) {
        this.house = house

        coroutineScope.launch(Dispatchers.Main) {
            houseDetail = getHouseDetailContents(house.title, house.detailUrl)

            val webPageManager = WebPageManager<House>()
            houseDetail.contents?.let {
                view.onGetWholeContents(webPageManager.parseWholePage(this@HouseDetailPresenter, it)) }
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

    private suspend fun getHouseDetailContents(title: String, detailUrl: String): ItemDetail =
            withContext(Dispatchers.IO) {
                val houseDetailNetworkRepositoy = ItemDetailNetworkRepository()
                houseDetailNetworkRepositoy.loadPage(title, detailUrl)
            }

    override fun goWebClicked() {
        if (BuildConfig.DEBUG) println(house.detailUrl)
        view.onGetWebUrl(house.detailUrl)
    }
}

