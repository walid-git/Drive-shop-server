package database;

public abstract class Queries {
    public static final String DB_NAME = "./driveshop.db";
    public static final String COLUMN_ID = "id";

    public abstract class Order {
        public static final String TABLE_NAME = "orders";
        public static final String COLUMN_CUSTOMER_ID = "customer_id";
        public static final String COLUMN_STATE = "order_state";
        public static final String CREATE_TABLE_QUERRY = "" +
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CUSTOMER_ID + " INTEGER NOT NULL," +
                COLUMN_STATE + " INTEGER NOT NULL,"+
                " FOREIGN KEY("+COLUMN_CUSTOMER_ID+") REFERENCES "+Customers.TABLE_NAME+"("+COLUMN_ID+") " +
                "ON DELETE CASCADE);";
        public static final String QUERY_PRODUCTS = "" +
                "SELECT * FROM " + TABLE_NAME + " ORDER BY "+COLUMN_ID+" DESC ;";
    }

    public abstract class SubOrder {
        public static final String TABLE_NAME = "suborders";
        public static final String COLUMN_ORDER_ID = "order_id";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String CREATE_TABLE_QUERRY = "" +
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ORDER_ID + " INTEGER NOT NULL," +
                COLUMN_PRODUCT_ID + " INTEGER NOT NULL," +
                COLUMN_QUANTITY + " INTEGER NOT NULL," +
                " CHECK("+COLUMN_QUANTITY+" > 0), " +
                "FOREIGN KEY("+COLUMN_ORDER_ID+") REFERENCES "+Order.TABLE_NAME+"("+COLUMN_ID+") ON DELETE CASCADE," +
                "FOREIGN KEY("+COLUMN_PRODUCT_ID+") REFERENCES "+Product.TABLE_NAME+"("+COLUMN_ID+") ON DELETE CASCADE" +
                ");";
    }
    public abstract class Product {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME = "product_name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_AVAILABLE_QTY = "available_qty";
        public static final String COLUMN_ICON_LOCATION = "icon_loc";
        public static final String COLUMN_LOCATION = "location";
        public static final String CREATE_TABLE_QUERRY = "" +
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " VARCHAR(20) NOT NULL, " +
                COLUMN_DESCRIPTION + " VARCHAR(40), " +
                COLUMN_PRICE + " FLOAT NOT NULL, " +
                COLUMN_ICON_LOCATION + " VARCHAR(150), " +
                COLUMN_LOCATION + " INTEGER NOT NULL UNIQUE, " +
                COLUMN_AVAILABLE_QTY + " INTEGER NOT NULL,"+
                "CHECK("+ COLUMN_LOCATION +" >= 0 AND "+
                COLUMN_PRICE+" >= 0 AND "+ COLUMN_AVAILABLE_QTY +" > 0)"+
                ");";
        public static final String QUERY_PRODUCTS = "" +
                "SELECT * FROM " + TABLE_NAME + ";";
    }

    public abstract class Customers {
        public static final String TABLE_NAME = "customers";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_BIRTH_DATE = "birth_date";
        public static final String COLUMN_PASSWORD = "password";

        public static final String CREATE_TABLE_QUERRY = "" +
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FIRST_NAME + " VARCHAR(25) NOT NULL, " +
                COLUMN_LAST_NAME + " VARCHAR(25) NOT NULL, " +
                COLUMN_PHONE + " VARCHAR(13) NOT NULL UNIQUE, " +
                COLUMN_EMAIL + " VARCHAR(50), " +
                COLUMN_BIRTH_DATE + " VARCHAR(10), " +
                COLUMN_PASSWORD + " VARCHAR(256) NOT NULL);";
        public static final String QUERY_CUSTOMERS = "" +
                "SELECT * FROM " + TABLE_NAME + ";";
    }

}
