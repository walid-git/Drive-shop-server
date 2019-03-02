package database;

public abstract class Queries {
    public static final String DB_NAME = "driveshop.db";
    public static final String COLUMN_ID = "id";


    public abstract class Order {
        public static final String TABLE_NAME = "orders";
        public static final String COLUMN_CUSTOMER_ID = "customer_id";
        public static final String CREATE_TABLE_QUERRY = "" +
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CUSTOMER_ID + " INTEGER);";

    }

    public abstract class SubOrder {
        public static final String TABLE_NAME = "suborders";
        public static final String COLUMN_ORDER_ID = "order_id";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String CREATE_TABLE_QUERRY = "" +
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ORDER_ID + " INTEGER ," +
                COLUMN_PRODUCT_ID + " INTEGER ," +
                COLUMN_QUANTITY + " INTEGER);";
    }

    public abstract class Product {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME = "product_name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_ICON_LOCATION = "icon_loc";
        public static final String CREATE_TABLE_QUERRY = "" +
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " VARCHAR(20), " +
                COLUMN_DESCRIPTION + " VARCHAR(40), " +
                COLUMN_PRICE + " FLOAT, " +
                COLUMN_ICON_LOCATION + " VARCHAR(150));";
        public static final String QUERY_PRODUCTS = "" +
                "SELECT * FROM " + TABLE_NAME + ";";

    }


}
