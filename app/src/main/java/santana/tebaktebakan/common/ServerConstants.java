package santana.tebaktebakan.common;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class ServerConstants {
    public static final String UrlBeckend = "http://chatmeet.cloudapp.net:3745/";
    //rest api
    public static final String getTebakanList = UrlBeckend+"api/v_gs/getTebakanList";
    public static final String insertTebakan = UrlBeckend+"api/v_gs/insertTebakan";
    public static final String UploadGambarTebakan = UrlBeckend+"api/v_gs/uploadGambarTebakan";
    public static final String registerGcm = UrlBeckend+"api/v_gs/registerGcm";

    public static final String registerUser = UrlBeckend+"register";
    public static final String loginTebakan = UrlBeckend+"loginTebakan";


// sending request
    public static final String mParamsUsername = "username";
    public static final String mParamsPassword = "passwordBrow";
    public static final String mParamsGcmID = "gcmID";
    public static final String mParamsIdChat = "idChatGroup";
    public static final String mParamsEmail = "email";
    public static final String mParamsToken = "access_token";
    public static final String mParams_idUser = "_idUser";
    public static final String mParamsTextTebakan = "textTebakan";
    public static final String mParamsGambarTebakan = "gambarTebakan";
    public static final String mParamsKunciTebakan= "kunciTebakan";
    public static final String mParamsQuestion = "question";
    public static final String mParamGender = "gender";

    //result from request
    public static final String resultBeckend = "result";
    public static final String statusBeckend = "status";
    public static final String statusBeckendOk = "ok";
    public static final String getTokenBeckend = "token";
    public static final String resultRegisterBeckend = "data_user_inserted";
    public static final String resultLoginBeckend = "login_success";
    public static final String uidUser = "uidUser";
    public static final String dataListTebakan = "dataListTebakan";
    public static final String _idTebakan = "_idTebakan";
    public static final String textTebakan = "textTebakan";
    public static final String gambarTebakan = "gambarTebakan";
    public static final String _idUser = "_idUser";


    //result type
    public static final String resultType = "resultType";
    public static final int addTebakanView = 0;
    public static final int addITebakanDown = 1;
    public static final int addTebakanAtas = 2;
    public static final int loginResult = 3;
    public static final int registerResult = 4;
    public static final int uploadResult = 5;
    public static final int showTebakanResult = 6;
    public static final int registerGcmResult = 7;

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
