package database;

import java.sql.*;
import java.util.ArrayList;

import GUI.Main;
import Shared.*;
import Utils.ImgUtils;
import org.sqlite.SQLiteConfig;

public class DBHandler {
    private final boolean DEBUG = true;
    Connection connection;
    Statement statement;
    PreparedStatement insertProductStatement;
    PreparedStatement deleteProductStatement;
    PreparedStatement editProductStatement;
    PreparedStatement querrySingleProductStatement;
    PreparedStatement querryProductQuantityStatement;
    PreparedStatement updateProductQuantityStatement;
    PreparedStatement insertCustomerStatement;
    PreparedStatement deleteCustomerStatement;
    PreparedStatement editCustomerStatement;
    PreparedStatement querryCustomerIdStatement;
    PreparedStatement querryCustomerStatement;
    PreparedStatement insertOrderStatement;
    PreparedStatement updateOrderStatement;
    PreparedStatement querryOrdersStatement;
    PreparedStatement insertSubOrderStatement;

    public DBHandler() {
        try {
            //Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            connection = DriverManager.getConnection("jdbc:sqlite:" + Queries.DB_NAME, config.toProperties());
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
                    Queries.Product.COLUMN_ICON_LOCATION + ", " +
                    Queries.Product.COLUMN_LOCATION + ", " +
                    Queries.Product.COLUMN_AVAILABLE_QTY +
                    ") " +
                    "VALUES (?, ?, ?, ?, ?, ?);", new String[]{Queries.COLUMN_ID});

            deleteProductStatement = connection.prepareStatement("DELETE FROM " + Queries.Product.TABLE_NAME +
                    " WHERE " + Queries.COLUMN_ID + " = ? ;");

            editProductStatement = connection.prepareStatement("UPDATE " + Queries.Product.TABLE_NAME + " SET " +
                    Queries.Product.COLUMN_NAME + " = ?, " +
                    Queries.Product.COLUMN_DESCRIPTION + " = ?, " +
                    Queries.Product.COLUMN_PRICE + " = ?, " +
                    Queries.Product.COLUMN_ICON_LOCATION + " = ?, " +
                    Queries.Product.COLUMN_LOCATION + " = ?, " +
                    Queries.Product.COLUMN_AVAILABLE_QTY + " = ? " +
                    "WHERE " + Queries.COLUMN_ID + " = ? ;", new String[]{Queries.COLUMN_ID});
            querrySingleProductStatement = connection.prepareStatement("SELECT * FROM " + Queries.Product.TABLE_NAME +
                    " WHERE " + Queries.COLUMN_ID + " = ? ;");
            deleteCustomerStatement = connection.prepareStatement("DELETE FROM " + Queries.Customers.TABLE_NAME +
                    " WHERE " + Queries.COLUMN_ID + " = ? ;");
            editCustomerStatement = connection.prepareStatement("UPDATE " + Queries.Customers.TABLE_NAME + " SET " +
                    Queries.Customers.COLUMN_FIRST_NAME + " = ?," +
                    Queries.Customers.COLUMN_LAST_NAME + " = ?," +
                    Queries.Customers.COLUMN_EMAIL + " = ?," +
                    Queries.Customers.COLUMN_PHONE + " = ?," +
                    Queries.Customers.COLUMN_BIRTH_DATE + " = ?," +
                    Queries.Customers.COLUMN_PASSWORD + " = ? " +
                    "WHERE " + Queries.COLUMN_ID + " = ? ;", new String[]{Queries.COLUMN_ID}
            );
            querryCustomerIdStatement = connection.prepareStatement("SELECT " + Queries.COLUMN_ID +
                    " FROM " + Queries.Customers.TABLE_NAME + " WHERE " +
                    Queries.Customers.COLUMN_PHONE + " = ? AND " +
                    Queries.Customers.COLUMN_PASSWORD + " = ? ;");
            querryCustomerStatement = connection.prepareStatement("SELECT * FROM " +
                    Queries.Customers.TABLE_NAME + " WHERE " +
                    Queries.COLUMN_ID + " = ? ;");
            insertCustomerStatement = connection.prepareStatement("INSERT INTO " + Queries.Customers.TABLE_NAME + " (" +
                    Queries.Customers.COLUMN_FIRST_NAME + ", " +
                    Queries.Customers.COLUMN_LAST_NAME + ", " +
                    Queries.Customers.COLUMN_EMAIL + ", " +
                    Queries.Customers.COLUMN_PHONE + ", " +
                    Queries.Customers.COLUMN_BIRTH_DATE + ", " +
                    Queries.Customers.COLUMN_PASSWORD + " ) " +
                    "VALUES (?, ?, ?, ?, ?, ?);", new String[]{Queries.COLUMN_ID});
            insertOrderStatement = connection.prepareStatement("INSERT INTO " + Queries.Order.TABLE_NAME + " (" +
                    Queries.Order.COLUMN_CUSTOMER_ID + ", "+
                    Queries.Order.COLUMN_STATE +
                    " ) VALUES (?,?)", new String[]{Queries.COLUMN_ID});
            insertSubOrderStatement = connection.prepareStatement("INSERT INTO " + Queries.SubOrder.TABLE_NAME + " (" +
                    Queries.SubOrder.COLUMN_ORDER_ID + ", " +
                    Queries.SubOrder.COLUMN_PRODUCT_ID + ", " +
                    Queries.SubOrder.COLUMN_QUANTITY + " ) "+
                    "VALUES (?, ?, ?);");
            updateOrderStatement = connection.prepareStatement("UPDATE " + Queries.Order.TABLE_NAME +
                    " SET " + Queries.Order.COLUMN_STATE + " = ? WHERE " +
                    Queries.COLUMN_ID + " = ? ;");
            querryOrdersStatement = connection.prepareStatement("SELECT * FROM " + Queries.Order.TABLE_NAME + " WHERE " +
                    Queries.Order.COLUMN_CUSTOMER_ID + " = ? ORDER BY " + Queries.Order.COLUMN_STATE );
            querryProductQuantityStatement = connection.prepareStatement("SELECT " + Queries.Product.COLUMN_AVAILABLE_QTY +
                    " FROM " +Queries.Product.TABLE_NAME +
                    " WHERE " + Queries.COLUMN_ID + " = ? ;");
            updateProductQuantityStatement = connection.prepareStatement("UPDATE " + Queries.Product.TABLE_NAME +
                    " SET " + Queries.Product.COLUMN_AVAILABLE_QTY + " = "+Queries.Product.COLUMN_AVAILABLE_QTY+" - ? WHERE " +
                    Queries.COLUMN_ID + " = ? ;");
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
            statement.execute(Queries.Customers.CREATE_TABLE_QUERRY);//
            if (DEBUG)
                System.out.println("Customers table created.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Order> querryOrders() {
        ArrayList<Order> orders = new ArrayList<Order>();
        try {
            ResultSet rs = statement.executeQuery(Queries.Order.QUERY_PRODUCTS);
            while (rs.next()) {
                orders.add(new Order(rs.getLong(1), rs.getLong(2), Order.State.values()[rs.getInt(3)]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public ArrayList<Order> querryOrders(long customerId) throws SQLException{
        ArrayList<Order> orders = new ArrayList<>();
        querryOrdersStatement.setLong(1, customerId);
        ResultSet rs = querryOrdersStatement.executeQuery();
        while (rs.next()) {
            orders.add(new Order(rs.getLong(1), rs.getLong(2), Order.State.values()[rs.getInt(3)]));
        }
        return orders;
    }

    public int updateOrderState(Order order) throws SQLException {
        updateOrderStatement.setInt(1,order.getOrderState().ordinal());
        updateOrderStatement.setLong(2,order.getId());

        return updateOrderStatement.executeUpdate();

    }

    public long insertOrder (Order order) throws SQLException {
        insertOrderStatement.setLong(1, order.getCustomerId());
        insertOrderStatement.setInt(2,order.getOrderState().ordinal());
        insertOrderStatement.executeUpdate();
        insertOrderStatement.getGeneratedKeys().next();
        long orderId = insertOrderStatement.getGeneratedKeys().getInt(1);
        for (SendableSubOrder s : order.getSendableSubOrders()) {
            insertSubOrderStatement.setLong(1,orderId);
            insertSubOrderStatement.setLong(2,s.getProductID());
            insertSubOrderStatement.setInt(3,s.getQuantity());
            insertSubOrderStatement.execute();
        }
        return orderId;
    }
    //done
    public Product querrySingleProduct(int id) {
        try {
            querrySingleProductStatement.setFloat(1, id);
            ResultSet resultSet = querrySingleProductStatement.executeQuery();
            String path = resultSet.getString(Queries.Product.COLUMN_ICON_LOCATION);
            return new Product(resultSet.getInt(Queries.COLUMN_ID),
                    (int) resultSet.getFloat(Queries.Product.COLUMN_PRICE),
                    resultSet.getString(Queries.Product.COLUMN_NAME),
                    resultSet.getString(Queries.Product.COLUMN_DESCRIPTION),
                    resultSet.getInt(Queries.Product.COLUMN_AVAILABLE_QTY),
                    path,
                    ImgUtils.imgToBytes(path),
                    resultSet.getInt(Queries.Product.COLUMN_LOCATION)
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    //done
    public ArrayList<Product> querryProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            ResultSet rs = statement.executeQuery(Queries.Product.QUERY_PRODUCTS);
            while (rs.next()) {
                products.add(new Product(rs.getInt(Queries.COLUMN_ID),
                        (int) rs.getFloat(Queries.Product.COLUMN_PRICE),
                        rs.getString(Queries.Product.COLUMN_NAME),
                        rs.getString(Queries.Product.COLUMN_DESCRIPTION),
                        rs.getInt(Queries.Product.COLUMN_AVAILABLE_QTY),
                        rs.getString(Queries.Product.COLUMN_ICON_LOCATION),
                        ImgUtils.imgToBytes(rs.getString(Queries.Product.COLUMN_ICON_LOCATION)),
                        rs.getInt(Queries.Product.COLUMN_LOCATION)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    //done
    public int insertProduct(Product p) throws SQLException {
        insertProductStatement.setString(1, p.getName());
        insertProductStatement.setString(2, p.getDescription());
        insertProductStatement.setInt(3, p.getPrice());
        insertProductStatement.setString(4, p.getIconPath());
        insertProductStatement.setInt(5, p.getLocation());
        insertProductStatement.setInt(6,p.getAvailable_qty());
        insertProductStatement.executeUpdate();
        insertProductStatement.getGeneratedKeys().next();
        return insertProductStatement.getGeneratedKeys().getInt(1);
    }

    public int insertCustomer(Customer c) throws SQLException {
        insertCustomerStatement.setString(1, c.getFirstName());
        insertCustomerStatement.setString(2, c.getLastName());
        insertCustomerStatement.setString(3, c.getEmail());
        insertCustomerStatement.setString(4, c.getPhone());
        insertCustomerStatement.setString(5, c.getBirthDate());
        insertCustomerStatement.setString(6, c.getPassword());
        insertCustomerStatement.executeUpdate();
        insertCustomerStatement.getGeneratedKeys().next();
        return insertCustomerStatement.getGeneratedKeys().getInt(1);
    }

    public int deleteProduct(Product p) {
        try {
            deleteProductStatement.setInt(1, p.getId());
            return deleteProductStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean confirmOrder(Order o) {
        for (SendableSubOrder s : o.getSendableSubOrders()) {
            try {
                querryProductQuantityStatement.setInt(1,s.getProductID());
                ResultSet rs = querryProductQuantityStatement.executeQuery();
                int qty = rs.getInt(Queries.Product.COLUMN_AVAILABLE_QTY);
                if (qty < s.getQuantity()) {

                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for (SendableSubOrder s : o.getSendableSubOrders()) {
            try {
                updateProductQuantityStatement.setInt(1,s.getQuantity());
                updateProductQuantityStatement.setInt(2,s.getProductID());
                updateProductQuantityStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("DBHandler.confirmOrder :: calling reload()...");
        Main.controllerProducts.reload();
        return true;
    }
    //done
    public int upadteProduct(Product old, Product updated, String iconLoc) throws SQLException {
        editProductStatement.setString(1, updated.getName());
        editProductStatement.setString(2, updated.getDescription());
        editProductStatement.setInt(3, updated.getPrice());
        editProductStatement.setString(4, iconLoc);
        editProductStatement.setInt(5, updated.getLocation());
        editProductStatement.setInt(6,updated.getAvailable_qty());
        editProductStatement.setInt(7, old.getId());
        return editProductStatement.executeUpdate();
    }

    public ArrayList<Customer> querryCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            ResultSet results = statement.executeQuery(Queries.Customers.QUERY_CUSTOMERS);
            while (results.next()) {
                customers.add(new Customer(results.getInt(Queries.COLUMN_ID),
                        results.getString(Queries.Customers.COLUMN_FIRST_NAME),
                        results.getString(Queries.Customers.COLUMN_LAST_NAME),
                        results.getString(Queries.Customers.COLUMN_BIRTH_DATE),
                        results.getString(Queries.Customers.COLUMN_EMAIL),
                        results.getString(Queries.Customers.COLUMN_PHONE)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public int deleteCustomer(Customer c) {
        try {
            deleteCustomerStatement.setInt(1, c.getId());
            return deleteCustomerStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int updateCustomer(Customer old, Customer updated) throws SQLException {
        editCustomerStatement.setString(1, updated.getFirstName());
        editCustomerStatement.setString(2, updated.getLastName());
        editCustomerStatement.setString(3, updated.getEmail());
        editCustomerStatement.setString(4, updated.getPhone());
        editCustomerStatement.setString(5, updated.getBirthDate());
        editCustomerStatement.setString(6, updated.getPassword());
        editCustomerStatement.setInt(7, old.getId());
        return editCustomerStatement.executeUpdate();
    }

    public long querryCustomerId(String phone, String password) {
        try {
            querryCustomerIdStatement.setString(1, phone);
            querryCustomerIdStatement.setString(2, password);
            ResultSet resultSet = querryCustomerIdStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Customer querryCustomer(int id) {
        try {
            querryCustomerStatement.setInt(1, id);
            ResultSet resultSet = querryCustomerStatement.executeQuery();
            if (resultSet.next()) {
                return new Customer(id,
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(6),
                        resultSet.getString(5),
                        resultSet.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
