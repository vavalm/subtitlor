package Test;

import Exceptions.SubtitlesFileException;
import beans.Film;
import beans.Subtitle;
import beans.SubtitleFile;
import dao.DaoFactory;
import dao.SubFilesDaoSql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestSubFilesDaoSql {
    public static DaoFactory daoFactory;
    public static Connection connection;
    public static SubFilesDaoSql subFilesDaoSql;

    public static void main(String[] args) throws SQLException, SubtitlesFileException {
        daoFactory = DaoFactory.getInstance();
        connection = daoFactory.getConnection();
        subFilesDaoSql = new SubFilesDaoSql(daoFactory);

//        TestUploadSubtitleFile();
        TestUpdateTranslatedSubtitle();
    }

    public static void TestGetFilms(){
        ArrayList<Film> films = subFilesDaoSql.getFilms();

        for (Film film : films
             ) {
            System.out.println(film.getName());
        }
    }

    public static void TestUploadSubtitleFile() throws SQLException, SubtitlesFileException {
        SubtitleFile subtitleFile = new SubtitleFile("testFilm4");

        ArrayList<Subtitle> subtitles = new ArrayList<Subtitle>();

        Subtitle subtitle1 = new Subtitle();
        subtitle1.setText("encore 1");
        subtitle1.setStartTime("00:00:27,029");
        subtitle1.setEndTime("00:00:28,030");
        subtitles.add(subtitle1);

        Subtitle subtitle2 = new Subtitle();
        subtitle2.setText("encore 2");
        subtitle2.setStartTime("00:00:29,029");
        subtitle2.setEndTime("00:00:30,030");
        subtitles.add(subtitle2);


        subtitleFile.setSubtitles(subtitles);
        subtitleFile.setIdFilm(5);

        subFilesDaoSql.UploadSubtitleFile(subtitleFile);
        System.out.println("id du film : " + subtitleFile.getIdFilm());

    }

    public static void TestUpdateTranslatedSubtitle() {

        SubtitleFile subtitleFile = new SubtitleFile("testFilm4");

        ArrayList<Subtitle> subtitles = new ArrayList<Subtitle>();

        Subtitle subtitle1 = new Subtitle();
        subtitle1.setText("encore 1");
        subtitle1.setTranslatedText("encore 1 mais traduit");
        subtitle1.setStartTime("00:00:27,029");
        subtitle1.setEndTime("00:00:28,030");
        subtitles.add(subtitle1);

        Subtitle subtitle2 = new Subtitle();
        subtitle2.setText("encore 2");
        subtitle2.setTranslatedText("encore 2 mai traduit");
        subtitle2.setStartTime("00:00:29,029");
        subtitle2.setEndTime("00:00:30,030");
        subtitles.add(subtitle2);


        subtitleFile.setSubtitles(subtitles);
        subtitleFile.setIdFilm(5);

        subFilesDaoSql.UpdateTranslatedSubtitles(subtitleFile.getIdFilm(), subtitleFile.getSubtitles());
    }
}
