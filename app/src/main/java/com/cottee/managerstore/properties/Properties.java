package com.cottee.managerstore.properties;

/**
 * Created by Administrator on 2017/11/20.
 */

public class Properties {
    public static final String EMAIL_SUBMIT_PATH = "http://thethreestooges.cn/merchant/bean/login/mail_submit.php";
    public static final String MAIL_VERIFICATION_PATH= "http://thethreestooges.cn/merchant/bean/login/mail_validate.php";
    public static final String USER_BUILD_PATH = "http://thethreestooges.cn/merchant/bean/login/user_write.php";
    public static final String LOGIN_PATH = "http://thethreestooges.cn/merchant/bean/login/login.php";
    public static final String FORGET_PASSWORD_EMAIL_SUBMIT_PATH ="http://thethreestooges.cn/merchant/bean/login/forget_submit.php";
    public static final String FORGET_PASSWORD_EMAIL_VER_SUBMIT_PATH ="http://thethreestooges.cn/merchant/bean/login/forget_validate.php";
    public static final String FORGET_PASSWORD_EMAIL_PSD_SUBMIT_PATH ="http://thethreestooges.cn/merchant/bean/login/user_forget.php";
    public static final String STORE_PHOTO = "https://thethreestooges.cn/merchant/bean/register/photo_mer_upload.php";
    public static final String PHOTO_BUSLIC = "https://thethreestooges.cn/merchant/bean/register/photo_buslic_upload.php";
    public static final String NECESSARY_INFO ="https://thethreestooges.cn/merchant/bean/register/first_register.php";
    public static final String SUPPLY_INFO = "https://thethreestooges.cn/merchant/bean/register/supply_register.php";
    public final static int USER_LOGIN = 1;
    public final static int CHECKOUT_EMAIL = 2;
    public final static int CHECKOUT_EMAIL_VER = 3;
    public final static int FINISH_USER_REGISTER = 4;
    public final static int FORGET_CHECKOUT_EMAIL = 5;
    public final static int FORGET_CHECKOUT_EMAIL_VER = 6;
    public final static int FORGET_CHECKOUT_EMAIL_PWD = 7;
    public static final int REQUEST_ADDRESS = 2;
    public static final int REQUEST_CAMERA = 3;
    public final static int NECESSARY_INFOMATION= 8;
    public final static int PHOTO_BULIC=9;
    public static final int TO_HOME = 10;
    public static final int TO_LOGIN = 11;

    public static boolean isSend=false;

}
