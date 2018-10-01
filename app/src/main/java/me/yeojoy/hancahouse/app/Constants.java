package me.yeojoy.hancahouse.app;

public interface Constants {
    String WEB_DATE_FORMATTER = "yyyy.MM.dd";
    String APP_DATE_FORMAT = "yyyy.MM.dd";

    //            String URL =  "http://192.168.8.107:5000"; // egg
    String URL =  "http://192.168.1.136:5000"; // home network
//            String URL =  "http://172.20.10.4:5000"; // iphone
    String URL_FORMAT_FOR_RENT =  "https://hanca.com/%EA%B5%90%EB%AF%BC%EC%9E%A5%ED%84%B0/?category1=%EC%95%84%ED%8C%8C%ED%8A%B8%2F%EC%A7%91+%EB%A0%8C%ED%8A%B8&mod=list&pageid=";
    String URL_FORMAT_FOR_SUBLET =  "https://hanca.com/%EA%B5%90%EB%AF%BC%EC%9E%A5%ED%84%B0/?category1=%EC%84%9C%EB%B8%8C%EB%A0%9B&mod=list&pageid=";

    String HOST = "https://hanca.com";

    String DETAIL_URL = URL + "/detail";

    String NO_IMAGE = "---";

    String TITLE_EMAIL = "E-mail:";
    String TITLE_PHONE = "전화번호:";
    String TITLE_REGION = "지역:";

    String PARSED_TIME_FORMATTER = "yyyyMMDD_HHmmss";
}

