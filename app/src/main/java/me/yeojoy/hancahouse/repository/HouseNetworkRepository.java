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

    public void loadPage(int page, OnLoadPageListener listener) {

        Runnable network = () -> {
            Connection.Response response = null;

            try {
                String url = URLDecoder.decode(URL_FORMAT_FOR_RENT, "UTF-8") + page;
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
                if (TextUtils.isEmpty(thumbnailUrl)) {
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
                        date != null ? date.getTime() : 0, parsedTime, Integer.parseInt(uid), (byte) 1);
                Log.d(TAG, i + " >>> " + house.toString());

                houses.add(house);

            }

            Log.d(TAG, "=================================================================");
            try {
                String url = URLDecoder.decode(URL_FORMAT_FOR_SUBLET, "UTF-8") + page;
                response = Jsoup.connect(url)
                        .method(Connection.Method.GET)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response == null) {
                return;
            }

            hancaDocument = null;
            try {
                hancaDocument = response.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (hancaDocument == null) {
                return;
            }

            thumbnails = hancaDocument.select("tbody > tr > td.kboard-list-thumbnail > a");
            titles = hancaDocument.select("tbody > tr > td.kboard-list-title > a");
            dates = hancaDocument.select("tbody > tr > td.kboard-list-date");
            // authors = soup.select('tbody > tr > td.kboard-list-user')
            authors = hancaDocument.select("tbody > tr > td.kboard-list-user");

            Log.d(TAG, "*****************************************************************");

            for (int i = 0, j = thumbnails.size(); i < j; i++) {
                Element thumbnailElement = thumbnails.get(i);
                Element titleElement = titles.get(i);
                Element dateElement = dates.get(i);

                String thumbnailUrl = thumbnailElement.select("img").attr("src");
                if (TextUtils.isEmpty(thumbnailUrl)) {
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
                        date != null ? date.getTime() : 0, parsedTime, Integer.parseInt(uid), (byte) 2);
                Log.d(TAG, i + " >>> " + house.toString());

                houses.add(house);

            }
            Log.d(TAG, "*****************************************************************");


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
