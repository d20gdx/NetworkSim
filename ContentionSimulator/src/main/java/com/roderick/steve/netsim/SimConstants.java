package com.roderick.steve.netsim;

public class SimConstants {
    // database connection properties
    public final static String DB_CONNECTION_STRING = "dbConnectionString";
    public final static String TEST_DB_CONNECTION_STRING = "jdbc:derby:memory:simTest;create=true;user=sim;password=sim";
    public final static String TEST_DB_DRIVER_NAME = "org.apache.derby.jdbc.EmbeddedDriver";
    public final static String TEST_DB_CONNECTION_STRING_DISCONNECT = "jdbc:derby:memory:simTest;user=sim;password=sim;drop=true";
    public final static String DATABASE_DRIVER = "JDBCDriver";
    public final static String DATABASE_NAME = "dbName";
    public final static String DATABASE_USERNAME = "dbUsername";
    public final static String DATABASE_PASSWORD = "dbPassword";
    public final static String PROPERTIES_FILE_NAME = "database.properties";

}
