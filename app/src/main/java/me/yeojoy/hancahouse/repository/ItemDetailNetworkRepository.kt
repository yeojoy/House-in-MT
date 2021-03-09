package me.yeojoy.hancahouse.repository

import android.text.TextUtils
import android.util.Log
import me.yeojoy.hancahouse.app.Constants
import me.yeojoy.hancahouse.model.ItemDetail
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.select.Elements
import java.net.URLDecoder
import java.util.*

class ItemDetailNetworkRepository {
    companion object {
        private val TAG = ItemDetailNetworkRepository::class.java.simpleName
    }

    fun loadPage(title: String, detailUrl: String?) : ItemDetail {
        var url = URLDecoder.decode(detailUrl, "UTF-8")
        if (!url.startsWith("https://")) {
            url = Constants.HOST + url
        }
        Log.d(TAG, "Detail House url : $url")
        var response = Jsoup.connect(url)
                .method(Connection.Method.GET)
                .execute()


        var detailDocument: Document? = response?.parse()

        val contents: Elements = detailDocument?.select(Constants.SELECT_DETAIL_CONTENTS) ?: Elements()
        val images : Elements = detailDocument?.select(Constants.SELECT_DETAIL_IMAGES) ?: Elements()

        Log.d(TAG, "contents > $contents")
        val contentStringBuilder = StringBuilder()

        val childNodes : List<Node> = contents[0].childNodes()
        for (node in childNodes) {
            if (node.nodeName() == Constants.NODE_NAME_IMAGE) {
                continue
            }
            var text = node.toString()
            if (text == Constants.NODE_NEW_LINE) {
                text = System.lineSeparator()
            } else if (text == " ") {
                continue
            } else if (text.contains("&nbsp;")) {
                text = text.replace("(&nbsp;)".toRegex(), " ").trim { it <= ' ' }
            }
            if (!TextUtils.isEmpty(text)) {
                contentStringBuilder.append(text)
            }
        }
        val imageUrls: MutableList<String> = ArrayList()
        for (e in images) {
            imageUrls.add(e.attr(Constants.ATTR_SOURCE))
        }

        val pattern = "\n{3,}"
        val contentString = contentStringBuilder.toString().replace(pattern.toRegex(), "\n")
        Log.d(TAG, "content > $contentString")

        return ItemDetail(title, contentString, null, detailUrl, imageUrls)
    }

}