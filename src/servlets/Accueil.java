package servlets;

import beans.Film;
import beans.Subtitle;
import beans.SubtitleFile;
import dao.DaoFactory;
import dao.SubFilesDao;
import dao.SubFilesDaoSql;
import utilities.PropertiesPerso;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/home")
@MultipartConfig( location = "/tmp", maxFileSize = 10*1024*1024, maxRequestSize = 5*10*1024*1024, fileSizeThreshold = 1024*1024)

public class Accueil extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        DaoFactory daoFactory = DaoFactory.getInstance();
        SubFilesDao subFilesDao = null;

        try {
            subFilesDao = new SubFilesDaoSql(daoFactory);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Film> films = subFilesDao.getFilms();

        req.setAttribute("films", films);
        req.setAttribute("subtitlesFileName", PropertiesPerso.SUBFILE_FIELD_NAME);//Nom du champ du fichier
        this.getServletContext().getRequestDispatcher("/WEB-INF/accueil.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        int i = 0;
        String fieldContent;
        ArrayList<String> translatedSubtitles = new ArrayList<String>();
        SubtitleFile subtitlesFile = null;

        int filmId = Integer.parseInt(req.getParameter("filmId")); //Récupération idfilm via champ caché du forumulaire


        DaoFactory daoFactory = DaoFactory.getInstance();
        SubFilesDao subFilesDao = null;
        try {
            subFilesDao = new SubFilesDaoSql(daoFactory);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //on récolte tous les sous-titres du formulaire (dans tous les champ lineX du formulaire)
        while ((fieldContent = req.getParameter("line" + i)) != null){
            translatedSubtitles.add(fieldContent);
            i++;
        }

        try {
            subtitlesFile = subFilesDao.getSubtitleFile(filmId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Subtitle sub :
                subtitlesFile.getSubtitles()) {
            System.out.println(sub.getText());
        }

        //On remplace tous les sous-titres par les nouveaux qui sont tirés du formulaire
        i=-1;
        int size = subtitlesFile.getSubtitles().size();
        for (Subtitle sub : subtitlesFile.getSubtitles()) {
            i++;
            if (i < size) {
                sub.setTranslatedText(translatedSubtitles.get(i));
            }
        }


        subFilesDao.UpdateTranslatedSubtitles(subtitlesFile.getIdFilm(), subtitlesFile.getSubtitles());

        req.setAttribute("pageTitle", "Merci de votre participation");
        doGet(req, resp);
    }
}
