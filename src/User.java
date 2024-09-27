import java.sql.SQLException;

public class User {
    String Username;
    public User(String username){
        Username = username;
    }
    public void newUpload(String name) throws SQLException {
        DBcontext dBcontext= new DBcontext();
        var conn = dBcontext.conn;

    }
}
