package com.app.citycareservice.utils;

public interface Params {

    long ONE_SEC_IN_MILLS = 1000;
    int RESEND_OTP_TIME = 30;
    int MAX_PHONE_NUM_LENGTH = 10;
    int OTP_LENGTH = 6;

    int REQ_CODE_SELECT_IMAGE = 10001;
    int REQ_CODE_ENABLE_GPS = 10002;
    int REQ_CODE_LOCATION_PERMISSION = 10003;


    String SHARED_PREF_NAME = "CCSharedPref";
    String INTENT_KEY_SERVICE_CATEGORY_ID = "serviceCategory";
    String DEFAULT_EMPTY_STRING = "-";
    //    String API_BASE_URL = "https://www.citycareservice.com";
    String API_BASE_URL = "http://sarkarinokar.com:3000/api/v1/";


    //Shared Pref Keys
    String SP_KEY_IS_LOGGED_IN = "isLogin";
    String SP_KEY_USER_NUMBER = "user_number";
    String SP_KEY_USER_ID = "user_id";
    String SP_KEY_AUTH_TOKEN = "auth_token";
    String SP_KEY_USER_NAME = "name";
    String SP_KEY_USER_GENDER = "gender";
    String SP_KEY_USER_PROFILE_PIC = "profilePic";
    String SP_KEY_USER_PHONE = "phoneNumber";
    String SP_KEY_USER_ADDRESS = "address";
    String SP_KEY_USER_PINCODE = "pincode";
    String SP_KEY_USER_FIRST_NAME = "first_name";
    String SP_KEY_USER_LAST_NAME = "last_name";
    String SP_KEY_USER_EMAIL = "user_email";
    String SP_KEY_PROFILE_IMAGE = "user_profile_img";
    String SP_KEY_TEMP_USER_LOCALITY = "temp_user_locality";
    String SP_KEY_TEMP_USER_SUB_LOCALITY = "temp_user_sub_locality";
    //    String SP_KEY_LAST_USED_ADDRESS = "lastUsedAddress";
    String SP_KEY_LAST_USED_ADDRESS_ID = "lastUsedAddressId";

    //Intent Keys
    String INTENT_KEY_TIME_TO_FINISH = "timeToFinish";
    String INTENT_KEY_SERVICE_ID = "serviceId";

    //    API Urls
    String API_REGISTER_URL = "register";
    String API_UPDATE_PROFILE_URL = "users/update-profile";
    String API_SERVICE_URL = "services";
    String API_CREATE_ORDER_URL = "orders/create";
    String API_MY_ORDERS_URL = "users/my-orders";
    String API_GET_ALL_SERVICE_URL = "services";

    //    Api Keys
    String API_AUTH_TOKEN_KEY = "x-auth-token";
    String API_PHONE_NUM_KEY = "phone";
    String API_USER_ID_KEY = "id";
    String API_NAME_KEY = "name";
    String API_LAST_NAME_KEY = "last_name";
    String API_EMAIL_KEY = "email";
    String API_PROFILE_PIC_KEY = "profile_pic";
    String API_QUERY_KEY = "query";
    String API_PAGE_NUMBER_KEY = "page_number";

    String API_SERVICE_ID_KEY = "service_id";
    String API_SERVICE_DATE_KEY = "service_date";
    String API_SERVICE_TIME_KEY = "service_time";
    String API_SERVICE_REMARKS_KEY = "remarks";
    String API_SERVICE_LOCATION_KEY = "location";

    //    Bundle keys
    String BUNDLE_KEY_USER_AUTH_TOKEN = "auth_token";
    String BUNDLE_NAME_PROFILE_DATA = "userProfileBundle";
    String BUNDLE_KEY_PROFILE_PIC_URI = "profile_pic_uri";
    String BUNDLE_KEY_PROFILE_PIC_URL = "profile_pic_url";
    String BUNDLE_KEY_USER_ID = "user_id";
//    String BUNDLE_KEY_USER_FIRST_NAME = "first_name";
//    String BUNDLE_KEY_USER_LAST_NAME = "last_name";
    String BUNDLE_KEY_USER_FULL_NAME = "full_name";
    String BUNDLE_KEY_USER_EMAIL = "user_email";
    String BUNDLE_KEY_USER_ADDRESS = "user_address";
    String BUNDLE_KEY_USER_PHONE = "user_phone";

}
