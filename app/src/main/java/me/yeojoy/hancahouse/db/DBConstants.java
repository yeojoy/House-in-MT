package me.yeojoy.hancahouse.db;

public interface DBConstants {
    String DATABASE_NAME = "hanca";
    String TABLE_NAME = "houses";
    String TABLE_NAME_DETAIL = "house_details";

    String COLUMN_ID = "id";
    String COLUMN_TITLE = "title";
    /* houses TABLE */
    String COLUMN_THUMBNAIL_URL = "thumbnail_url";
    String COLUMN_DETAIL_URL = "url";
    String COLUMN_AUTHOR = "author";
    String COLUMN_DATE = "date";
    String COLUMN_PARSED_TIME = "parsed_time";
    String COLUMN_UID = "uid";
    String COLUMN_BOARD_TYPE = "board_type";

    /* house_details TABLE */
    String COLUMN_CONTENTS = "contents";
    String COLUMN_IMAGE_URL = "image_url";
    String COLUMN_HOUSE_ID = "house_id";



    int TYPE_RENT = 1;
    int TYPE_SUBLET = 2;
}
