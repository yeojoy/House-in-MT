package me.yeojoy.hancahouse.app;

public interface Constants {
    String WEB_DATE_FORMATTER = "yyyy.MM.dd";
    String APP_DATE_FORMAT = "yyyy.MM.dd";

    String URL_FORMAT_FOR_RENT =  "https://hanca.com/%EA%B5%90%EB%AF%BC%EC%9E%A5%ED%84%B0/?category1=%EC%95%84%ED%8C%8C%ED%8A%B8%2F%EC%A7%91+%EB%A0%8C%ED%8A%B8&mod=list&pageid=";
    String URL_FORMAT_FOR_SUBLET =  "https://hanca.com/%EA%B5%90%EB%AF%BC%EC%9E%A5%ED%84%B0/?category1=%EC%84%9C%EB%B8%8C%EB%A0%9B&mod=list&pageid=";

    String HOST = "https://hanca.com";

    String NO_IMAGE = "---";

    String TITLE_EMAIL = "E-mail:";
    String TITLE_PHONE = "전화번호:";
    String TITLE_REGION = "지역:";

    String PARSED_TIME_FORMATTER = "yyyyMMDD_HHmmss";

    int TIMER_DEFAULT_HOUR = 3;
    int TIMER_MIN_HOUR = 2;
    int TIMER_MAX_HOUR = 12;

    int CRAWLER_STATUS_ON = 1;
    int CRAWLER_STATUS_OFF = -1;

    /* SharedPreferences Key */
    String KEY_INITIALIZE_PAGE = "key_first_run__";
    String KEY_TIMER_DURATION = "timer_duration__";
    String KEY_CRAWLER_STATUS = "crawler_switch__";

    /* JSoup select */
    String SELECT_THUMBNAILS = "tbody > tr > td.kboard-list-thumbnail > a";
    String SELECT_TITLES = "tbody > tr > td.kboard-list-title > a";
    String SELECT_DATES = "tbody > tr > td.kboard-list-date";
    String SELECT_AUTHORS = "tbody > tr > td.kboard-list-user";
    String SELECT_IMAGES = "img";

    String SELECT_DETAIL_CONTENTS = "div.content-view";
    String SELECT_DETAIL_IMAGES = "div.content-view > img";

    String ATTR_SOURCE = "src";
    String ATTR_HREF = "href";

    String NODE_NAME_IMAGE = "img";
    String NODE_NEW_LINE = "<br>";

    String TEXT_UID = "uid=";

    /* Intent Key */
    String KEY_INTENT_URL = "detail_url";

    /* Intent Filter */
    String ACTION_START_CRAWLER = "me.yeojoy.hancahouse.START_CRAWLER";
}
