import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        try {
//            DropUserTable();
            DBcontext dBcontext = new DBcontext();
            dBcontext.initialiseUsersTable();
//            signUpPrompt(dBcontext);
            String[] s= dBcontext.getAllUsers();
            for(int a =0 ;a<s.length;a++){
                System.out.println(s[a]);
            }
            dBcontext.initialiseUsersTable();
            dBcontext.initialiseUploadsTable();

//            sign up test
            signUpPrompt(dBcontext);

//            login test
            User user=loginPrompt(dBcontext);
            if(user!=null){
                System.out.println("logged in!");
                System.out.println(user.Username);
                System.out.println("Delete account?");
                String ans = myObj.nextLine();
                if(Objects.equals(ans, "y") || Objects.equals(ans, "yes")){
                    dBcontext.deletUser(user);
                }
            }
            else {
                System.out.println("not Logged in");
            }
            System.out.println(user.Username);

        } catch (SQLException e) {
            System.out.println(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("operation ran successfully");
        }
    }
    static void signUpPrompt(DBcontext dBcontext) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
        Scanner myObj = new Scanner(System.in);
        System.out.println("enter username");
        String username = myObj.nextLine();
        System.out.println("enter password");
        String password = myObj.nextLine();
        dBcontext.signup(username,password);
    }
    static User loginPrompt(DBcontext dBcontext) throws SQLException {
        Scanner myObj = new Scanner(System.in);
        System.out.println("enter username");
        String username = myObj.nextLine();
        System.out.println("enter password");
        String password = myObj.nextLine();
        return dBcontext.login(username, password);
    }
    static void DropUserTable() throws SQLException {
        DBcontext dBcontext=new DBcontext();
        var sql = "DROP TABLE users";
        dBcontext.conn.prepareStatement(sql).execute();

    }
}