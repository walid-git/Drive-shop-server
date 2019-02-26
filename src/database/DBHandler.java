package database;


import java.sql.*;
import org.sqlite.SQLiteException;


public class DBHandler {
    private final boolean DEBUG = true;
    Connection connection;
    Statement statement;

    public DBHandler(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:driveshop.db");
            if(DEBUG)
                System.out.println("connection to database established.");
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS ORDERS (orderId INTEGER,userID INTEGER);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createTables(statement);
    }

    private void createTables(Statement statement){
      /*  try {
            statement.execute(Queries.CREATE_MEMBERS_TABLE);
            if(DEBUG)
                System.out.println("Members table created.");
            statement.execute(Queries.CREATE_EQUIPMENTS_TABLE);
            if(DEBUG)
                System.out.println("Equipments table created.");
            statement.execute(Queries.CREATE_BORROWS_TABLE);
            if(DEBUG)
                System.out.println("Borrows table created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }


}
