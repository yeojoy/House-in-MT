package me.yeojoy.hancahouse.repository;

import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import me.yeojoy.hancahouse.app.Constants;
import me.yeojoy.hancahouse.model.HouseDetail;

public class HouseDetailNetworkRepository implements Constants {
    private static final String TAG = HouseDetailNetworkRepository.class.getSimpleName();

    private static HouseDetailNetworkRepository sInstance;

    public static HouseDetailNetworkRepository getInstance() {
        if (sInstance == null) {
            synchronized (HouseDetailNetworkRepository.class) {
                if (sInstance == null) {
                    sInstance = new HouseDetailNetworkRepository();
                }
            }
        }
        return sInstance;
    }

    private HouseDetailNetworkRepository() {}

    public void loadPage(HouseDetail houseDetail, OnLoadDetailPageListener listener) {
        Log.d(TAG, "housedetail > " + houseDetail.toString());
        Runnable network = () -> {

            Connection.Response response = null;
            try {
                String url = URLDecoder.decode(houseDetail.getUrl(), "UTF-8");

                if (!url.startsWith("https://")) {
                    url = HOST + url;
                }

                Log.d(TAG, "Detail House url : " + url);
                response = Jsoup.connect(url)
                        .method(Connection.Method.GET)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Document detailDocument = null;

            try {
                detailDocument = response.parse();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
                return;
            }

            if (detailDocument == null) {
                return;
            }

            Elements contents = detailDocument.select(SELECT_DETAIL_CONTENTS);
            Elements images = detailDocument.select(SELECT_DETAIL_IMAGES);

            Log.d(TAG, "contents > " + contents.toString());

            StringBuilder contentStringBuilder = new StringBuilder();

            List<Node> childNodes = contents.get(0).childNodes();

            for (Node node : childNodes) {
                if (node.nodeName().equals(NODE_NAME_IMAGE)) {
                    continue;
                }

                String text = node.toString();
                if (text.equals(NODE_NEW_LINE)) {
                    text = System.lineSeparator();
                } else if (text.equals(" ")) {
                    continue;
                } else if (text.contains("&nbsp;")) {
                    text = text.replaceAll("(&nbsp;)", " ").trim();
                }

                if (!TextUtils.isEmpty(text)) {
                    contentStringBuilder.append(text);
                }
            }

            List<String> imageUrls = new ArrayList<>();
            for (Element e : images) {
                imageUrls.add(e.attr(ATTR_SOURCE));
            }

            Log.d(TAG, "content > " + contentStringBuilder.toString());
            houseDetail.setContents(contentStringBuilder.toString());

            if (imageUrls.size() > 0) {
                houseDetail.setImageUrls(imageUrls);
            }

            if (listener != null) {
                listener.onLoadDetailPage(houseDetail);
            }
        };

        Thread thread = new Thread(network);
        thread.start();
    }

    public interface OnLoadDetailPageListener {
        void onLoadDetailPage(HouseDetail houseDetail);
    }
}
