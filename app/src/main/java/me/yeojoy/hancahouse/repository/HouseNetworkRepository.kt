package me.yeojoy.hancahouse.repository;

import me.yeojoy.hancahouse.app.Constants
import me.yeojoy.hancahouse.model.House
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.net.URLDecoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HouseNetworkRepository {

    fun loadPage(page: Int): List<House> {
        val houses = mutableListOf<House>()

        for (index in 0..2) {
            val url: String = when (index) {
                0 -> URLDecoder.decode(Constants.URL_FORMAT_FOR_RENT, "UTF-8") + page
                else -> URLDecoder.decode(Constants.URL_FORMAT_FOR_SUBLET, "UTF-8") + page
            }
            val response: Connection.Response? = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .execute()

            val hancaDocument: Document? = response?.parse()

            val thumbnails: Elements? = hancaDocument?.select(Constants.SELECT_THUMBNAILS)
            val titles: Elements? = hancaDocument?.select(Constants.SELECT_TITLES)
            val dates: Elements? = hancaDocument?.select(Constants.SELECT_DATES)
            val authors: Elements? = hancaDocument?.select(Constants.SELECT_AUTHORS)

            if (thumbnails != null) {
                for ((i, thumbnail) in thumbnails.withIndex()) {
                    val title = titles?.get(i)?.text()
                    val author = authors?.get(i)?.text()
                    var dateString = dates?.get(i)?.text()

                    val thumbnailUrl = thumbnail?.select(Constants.SELECT_IMAGES)?.attr(Constants.ATTR_SOURCE) ?: Constants.NO_IMAGE
                    val detailUrl = thumbnail?.attr(Constants.ATTR_HREF)
                    val uidIndex = detailUrl?.indexOf(Constants.TEXT_UID)
                    val uid = detailUrl?.substring(uidIndex!! + Constants.TEXT_UID.length)

                    val formatter = SimpleDateFormat(Constants.WEB_DATE_FORMATTER, Locale.getDefault())
                    var date: Date?
                    try {
                        date = formatter.parse(dateString)
                    } catch (e: ParseException) {
                        val calendar = Calendar.getInstance(Locale.getDefault())
                        dateString = "${calendar.get(Calendar.YEAR)}.${calendar.get(Calendar.MONTH) + 1}.${calendar.get(Calendar.DATE)} $dateString"
                        date = formatter.parse(dateString)
                    }

                    val parsedTime = SimpleDateFormat(Constants.PARSED_TIME_FORMATTER, Locale.getDefault()).format(Date())

                    val house = House(title ?: "", thumbnailUrl,
                        detailUrl ?: "", author ?: "",
                            date?.time ?: 0L, parsedTime, uid?.toLong() ?: 0L)
                    houses.add(house)
                }
            }

        }
        return houses
    }
}
