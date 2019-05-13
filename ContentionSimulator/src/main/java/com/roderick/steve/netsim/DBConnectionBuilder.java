package com.roderick.steve.netsim;
/************************************************************************
 *  @author: SRoderick                                                  *
 *  Date: 21/12/2018                                                    *
 ***********************************************************************/

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Class to spool results of the Network simulator to a Derby (Java) database
 * @author SRoderick
 *
 */
public class DBConnectionBuilder {

   public String dbURI = "";
   protected String dbDriver = "org.apache.derby.jdbc.ClientDriver";
   private static DBConnectionBuilder Instance = null;
   private BasicDataSource basicDataSource = new BasicDataSource();
   private int conCount = 0;
   

   /**
    * Constructor
    */
   private DBConnectionBuilder() {
   }

   /**
    * Constructor Sets the properties supplied.
    * 
    * @param properties an object holding all the properties required.
    */
   private DBConnectionBuilder(Properties properties) {
      setProperties(properties);
   }

   /**
    * set the properties for access to the SIM database.
    * 
    * @param properties an object holding all the properties required.
    */
   public void setProperties(Properties properties) {

      String dbConnectionString = properties.getProperty(SimConstants.DB_CONNECTION_STRING);
      String dbName = properties.getProperty(SimConstants.DATABASE_NAME);
      String dbUserName = properties.getProperty(SimConstants.DATABASE_USERNAME);
      String dbPassword = properties.getProperty(SimConstants.DATABASE_PASSWORD);
      String dbDriver = properties.getProperty(SimConstants.DATABASE_DRIVER);
      if((dbConnectionString == null || dbConnectionString.length() == 0) || (dbName == null || dbName.length() == 0)
            || (dbDriver == null || dbDriver.length() == 0)) {
         dbURI = "";
         dbDriver = "org.apache.derby.jdbc.ClientDriver";
      }
      else {
         dbURI = (dbConnectionString.contains("jdbc:derby:memory")) ? dbConnectionString
               : dbConnectionString + "/" + dbName + ";";
         if(dbUserName != null && !dbUserName.equals("") && dbPassword != null && !dbPassword.equals("")) {
            dbURI = dbURI + "user=" + dbUserName + ";password=" + dbPassword + ";";
         }
         dbURI = dbURI + "create=true";

      }

      basicDataSource.setDriverClassName(dbDriver);
      basicDataSource.setUrl(dbURI);
      basicDataSource.setUsername(dbUserName);
      basicDataSource.setPassword(dbPassword);

   }

   /**
    * Repoints connection - used for testing purposes
    * @param URL
    */
   public void destory() {
      try {
         basicDataSource.close();
         Instance = null;
      }
      catch (SQLException e) {
      }
   }

   /**
    * returns a static instance of the database connection class
    * @return
    */
   public static synchronized DBConnectionBuilder getInstance() {

      if(Instance == null) {
         Instance = new DBConnectionBuilder();
      }
      return Instance;
   }

   /**
    * @return
    */
   public synchronized Connection getConnection() {
      Connection connection = null;

      try {
         connection = basicDataSource.getConnection();
         connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
         
         if(conCount != basicDataSource.getNumActive()) {
            conCount = basicDataSource.getNumActive();
         }
      }
      catch (SQLException e) {

      }
      return connection;
   }

}

