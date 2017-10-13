package servlets;

import beans.Subtitle;
import beans.SubtitleFile;
import dao.DaoFactory;
import dao.SubFilesDao;
import utilities.PropertiesPerso;
import utilities.SubtitlesHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/edit")
@MultipartConfig( location = "/tmp", maxFileSize = 10*1024*1024, maxRequestSize = 5*10*1024*1024, fileSizeThreshold = 1024*1024)
public class EditSubtitle extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String pageTitle;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.sendRedirect(request.getContextPath() + "/home");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        //Initialisation des variables
        SubtitleFile subtitleFile = null;
        Part part = request.getPart(PropertiesPerso.SUBFILE_FIELD_NAME);//récupère le part du fichier
        String filmName = request.getParameter("filmName");//récupération du nom du film dans le formulaire

        DaoFactory daoFactory = DaoFactory.getInstance();
        SubFilesDao subFilesDao = null;
        SubtitlesHandler subtitlesHandler;

        try {
            subFilesDao = daoFactory.getSubFilesDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

/////////////////////        Le formulaire d'un nouveau fichier a été submit
        if (request.getParameter("idform").equals("upload")) {
            pageTitle = "Edition de votre nouveau fichier importé";

            subtitlesHandler = new SubtitlesHandler();
            subtitleFile = subtitlesHandler.PartToSubFile(part, filmName); //Transformation en fichier de sous-titres

            if (subFilesDao.isInBdd(filmName)) {
                pageTitle = "Edition d'une traduction en base de données";
            }

            subFilesDao.UploadSubtitleFile(subtitleFile);// Enregistrement en bdd
        }

/////////////////////        On veut éditer un fichier déjà présent en BDD

        else if (request.getParameter("idform").equals("edit")) {
            pageTitle = "Edition d'une traduction en base de données";

            int idFilm = Integer.parseInt(request.getParameter("filmId"));//id fu film à extraire de la bdd

            try {
                subtitleFile = subFilesDao.getSubtitleFile(idFilm);
            } catch (SQLException e) { }
        }


//        subtitleFile.setSubtitles(subtitleFile.getSubtitles());//récupère les sous-titres originaux

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("subtitleFile", subtitleFile);
        this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
    }

}
