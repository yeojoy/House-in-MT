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
import me.yeojoy.hancahouse.model.House;

public class HouseNetworkRepository implements Constants {
    private static final String TAG = HouseNetworkRepository.class.getSimpleName();

    public static final int TYPE_RENT = 1;
    public static final int TYPE_SUBLET = 2;

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

    public void loadPage(int type, int page, OnLoadPageListener listener) {

        if (type != TYPE_RENT && type != TYPE_SUBLET) {
            throw new IllegalArgumentException("Type is only 1 or 2.");
        }

        Runnable network = () -> {
            Connection.Response response = null;

            try {
                String url = URLDecoder.decode(type == 1 ? URL_FORMAT_FOR_RENT : URL_FORMAT_FOR_SUBLET, "UTF-8") + page;
                response = Jsoup.connect(url)
                        .method(Connection.Method.GET)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response == null) {
                return;
            }

            Document hancaDocument = null;
            try {
                hancaDocument = response.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (hancaDocument == null) {
                return;
            }

            Elements thumbnails = hancaDocument.select("tbody > tr > td.kboard-list-thumbnail > a");
            Elements titles = hancaDocument.select("tbody > tr > td.kboard-list-title > a");
            Elements dates = hancaDocument.select("tbody > tr > td.kboard-list-date");
            // authors = soup.select('tbody > tr > td.kboard-list-user')
            Elements authors = hancaDocument.select("tbody > tr > td.kboard-list-user");

            Log.d(TAG, "=================================================================");

            List<House> houses = new ArrayList<>();

            for (int i = 0, j = thumbnails.size(); i < j; i++) {
                Element thumbnailElement = thumbnails.get(i);
                Element titleElement = titles.get(i);
                Element dateElement = dates.get(i);

                String thumbnailUrl = thumbnailElement.select("img").attr("src");
                if (!TextUtils.isEmpty(thumbnailUrl)) {
                    thumbnailUrl = URL + thumbnailUrl.substring(1);
                } else {
                    thumbnailUrl = NO_IMAGE;
                }

                String detailUrl = thumbnailElement.attr("href");
                int uidIndex = detailUrl.indexOf("uid=");

                String uid = detailUrl.substring(uidIndex + 4);

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
                        date != null ? date.getTime() : 0, parsedTime, Integer.parseInt(uid), type == TYPE_RENT ? (byte) 1: (byte) 2);
                Log.d(TAG, i + " >>> " + house.toString());

                houses.add(house);

            }
            Log.d(TAG, "=================================================================");

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
