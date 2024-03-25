package com.triskelapps.alcalasuena;

public class DebugHelper {

    public static Class SHORTCUT_ACTIVITY = null;
//    public static Class SHORTCUT_ACTIVITY = SendNewsActivity.class;

    // === SWITCHES DEBUG ===

    // Trues in production apk (automatic)
    private static final boolean CRASH_REPORT_ENABLED = false;
    private static final boolean FINAL_TTF = true;
    private static final boolean PROD_ENVIRONMENT = true;


    // Falses
    private static final boolean FORZE_DATA_SYNC = false;
    private static final boolean MOCK_NOTIF_NEWS = false;







    private static final boolean SKIP_SPLASH = true;
    private static final boolean COMPLETE_EDIT_TEXTS = true;
    public static final boolean DEBUG_MESSAGES = false;
    private static final boolean DEBUG_TOKEN = false;

    private static final boolean FORZE_MASTER = false;
    private static final boolean FORZE_ALARM = true;

    private static final boolean FORZE_BETA_ENV_APK = true;

    public static final boolean BILLING_TESTING = false;

    public static final boolean MOCK_DATA = false;

    // ----------------------------------------------

    public static final boolean SWITCH_FINAL_TTF = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? FINAL_TTF : true;

    public static final boolean SWITCH_CRASH_REPORT_ENABLED = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? CRASH_REPORT_ENABLED : true;

    public static final boolean SWITCH_SKIP_SPLASH = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? SKIP_SPLASH : false;

    public static final boolean SWITCH_COMPLETE_EDIT_TEXTS = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? COMPLETE_EDIT_TEXTS : false;

    public static final boolean SWITCH_PROD_ENVIRONMENT = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? PROD_ENVIRONMENT : true;

    public static final boolean SWITCH_DEBUG_MESSAGES = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? DEBUG_MESSAGES : false;

    public static final boolean SWITCH_FORZE_MASTER = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? FORZE_MASTER : false;

    public static final boolean SWITCH_FORZE_ALARM = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? FORZE_ALARM : false;

    public static final boolean SWITCH_FORZE_DATA_SYNC = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? FORZE_DATA_SYNC : false;

    public static final boolean SWITCH_DEBUG_TOKEN = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? DEBUG_TOKEN : false;

    public static final boolean SWITCH_MOCK_NOTIF_NEWS = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? MOCK_NOTIF_NEWS : false;

}
