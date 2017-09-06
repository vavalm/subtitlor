package servlets;

import Exceptions.SubtitlesFileException;
import beans.SubtitlesFile;
import dao.DaoFactory;
import dao.SubFilesDao;
import dao.SubFilesDaoSql;
import utilities.PropertiesPerso;
import utilities.SubtitlesHandler;
import utilities.UploadManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet("/edit")
@MultipartConfig( location = "C:\\tmp", maxFileSize = 10*1024*1024, maxRequestSize = 5*10*1024*1024, fileSizeThreshold = 1024*1024)
public class EditSubtitle extends HttpServlet {
    private static final long serialVersionUID = 1L;
    //Correspond au nom du champ qui va accueillir les fichiers de sous-titre dans le formulaire

    private String pageTitle;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.sendRedirect(request.getContextPath() + "/home");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServletContext context = getServletContext();
        String error = "";
        SubtitlesFile subtitlesFile = null;
        SubtitlesHandler subtitlesHandler = null;//permet de générer les sous titres

        Part part = request.getPart(PropertiesPerso.SUBFILE_FIELD_NAME);//récupère le part du fichier
        DaoFactory daoFactory = DaoFactory.getInstance();
        SubFilesDao subFilesDao = daoFactory.getSubFilesDao();
        UploadManager uploadManager = new UploadManager();

/////////////////////        Le formulaire d'un nouveau fichier a été submit
        if (request.getParameter("idform").equals("upload")) {
            pageTitle = "Edition de votre nouveau fichier importé";

            subtitlesHandler = new SubtitlesHandler();
            subFilesDao = daoFactory.getSubFilesDao();
            subtitlesFile = subtitlesHandler.toSubFile(SubFilesDaoSql.ClearSpecialChar(UploadManager.getFileName(part)),uploadManager.PartToArray(part));


        }

        /////////////////////        On veut éditer un fichier déjà présent en BDD

        else if (request.getParameter("idform").equals("edit")) {
            pageTitle = "Edition d'une traduction en base de données";

            String fileName = request.getParameter("fileInBdd");//nom du fichier à extraire de la bdd
            subtitlesFile = subFilesDao.getSubtitlesFile(fileName);
        }


        subtitlesFile.setSubtitles(subtitlesFile.getSubtitles());//récupère les sous-titres originaux

        request.setAttribute("error", error);
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("filename", subtitlesFile.getName());
        request.setAttribute("subtitles", subtitlesFile.getSubtitles());
        this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
    }

}
