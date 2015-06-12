package santana.tebaktebakan.common;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class ServerConstants {
    public static final String UrlBeckend = "adsf";
    //rest api
    public static final String getTebakanList = UrlBeckend+"getTebakanList";
    public static final String insertTebakan = UrlBeckend+"insertTebakan";
    public static final String loginTebakan = UrlBeckend+"loginTebakan";
//    public static final String insertTebakan = UrlBeckend+"insertTebakan";


// sending request
    public static final String mParamsUsername = "username";
    public static final String mParamsPassword = "passwordBrow";
    public static final String mParamsGcmID = "gcmID";
    public static final String mParamsIdChat = "idChatGroup";
    public static final String mParamsEmail = "email";
    public static final String mParamsToken = "access_token";
    public static final String mParamsUidUser = "uidUser";
    public static final String mParamsQuestion = "question";
    public static final String mParamGender = "gender";
    public static final String mParamUidUserFriend = "uidUserFriend";

    //result from request
    public static final String resultBeckend = "result";
    public static final String statusBeckend = "status";
    public static final String statusBeckendOk = "ok";
    public static final String getTokenBeckend = "token";
    public static final String resultRegisterBeckend = "data_user_inserted";
    public static final String resultLoginBeckend = "login_success";
    public static final String uidUser = "uidUser";

    //result type
    public static final String resultType = "resultType";
    public static final int resultTypeRegister = 0;
    public static final int resultTypelogin = 1;
    public static final int resultRegisterGcm = 2;
    public static final int resultPushNotif = 3;
    public static final int UserAddToGroup = 4;

    //result from request not_ok
    public static final int ErrorUserNotFound = 11;
    public static final int ErrorQuery = 12;
    public static final int ErrorEmptyParams = 14;
    public static final int GroupAlreadyExist = 20;
    public static final int ErrorUserAlreadyRegistered = 15;

    //token error
    public static final int ErrorEmptyToken = 27;
    public static final int ErrorGenereteToken = 13;
    public static final int ErrorTokenExpired = 17;
    public static final int ErrorVerifyToken = 18;
}
