package dao;

import beans.SubtitlesFile;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {
    private static String url = "jdbc:mysql://localhost:3306/";
    private static String username = "root";
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
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {

        }
        if (instance == null){
            instance = new DaoFactory(
                    url + bddName, username, password);
        }

        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null){
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
