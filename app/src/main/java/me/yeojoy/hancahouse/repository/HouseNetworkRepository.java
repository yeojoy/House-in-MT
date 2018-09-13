package me.yeojoy.hancahouse.repository;

import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.yeojoy.hancahouse.app.Constants;
import me.yeojoy.hancahouse.model.House;

public class HouseNetworkRepository {
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
//            String url = "http://192.168.8.107:5000"; // egg
//            String url = "http://192.168.1.136:5000"; // home network
            String url = "http://172.20.10.4:5000"; // iphone
//            String url = String.format("https://hanca.com/%EA%B5%90%EB%AF%BC%EC%9E%A5%ED%84%B0/?category1=%EC%95%84%ED%8C%8C%ED%8A%B8%2F%EC%A7%91+%EB%A0%8C%ED%8A%B8&mod=list&pageid=%d", page);
            Connection.Response response = null;
            try {
                response = Jsoup.connect(url)
                        .method(Connection.Method.GET)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Document hancaDocument = null;
            try {
                hancaDocument = response.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements thumbnails = hancaDocument.select("tbody > tr > td.kboard-list-thumbnail > a");
            Elements titles = hancaDocument.select("tbody > tr > td.kboard-list-title > a");
            Elements dates = hancaDocument.select("tbody > tr > td.kboard-list-date");

            Log.d(TAG, "=================================================================");
            List<House> houses = new ArrayList<>();
            for (int i = 0, j = thumbnails.size(); i < j; i++) {
                Element thumbnailElement = thumbnails.get(i);
                Element titleElement = titles.get(i);
                Element dateElement = dates.get(i);

                String thumbnailUrl = thumbnailElement.select("img").attr("src");
                if (!TextUtils.isEmpty(thumbnailUrl)) {
                    thumbnailUrl = url + thumbnailUrl.substring(1);
                } else {
                    thumbnailUrl = "No IMAGES!";
                }

                String detailUrl = thumbnailElement.attr("href");
                int uidIndex = detailUrl.indexOf("uid=");

                String uid = detailUrl.substring(uidIndex + 4);

                String title = titleElement.text();
                String dateString = dateElement.text();
                SimpleDateFormat formatter = new SimpleDateFormat(Constants.WEB_DATE_FORMATTER);
                Date date = null;
                try {
                    date = formatter.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "thumbnail > " + thumbnailUrl);
                Log.d(TAG, "url > " + detailUrl);
                Log.d(TAG, "title > " + title);
                Log.d(TAG, "date > " + dateString + ", milliseconds > " + (date != null ? date.getTime() : 0));
                Log.d(TAG, "uid > " + uid);
//                Log.d(TAG, "thumbnail > " + thumbnail.toString());
//                Log.d(TAG, "title > " + title.toString());
//                Log.d(TAG, "date > " + date.toString());
                House house = new House(title, thumbnailUrl, url,
                        date != null ? date.getTime() : 0, Integer.parseInt(uid));

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
