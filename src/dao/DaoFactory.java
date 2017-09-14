package dao;

import beans.SubtitlesFile;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {
    private static String url = "jdbc:mysql://localhost:3306/";
    private static String username = "root";
    private static String password = "root";
    private static String bddName = "subtitles";
    private static DaoFactory instance;
    private static BoneCP boneCP;
    private static Connection connection;

    protected DaoFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DaoFactory getInstance() {

        try {
            Class.forName("com.mysql.jdbc.Driver");//chargement driver
//            boneCP = setDaoConfig();
        } catch (ClassNotFoundException e) {

        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
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
        if (connection == null){
//            connection = boneCP.getConnection();
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

    public SubFilesDaoSql getSubFilesDao(){
        return new SubFilesDaoSql(this);
    }

    public SubFilesDaoDisk getDiskDao() throws IOException, ServletException {
        return new SubFilesDaoDisk(this);
    }
}
