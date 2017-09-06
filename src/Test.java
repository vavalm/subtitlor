//import Exceptions.SubtitlesFileException;
//import beans.Subtitle;
//import beans.SubtitlesFile;
//import dao.DaoFactory;
//import dao.SubFilesDao;
//import dao.SubFilesDaoSql;
//import utilities.SubtitlesHandler;
//
//import java.io.File;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class Test {
//
//    public static void main(String[] args) {
//        TestEnregistrementFichier();
//
//    }
//
//    public static void TestEnregistrementFichier(){
//        DaoFactory daoFactory = DaoFactory.getInstance();
//        SubFilesDaoSql subFilesDao = new SubFilesDaoSql(daoFactory);
//        SubtitlesFile subtitlesFile = null;
//        File file = new File("D:\\vavalm\\Google Drive\\Développement\\Java\\subtitlor-project\\web\\subfiles\\password_presentation.srt");
//        ArrayList<Subtitle> subtitles;
//        SubtitlesHandler handler = null;
//
//        try {
//            handler = new SubtitlesHandler(file);
//        } catch (SubtitlesFileException e) {
//            e.printStackTrace();
//            System.out.println("erreur récup fichier par le handler");
//        }
//
//        subtitlesFile = handler.toSubFile("test", handler.getSubtitles());
//        try {
//            subFilesDao.saveUploadSubFile(subtitlesFile);
//        } catch (SubtitlesFileException e) {
//            e.printStackTrace();
//            System.out.println("Erreur saveuploadfile in main");
//        }
//    }
//
//
//
//    public static void affichageTables(ResultSet resultSet){
//        try {
//            while (resultSet.next()){
//                System.out.println(resultSet.getString(1));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
