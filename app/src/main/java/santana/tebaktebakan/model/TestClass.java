package santana.tebaktebakan.model;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class TestClass {
    private String _idUser;
    private String UserName;
    private Integer Point;

    public TestClass(){}

    public TestClass(String _idUser, String userName, Integer point) {
        this._idUser = _idUser;
        UserName = userName;
        Point = point;
    }

    public String get_idUser() {
        return _idUser;
    }

    public void set_idUser(String _idUser) {
        this._idUser = _idUser;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Integer getPoint() {
        return Point;
    }

    public void setPoint(Integer point) {
        Point = point;
    }
}
