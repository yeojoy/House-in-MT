package me.yeojoy.hancahouse.app;

object Constants {
    const val WEB_DATE_FORMATTER = "yyyy.MM.dd"
    const val APP_DATE_FORMAT = "yyyy.MM.dd"

    const val URL_FORMAT_FOR_RENT =  "https://hanca.com/%EA%B5%90%EB%AF%BC%EC%9E%A5%ED%84%B0/?category1=%EC%95%84%ED%8C%8C%ED%8A%B8%2F%EC%A7%91+%EB%A0%8C%ED%8A%B8&mod=list&pageid="
    const val URL_FORMAT_FOR_SUBLET =  "https://hanca.com/%EA%B5%90%EB%AF%BC%EC%9E%A5%ED%84%B0/?category1=%EC%84%9C%EB%B8%8C%EB%A0%9B&mod=list&pageid="
    const val URL_FORMAT_FOR_SELLING =  "https://hanca.com/%ea%b5%90%eb%af%bc%ec%9e%a5%ed%84%b0/?category1=%ED%8C%9D%EB%8B%88%EB%8B%A4&mod=list&pageid="

    const val TYPE_SUBLET = 0
    const val TYPE_RENT = 1

    const val HOST = "https://hanca.com"

    const val NO_IMAGE = "---"

    const val TITLE_EMAIL = "E-mail:"
    const val TITLE_PHONE = "전화번호:"
    const val TITLE_REGION = "지역:"

    const val PARSED_TIME_FORMATTER = "yyyyMMDD_HHmmss"

    const val TIMER_DEFAULT_HOUR = 3
    const val TIMER_MIN_HOUR = 2
    const val TIMER_MAX_HOUR = 12

    const val CRAWLER_STATUS_ON = 1
    const val CRAWLER_STATUS_OFF = -1

    /* SharedPreferences Key */
    const val KEY_INITIALIZE_PAGE = "key_first_run__"
    const val KEY_TIMER_DURATION = "timer_duration__"
    const val KEY_CRAWLER_STATUS = "crawler_switch__"

    /* JSoup select */
    const val SELECT_THUMBNAILS = "tbody > tr > td.kboard-list-thumbnail > a"
    const val SELECT_TITLES = "tbody > tr > td.kboard-list-title > a"
    const val SELECT_DATES = "tbody > tr > td.kboard-list-date"
    const val SELECT_AUTHORS = "tbody > tr > td.kboard-list-user"
    const val SELECT_IMAGES = "img"

    const val SELECT_DETAIL_CONTENTS = "div.content-view"
    const val SELECT_DETAIL_IMAGES = "div.content-view > img"

    const val ATTR_SOURCE = "src"
    const val ATTR_HREF = "href"

    const val NODE_NAME_IMAGE = "img"
    const val NODE_NEW_LINE = "<br>"

    const val TEXT_UID = "uid="

    /* Intent Key */
    const val KEY_INTENT_URL = "detail_url"

    /* Intent Filter */
    const val ACTION_START_CRAWLER = "me.yeojoy.hancahouse.START_CRAWLER"
}
