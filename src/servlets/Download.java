package servlets;

import beans.SubtitleFile;
import dao.DaoFactory;
import dao.SubFilesDao;
import dao.SubFilesDaoSql;
import utilities.SubtitlesHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/download/*")
public class Download extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        SubFilesDao subFilesDao = null;
        SubtitleFile subtitleFile = null;
        SubtitlesHandler subtitlesHandler = null;
        File file = null;
        Boolean exportOriginal = true;

        try {
            subFilesDao = daoFactory.getSubFilesDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        subtitlesHandler = new SubtitlesHandler();

        String uri = req.getRequestURI();
        String[] split = uri.split("/");
        int id = Integer.parseInt(split[split.length-1]);
        if (split[split.length - 2].equals("translated")) {
            exportOriginal = false;
        }


        try {
            subtitleFile = subFilesDao.getSubtitleFile(id);
            //creation du fichier dans le dossier downloads
            file = subtitlesHandler.getOriginalSrt(subtitleFile, this.getServletContext().getRealPath("/"), exportOriginal);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //on redirige vers l'url du fichier créé pour le télécharger
        resp.sendRedirect("/downloads/" + subtitleFile.getName() + ".srt");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
