package servlets;

import beans.Subtitle;
import beans.SubtitlesFile;
import dao.DaoFactory;
import dao.SubFilesDao;
import dao.SubFilesDaoSql;
import utilities.PropertiesPerso;
import utilities.SubtitlesHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/home")
@MultipartConfig( location = "/tmp", maxFileSize = 10*1024*1024, maxRequestSize = 5*10*1024*1024, fileSizeThreshold = 1024*1024)

public class Accueil extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        ServletContext context = getServletContext();
        DaoFactory daoFactory = DaoFactory.getInstance();
        SubFilesDao subFilesDao = new SubFilesDaoSql(daoFactory);

        ArrayList<String> files = subFilesDao.getFilesInDB(PropertiesPerso.DATABASE_NAME);

        req.setAttribute("subFiles", files);
        req.setAttribute("subtitlesFileName", PropertiesPerso.SUBFILE_FIELD_NAME);//Nom du champ du fichier
        this.getServletContext().getRequestDispatcher("/WEB-INF/accueil.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        int i = 0;
        String fieldName = "line";
        String fieldContent = "";
        String tableName = req.getParameter("filename");

        ArrayList<String> translatedSubtitles = new ArrayList<String>();
        DaoFactory daoFactory = DaoFactory.getInstance();
        SubFilesDao subFilesDao = new SubFilesDaoSql(daoFactory);
        SubtitlesFile subtitlesFile;

        //on récolte tous les sous-titres du formulaire
        while ((fieldContent = req.getParameter("line" + i)) != null){
            translatedSubtitles.add(fieldContent);
            i++;
        }

        subtitlesFile = subFilesDao.getSubtitlesFile(tableName);

        //On remplace tous les sous-titres par les nouveaux qui sont tirés du formulaire
        i=0;
        for (Subtitle sub :
                subtitlesFile.getSubtitles()) {
            sub.setTranslatedText(translatedSubtitles.get(i));
            i++;
        }

        subFilesDao.UpdateTranslatedSubtitles(tableName, subtitlesFile.getSubtitles());

        req.setAttribute("pageTitle", "Merci de votre participation");
        doGet(req, resp);
    }
}
