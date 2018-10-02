package me.yeojoy.hancahouse.repository;

import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.yeojoy.hancahouse.app.Constants;
import me.yeojoy.hancahouse.db.DBConstants;
import me.yeojoy.hancahouse.model.House;

public class HouseNetworkRepository implements Constants, DBConstants {
    private static final String TAG = HouseNetworkRepository.class.getSimpleName();

    private static HouseNetworkRepository sInstance;

    public static HouseNetworkRepository getInstance() {
        if (sInstance == null) {
            synchronized (HouseNetworkRepository.class) {
                if (sInstance == null) {
                    sInstance = new HouseNetworkRepository();
                }
            }
        }

        return sInstance;
    }

    private HouseNetworkRepository() {

    }

    public void loadPage(int page, OnLoadPageListener listener) {

        Runnable network = () -> {
            List<House> houses = new ArrayList<>();

            for (int index = 0, loopTime = 2; index < loopTime; index++) {
                Connection.Response response = null;
                try {
                    String url = URLDecoder.decode(index == 0 ? URL_FORMAT_FOR_RENT : URL_FORMAT_FOR_SUBLET, "UTF-8") + page;
                    response = Jsoup.connect(url)
                            .method(Connection.Method.GET)
                            .execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (response == null) {
                    continue;
                }

                Document hancaDocument = null;
                try {
                    hancaDocument = response.parse();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (hancaDocument == null) {
                    continue;
                }

                Elements thumbnails = hancaDocument.select(SELECT_THUMBNAILS);
                Elements titles = hancaDocument.select(SELECT_TITLES);
                Elements dates = hancaDocument.select(SELECT_DATES);
                Elements authors = hancaDocument.select(SELECT_AUTHORS);

                Log.d(TAG, index == 0 ?
                        "=================================================================" :
                        "*****************************************************************"
                );

                for (int i = 0, j = thumbnails.size(); i < j; i++) {
                    Element thumbnailElement = thumbnails.get(i);
                    Element titleElement = titles.get(i);
                    Element dateElement = dates.get(i);

                    String thumbnailUrl = thumbnailElement.select(SELECT_IMAGES).attr(ATTR_SOURCE);
                    if (TextUtils.isEmpty(thumbnailUrl)) {
                        thumbnailUrl = NO_IMAGE;
                    }

                    String detailUrl = thumbnailElement.attr(ATTR_HREF);
                    int uidIndex = detailUrl.indexOf(TEXT_UID);

                    String uid = detailUrl.substring(uidIndex + TEXT_UID.length());

                    String title = titleElement.text();
                    String author = authors.get(i).text();
                    String dateString = dateElement.text();
                    SimpleDateFormat formatter = new SimpleDateFormat(Constants.WEB_DATE_FORMATTER, Locale.getDefault());
                    Date date = null;
                    try {
                        date = formatter.parse(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Date nowDate = new Date();
                    String parsedTime = new SimpleDateFormat(Constants.PARSED_TIME_FORMATTER, Locale.getDefault()).format(nowDate);

                    House house = new House(title, thumbnailUrl, detailUrl, author,
                            date != null ? date.getTime() : 0, parsedTime, Integer.parseInt(uid), index == 0 ? TYPE_RENT : TYPE_SUBLET);
                    Log.d(TAG, i + " >>> " + house.toString());

                    houses.add(house);
                }

                Log.d(TAG, index == 0 ?
                        "=================================================================" :
                        "*****************************************************************"
                );
            }

            if (listener != null) {
                listener.onLoadPage(houses);
            }

        };

        Thread thread = new Thread(network);
        thread.start();
    }

    public interface OnLoadPageListener {
        void onLoadPage(List<House> houses);
    }
}
