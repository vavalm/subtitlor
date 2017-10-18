package dao;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DaoFactory {
    private static final String PROPERTIES_FILE = "dao/dao.properties";
    private static String url = "jdbc:mysql://localhost:3306/";
    private static String username = "";
    private static String password = "";
    private static String bddName = "subtitles";
    private static DaoFactory instance;
    private static Connection connection;

    protected DaoFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DaoFactory getInstance() {

        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( PROPERTIES_FILE );
        try{
            properties.load(fichierProperties);
            url = properties.getProperty("URL");
            username = properties.getProperty("LOGIN");
            password = properties.getProperty("PWD");
            bddName = properties.getProperty("BDD_NAME");
        } catch (IOException e) {
            System.out.println("pas de fichier properties");
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");//chargement driver
        } catch (ClassNotFoundException e) {

        }
        if (instance == null){
            instance = new DaoFactory(
                    url + bddName, username, password);
        }

        return instance;
    }

    private static BoneCP setDaoConfig() throws SQLException {
        BoneCPConfig config = new BoneCPConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMinConnectionsPerPartition(1);
        config.setMaxConnectionsPerPartition(2);
        config.setPartitionCount(2);

        return new BoneCP(config);
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

    public SubFilesDaoSql getSubFilesDao() throws SQLException {
        return new SubFilesDaoSql(this);
    }

}
