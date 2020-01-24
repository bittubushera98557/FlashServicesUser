package com.FlashScreen.flashservice.Common;

public class Const {

    public static String adminEmail = "flashservices786@gmail.com";
    public static String adminPhone_First = "9569622228";
    public static String adminPhone_Second = "9569922228";
     public static final String URL_BASE = "http://iwwphp.info/flashservices/";  //old
//     public static final String URL_BASE = "http://flashservices.in/";    //live

     public static final String URL_REG = URL_BASE + "json/customer_register_json.php";
    public static final String URL_VREG = URL_BASE + "json/add_provider_json.php";
    public static final String URL_User_UpdateProfile= URL_BASE + "json/update_customer_profile_new.php";
    public static final String URL_ADD_VENDOR_SIGNUP= URL_BASE + "json/add_vendor_signup.php";
    public static final String URL_LOGIN = URL_BASE + "json/login_customer.php";
    public static final String UPDATE_NEW_PASSWORD = URL_BASE + "json/update_password.php";
    public static final String URL_VLOGIN = URL_BASE + "json/login_providers.php";
    public static final String URL_ADMIN_LOGIN = URL_BASE + "json/login_admin.php";
    public static final String URL_FETCH_CAT = URL_BASE + "json/fetch_category.php";
    public static final String URL_FETCH_CAT_DETAILS = URL_BASE + "json/fetch_services.php";
    public static final String URL_FETCH_ASSIGN = URL_BASE + "json/fetch_assign_user.php";
    public static final String URL_FETCH_PROVIDERS = URL_BASE + "json/fetch_providers.php";

    public static final String URL_FETCH_STATE = URL_BASE + "json/fetch_state.php";
    public static final String URL_FETCH_TIME = URL_BASE + "json/fetch_time.php";
    public static final String URL_FETCH_CITY = URL_BASE + "json/fetch_city.php";
    public static final String URL_FETCH_BANNER = URL_BASE + "json/fetch_banner.php";
    public static final String URL_SEND_ASSIGN = URL_BASE + "json/assign_customer.php";
    public static final String URL_CHANGE_ASSIGN = URL_BASE + "json/update_assign_customer.php";

    public static final String URL_SEND_SERVICE = URL_BASE + "json/add_location_json.php";
    public static final String URL_SEND_mob_token = URL_BASE + "json/add_mobile_token.php";
    public static final String URL_ORDER = URL_BASE + "json/fetch_order.php";

    public static final String Logout_Admin = URL_BASE + "json/logout_admin.php";
    public static final String Logout_noty = URL_BASE + "json/logout_mobile_token.php";
    public static final String Logout_noty_user = URL_BASE + "json/logout_user_mobiletoken.php";
    public static final String Fetch_members = URL_BASE + "json/fetch_customers.php";

    public static final String Add_Service = URL_BASE + "json/add_services_json.php";
    public static final String Add_forget = URL_BASE + "json/reset_password.php";
    public static final String Fetch_all = URL_BASE + "json/fetch_order.php";

    public static final String Save_user_Mob_token = URL_BASE + "json/add_user_mobiletoken.php";
    public static final String URL_SEND_NOTIFICATION = URL_BASE + "json/add_notification.php";
    public static final String URL_FETCH_NOTIFICATION = URL_BASE + "json/fetch_notification.php";

    public static final String URL_SEND_FEEDBACK = URL_BASE + "json/add_feedback.php";
    public static final String URL_FETCH_FEEDBACK = URL_BASE + "json/fetch_feedback.php";
    public static final String URL_FETCH_VENDORFEEDBACK = URL_BASE + "json/fetch_vendor_feedback.php";
    public static final String URL_VERSION = URL_BASE + "json/version.php";
    public static final String URL_CANCEL_ORDER = URL_BASE + "json/cancel_order.php";

    public static final String URL_CANCEL = URL_BASE + "json/cancel_orderId.php";
    public static final String URL_CANCEL_VENDOR_NOTIFICATION = URL_BASE + "json/cancel_vendor.php";
    public static final String URL_GET_MAP_STATUS= URL_BASE + "json/fetch_map_status.php";

    public static final String URL_userTermCondition=URL_BASE +"json/fetch_user_terms.php";
    public static final String OTP_URL= URL_BASE +"/json/otp_send.php";


    public  static  String  catId="";
    public  static  String  image="";
    public  static  String  price="";
    public  static  String  serviceId="";
    public  static  String  stateid="";
    public  static  String  cityid="";
    public  static  String  areaname="";
    public  static  String  date="";
    public  static  String  time="";
    public  static  String  lattitude="";
    public  static  String  longitude="";

    public  static  String  searchedOrderLat="";
    public  static  String  searchedOrderLng="";
    public  static  String[]  place_id;


}
