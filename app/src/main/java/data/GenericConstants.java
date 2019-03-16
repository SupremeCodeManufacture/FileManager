package data;

public class GenericConstants {

    public static final String KEY_IN_APP_SKU_ID = "pro_version";
    public static final int KEY_IN_APP_RESPONSE_CODE = 123;
    public static final long TWO_DAYS_IN_MILIS = 172800000L;
    public static String EXTRA_ALL_INTERNAL_FILES_PATH = "/storage/emulated/0";

    public static String SUPPORT_EMAIL = "support@suprememanufacture.com";

    public static String EXTRA_ARG_PATH = "EXTRA_ARG_PATH";
    public static String EXTRA_ARG_PATTERN = "EXTRA_ARG_PATTERN";
    public static String EXTRA_ARG_TITLE = "EXTRA_ARG_TITLE";
    public static String EXTRA_ARG_VIEW_TYPE = "EXTRA_ARG_VIEW_TYPE";
    public static String EXTRA_ARG_SD_CARD_PATH = "EXTRA_ARG_SD_CARD_PATH";
    public static String EXTRA_ARG_PATH_TO = "EXTRA_ARG_PATH_TO";
    public static String EXTRA_NEED_UPGRADE = "EXTRA_NEED_UPGRADE";

    public static final int CODE_PATH_TO_MOVE_SELECTED = 123;
    public static final int CODE_PATH_TO_COPY_SELECTED = 124;


    public static final int STATE_LOADING = 1;
    public static final int STATE_OK = 2;
    public static final int STATE_EMPTY = 3;

    //cloud message topics & cmd
    public static final String TOPIC_ALL = "all";
    public static final String CMD_GO_TO_PLAYSTORE = "go_to_playtore";
    public static final String CMD_PROMOTE_APP = "promote_app";
    public static final String CMD_PROMO_FAKE = "promo_fake";
    public static final String CMD_NOTIFY_BUY = "notify_buy";


    //shared prefs vars
    public static final String KEY_SHARED_PREFS = "KEY_SHARED_PREFS";
    public static final String KEY_SP_IS_USER_PRO = "KEY_SP_IS_USER_PRO";
    public static final String KEY_SP_APP_BUILDS = "KEY_SP_APP_BUILDS";
    public static final String KEY_SP_SELECTED_THEME = "KEY_SP_SELECTED_THEME";
    public static final String KEY_SP_SELECTED_DEF_PATH = "KEY_SP_SELECTED_DEF_PATH";
    public static final String KEY_FIRST_LAUNCH = "KEY_FIRST_LAUNCH";

    public static final int KEY_SELECTED_THEME_1 = 1;
    public static final int KEY_SELECTED_THEME_2 = 2;
    public static final int KEY_SELECTED_THEME_3 = 3;
    public static final int KEY_SELECTED_THEME_4 = 4;
    public static final int KEY_SELECTED_THEME_5 = 5;

    public static final int KEY_SELECTED_LAST_USED = 1;
    public static final int KEY_SELECTED_BY_PATH = 2;
    public static final int KEY_SELECTED_FOR_COPY_MOVE = 3;


    public static long TIME_SINCE_3_HOURS = System.currentTimeMillis() - 43200000L;
    public static long TIME_SINCE_24_HOURS = System.currentTimeMillis() - 43200000L;
    public static long TIME_SINCE_3_DAYS = System.currentTimeMillis() - 259200000L;
    public static long TIME_SINCE_7_DAYS = System.currentTimeMillis() - 604800000L;
    public static long TIME_SINCE_30_DAYS = System.currentTimeMillis() - 2592000000L;
    public static long TIME_SINCE_ANWAYS = 0;

    public static String REGEX_IMAGES = "([^\\s]+(\\.(?i)(bmp|gif|ico|jpeg|jpg|pcx|png|psd|tga|tiff|tif|xcf))$)";
    public static String REGEX_VIDS = "([^\\s]+(\\.(?i)(avi|mov|wmv|mkv|3gp|f4v|flv|mp4|mpeg|webm))$)";
    public static String REGEX_DOCS = "([^\\s]+(\\.(?i)(xls|xlk|xlsb|xlsm|xlsx|xlr|xltm|xlw|numbers|ods|ots|doc|docm|docx|dot|mcw|rtf|pages|odt|ott|pdf|pptx|keynote|ppt|pps|pot|odp|otp))$)";
    public static String REGEX_MUSIC = "([^\\s]+(\\.(?i)(aiff|aif|wav|flac|m4a|wma|amr|mp2|mp3|wma|aac|mid|m3u))$)";
    public static String REGEX_ARCHIVES = "([^\\s]+(\\.(?i)(cab|7z|alz|arj|bzip2|bz2|dmg|gzip|gz|jar|lz|lzip|lzma|zip|rar|tar|tgz))$)";
    public static String REGEX_APKS = "([^\\s]+(\\.(?i)(apk))$)";

}