package me.yeojoy.hancahouse.detail

import kotlinx.coroutines.*
import me.yeojoy.hancahouse.BuildConfig
import me.yeojoy.hancahouse.model.Item
import me.yeojoy.hancahouse.model.ItemDetail
import me.yeojoy.hancahouse.repository.ItemDetailNetworkRepository
import me.yeojoy.hancahouse.util.WebPageManager

class ItemDetailPresenter(private val view: ItemDetailContract.View<Item>)
    : ItemDetailContract.Presenter<Item> {

    private lateinit var item: Item
    private lateinit var itemDetail: ItemDetail

    private val parentJob = Job()

    private val coroutineExceptionHandler: CoroutineExceptionHandler
            = CoroutineExceptionHandler { _, throwable ->
        coroutineScope.launch(Dispatchers.Main) {
            println("There is an error to get House Detail information.")
        }

        GlobalScope.launch { println("Caught $throwable") }
    }


    private val coroutineScope = CoroutineScope(
            Dispatchers.Main + parentJob
                    + coroutineExceptionHandler
    )

    override fun setItem(item: Item) {
        this.item = item

        coroutineScope.launch(Dispatchers.Main) {
            itemDetail = getItemDetailContents(item.title, item.detailUrl)

            val webPageManager = WebPageManager<Item>()
            itemDetail.contents?.let {
                view.onGetWholeContents(webPageManager.parseWholePage(this@ItemDetailPresenter, it)) }
            view.onGetHouseDetail(itemDetail)
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

    private suspend fun getItemDetailContents(title: String, detailUrl: String): ItemDetail =
            withContext(Dispatchers.IO) {
                val houseDetailNetworkRepositoy = ItemDetailNetworkRepository()
                houseDetailNetworkRepositoy.loadPage(title, detailUrl)
            }

    override fun goWebClicked() {
        if (BuildConfig.DEBUG) println(item.detailUrl)
        view.onGetWebUrl(item.detailUrl)
    }
}

