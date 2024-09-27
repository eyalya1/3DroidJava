import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.LinkedList;
import java.util.Objects;

public class DBcontext {

    Connection conn;
    Statement statement;
    public void signup(String username,String password) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
        if(StringArrIncludes(getAllUsers(),username)){
            return;
        }
        PasswordAuthentication passAuth = new PasswordAuthentication();
        String passHash = passAuth.hash(password.toCharArray());
        String sql = "INSERT INTO users(username, password) VALUES(?,?)";

        try (
                var pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, passHash);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
        /**
         * Adds user to database
         * hashes the passord before adding it
         */
    public User login(String username, String password) throws SQLException {
//        System.out.println(username+" : "+ password);
        PasswordAuthentication passAuth = new PasswordAuthentication();
        String userPass = getUserPassHash(username);
//        System.out.println();
        System.out.println(userPass);
        boolean isAuthed=passAuth.authenticate(password.toCharArray(),userPass);
        if(!isAuthed)
            throw new RuntimeException("invalid username or password");
        else {
        return new User(username);
        }
    }
    public String getUserPassHash(String username) throws SQLException{
//        System.out.println("got getUser");
        var sql = "SELECT username,password FROM users WHERE username = ?";
        var psmnt = conn.prepareStatement(sql);
        psmnt.setString(1,username);
        ResultSet rs= psmnt.executeQuery();
        String result="";
        while (rs.next()){
            System.out.println(rs.getString("password"));
            result=rs.getString("password");
        }
        return result;
    }
    public String[] getAllUsers() throws SQLException{
//        System.out.println("got getAllUsers");
        var sql = "SELECT username FROM users";
        var psmnt = conn.prepareStatement(sql);
        ResultSet rs =  psmnt.executeQuery();
        int count = 0;
        LinkedList<String> result = new LinkedList<String>();
        while (rs.next()) {
            count++;
            result.add(rs.getString("username"));
        }
        String[] resultArr = new String[count];
        for(int count1=0;count1<count;count1++){
            resultArr[count1] = result.get(count1);
        }
        return resultArr;
    }
    public void initialiseUsersTable() throws SQLException {
        var sql = "CREATE TABLE IF NOT EXISTS users ("
                + "	id INTEGER PRIMARY KEY ,"
                + "	username text NOT NULL,"
                + "	password text NOT NULL"
                + ");";
        var stmp = conn.createStatement();
        stmp.execute(sql);
    }
    public void initialiseUploadsTable(){
        var sql = "CREATE TABLE IF NOT EXISTS uploads ("
                + "id INTEGET PRIMARY KEY ,"
                + "file_name NOT NULL ,"
                + "user_id NOT NULL "
                +" FOREIGN KEY (user_id)"
                +"REFERENCES users(id)";
    }
    public void deletUser(User user) throws SQLException {
        System.out.println("got to delete");
        var sql = "DELETE FROM users WHERE username = ?";
        var a = conn.prepareStatement(sql);
        a.setString(1,user.Username);
        a.execute();
    }
    public DBcontext() throws SQLException {
    conn = DriverManager.getConnection("jdbc:sqlite:3Droid.db");
    statement = conn.createStatement();
    }
    public static boolean StringArrIncludes(String[] arr,String search){
        for (int i = 0; i < arr.length; i++) {
            if(Objects.equals(arr[0], search)){
                return true;
            }
        }
        return false;
    }
}
