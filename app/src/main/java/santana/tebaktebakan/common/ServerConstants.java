package santana.tebaktebakan.common;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class ServerConstants {
    public static final String UrlBeckend = "http://chatmeet.cloudapp.net:3745/";
    //rest api
    public static final String getTebakanList = UrlBeckend+"api/v_gs/getTebakanList";

    //url gambar tidak perlu pakai garis miring lagi
    public static final String getGambarTebakan = UrlBeckend+"getImage";

    public static final String UploadGambarTebakan = UrlBeckend+"api/v_gs/uploadGambarTebakan";
    public static final String registerGcm = UrlBeckend+"api/v_gs/registerGcm";

    //upload tebakan type
    public static final String insertTebakanTextGambar = UrlBeckend+"api/v_gs/insertTebakanTextGambar";
    public static final String insertTebakanText = UrlBeckend+"api/v_gs/insertTebakanText";

    //show all tebakan
    public static final String ShowTebakanFirst = UrlBeckend+"api/v_gs/showAllTebakanFirst";
    public static final String ShowAllTebakanPaging = UrlBeckend+"api/v_gs/showTebakanPaging";

    public static final String registerUser = UrlBeckend+"register";
    public static final String registerTempUser = UrlBeckend+"registerTemp";
    public static final String loginTebakan = UrlBeckend+"login";
    public static final String loginTebakanTemp = UrlBeckend+"loginTemp";

    //api jawaban benar
    public static final String jawabanBenar = UrlBeckend+"api/v_gs/jawabanBenar";
    public static final String jawabanSalah = UrlBeckend+"api/v_gs/jawabanSalah";
    public static final String next = UrlBeckend+"api/v_gs/next";
    public static final String tryAgain = UrlBeckend+"api/v_gs/tryAgain";

    //api for tempUser
    public static final String showTebakanPagingTemp = UrlBeckend+"api/v_gs/showTebakanPagingTemp";
    public static final String ShowAlltebakanTemp = UrlBeckend+"api/v_gs/showAllTebakanFirstTemp";


// sending request
    public static final String mParamsUsername = "username";
    public static final String mParamsPassword = "passwordBrow";
    public static final String mParamsGcmID = "gcmID";
    public static final String mParamsIdChat = "idChatGroup";
    public static final String mParamsEmail = "email";
    public static final String mParamsToken = "access_token";
    public static final String mParamsIdTebakan = "_idTebakan";
    public static final String mParamCoins = "coins";
    public static final String mParams_idUser = "_idUser";
    public static final String mParamsTextTebakan = "textTebakan";
    public static final String mParamsGambarTebakan = "gambarTebakanFile";
    public static final String mParamsGambarTebakanUrl = "gambarTebakan";
    public static final String mParamsBinaryGambarTebakan = "gambarTebakanBinary";
    public static final String mParamsKunciTebakan= "jawabanTebakan";
    public static final String mParamsQuestion = "question";
    public static final String mParamGender = "gender";
    public static final String mParamlastIdTebakan = "lastIdTebakan";
    public static final String mParamlevel = "level";

    //result from request
    public static final String resultBeckend = "result";
    public static final String statusBeckend = "status";
    public static final String statusBeckendOk = "ok";
    public static final String getTokenBeckend = "token";
    public static final String resultRegisterBeckend = "data_user_inserted";
    public static final String resultLoginBeckend = "login_success";
    public static final String uidUser = "uidUser";
    public static final String dataListTebakan = "dataListTebakan";
    public static final String _idTebakan = "_id";
    public static final String textTebakan = "textTebakan";
    public static final String gambarTebakan = "gambarTebakan";
    public static final String _idUser = "_idUser";
    public static final String gcmID = "gcmID";
    public static final String username = "username";
    public static final String point = "point";


    //result type
    public static final String resultType = "resultType";
    public static final int addTebakanView = 0;
    public static final int addITebakanDown = 1;
    public static final int addTebakanAtas = 2;
    public static final int loginResult = 3;
    public static final int registerResult = 4;
    public static final int registerTempResult = 44;
    public static final int uploadResult = 5;
    public static final int showTebakanResult = 6;
    public static final int registerGcmResult = 7;
    public static final int dataFoundResult = 7;

    //result from request not_ok
    public static final int ErrorUserNotFound = 11;
    public static final int ErrorQuery = 12;
    public static final int ErrorEmptyParams = 14;
    public static final int ErrorDataNotFound = 25;
    public static final int GroupAlreadyExist = 20;
    public static final int ErrorUserAlreadyRegistered = 15;

    //token error
    public static final int ErrorEmptyToken = 27;
    public static final int ErrorGenereteToken = 13;
    public static final int ErrorTokenExpired = 17;
    public static final int ErrorVerifyToken = 18;
    public static final int CoinsZero = 31;
}
