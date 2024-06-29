import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DbFunctions {

    public Connection connectToDb(String dbName, String user, String pass) {
        Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, user, pass);
            if (conn != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return conn;
    }

    // function
    public void createUserTable(Connection conn, String tableName) {
        Statement statement;
        try {
            String query = "create table " + tableName + "(empid SERIAL, username varchar(200), primary key(empid));";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created: " + tableName);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // simple add to table
    // conn & tableName necessary, other params for info to be inserted
    public void createUser(Connection conn, String tableName, String userName) {
        Statement statement;
        try {
            String query = String.format("insert into %s(username) values('%s')", tableName, userName);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.printf("Row Inserted at %s\n", tableName);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

//    public void createMatch(Connection conn, String tableName, String winner, String totalTurns) {
//        Statement statement;
//        try {
//            String query = String.format("insert into %s(winner, total_turns) values('%s', '%s')", tableName, winner, totalTurns);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }

    public void getAllUsers(Connection conn, String tableName) {
        Statement statement;
        ResultSet rs=null;
        try {
            String query = String.format("select * from %s", tableName);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);

            ArrayList<String> returnArray = new ArrayList<>();
            while (rs.next()) {
                returnArray.add(rs.getString("username"));
                System.out.println(rs.getString("username"));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateName(Connection conn, String tableName, String oldName, String newName) {
        Statement statement;
        try {
            String query = String.format("update %s set username='%s' where username='%s'", tableName, newName, oldName);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("User name updated");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deleteUser(Connection conn, String tableName, String userName) {
        Statement statement;
        try {
            String query = String.format("delete from %s where name='%s'", tableName, userName);
            statement= conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("User deleted");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void searchByName(Connection conn, String tableName, String userName) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = String.format("select * from %s where name='%s'", tableName, userName);
            statement = conn.createStatement();
            rs= statement.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getString("username"));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
