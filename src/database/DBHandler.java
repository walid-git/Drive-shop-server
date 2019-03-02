package database;


import java.sql.*;
import java.util.ArrayList;

import Network.ProductServer;
import Util.Product;
import database.Queries;
import org.sqlite.SQLiteException;


public class DBHandler {
    private final boolean DEBUG = true;
    Connection connection;
    Statement statement;
    PreparedStatement insertProductStatement;
    PreparedStatement deleteProductStatement;
    PreparedStatement editProductStatement;

    public DBHandler() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + Queries.DB_NAME);
            if (DEBUG)
                System.out.println("connection to database established.");
            statement = connection.createStatement();
            createTables(statement);
            initPreparedStatements();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void initPreparedStatements() {
        try {
            insertProductStatement = connection.prepareStatement("INSERT INTO " + Queries.Product.TABLE_NAME + " (" +
                    Queries.Product.COLUMN_NAME + ", " +
                    Queries.Product.COLUMN_DESCRIPTION + ", " +
                    Queries.Product.COLUMN_PRICE + ", " +
                    Queries.Product.COLUMN_ICON_LOCATION + ") " +
                    "VALUES (?, ?, ?, ?);", new String[]{Queries.COLUMN_ID});

            deleteProductStatement = connection.prepareStatement("DELETE FROM " + Queries.Product.TABLE_NAME +
                    " WHERE " + Queries.COLUMN_ID + " = ? ;");

            editProductStatement = connection.prepareStatement("UPDATE " + Queries.Product.TABLE_NAME + " SET " +
                    Queries.Product.COLUMN_NAME + " = ?, " +
                    Queries.Product.COLUMN_DESCRIPTION + " = ?, " +
                    Queries.Product.COLUMN_PRICE + " = ?, " +
                    Queries.Product.COLUMN_ICON_LOCATION + " = ? " +
                    "WHERE "+ Queries.COLUMN_ID+" = ? ;" , new String[]{Queries.COLUMN_ID});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables(Statement statement) {
        try {
            statement.execute(Queries.Order.CREATE_TABLE_QUERRY);
            if (DEBUG)
                System.out.println("orders table created.");
            statement.execute(Queries.Product.CREATE_TABLE_QUERRY);
            if (DEBUG)
                System.out.println("Products table created.");
            statement.execute(Queries.SubOrder.CREATE_TABLE_QUERRY);
            if (DEBUG)
                System.out.println("Suborders table created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Product> querryProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            ResultSet rs = statement.executeQuery(Queries.Product.QUERY_PRODUCTS);
            while (rs.next()) {
                products.add(new Product(rs.getInt(Queries.COLUMN_ID),
                        (int)rs.getFloat(Queries.Product.COLUMN_PRICE),
                        rs.getString(Queries.Product.COLUMN_NAME),
                        rs.getString(Queries.Product.COLUMN_DESCRIPTION),
                        ProductServer.imgToBytes(rs.getString(Queries.Product.COLUMN_ICON_LOCATION))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public int insertProduct(Product p,String iconLoc) {
        try {
            insertProductStatement.setString(1,p.getName());
            insertProductStatement.setString(2,p.getDescription());
            insertProductStatement.setInt(3,p.getPrice());
            insertProductStatement.setString(4,iconLoc);
            insertProductStatement.executeUpdate();
            insertProductStatement.getGeneratedKeys().next();
            return insertProductStatement.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }

    public int deleteProduct(Product p) {
        try {
            deleteProductStatement.setInt(1,p.getId());
            return deleteProductStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int upadteProduct(Product old, Product updated,String iconLoc) {
        try {
            editProductStatement.setString(1,updated.getName());
            editProductStatement.setString(2,updated.getDescription());
            editProductStatement.setInt(3,updated.getPrice());
            editProductStatement.setString(4,iconLoc);
            editProductStatement.setInt(5,old.getId());
            return editProductStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
