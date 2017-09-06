package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Success extends HttpServlet {
    private String pageTitle;

    public static void createDirectories(String path) {
        if (!new File(path + "/Subtitlor").exists()) {
            // Créer le dossier avec tous ses parents
            new File(path).mkdirs();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        pageTitle = "Merci d'avoir édité vos sous titres. Vous pouvez continuer plus tard.";
        List<String> subtitles_translated = new ArrayList<String>();

//        Stockage de toutes les lignes du formulaire
        String name_line = "line";
        int i = 0;

        while (req.getParameter(name_line + Integer.toString(i)) != null) {
            subtitles_translated.add(req.getParameter(name_line + Integer.toString(i)));
            i++;
        }

//       Envoie des variables
        req.setAttribute("subtitles_translated", subtitles_translated);
//        req.setAttribute("subtitles",original_subtitles.getSubtitles());
        req.setAttribute("pageTitle", pageTitle);
        this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(req, resp);
    }

    private String getAppDataSubtitlorPath() {
        String subtitlor = "";

        String workingDirectory;
//here, we assign the name of the OS, according to Java, to a variable...
        String OS = (System.getProperty("os.name")).toUpperCase();
//to determine what the workingDirectory is.
//if it is some version of Windows
        if (OS.contains("WIN")) {
            //it is simply the location of the "AppData" folder
            workingDirectory = System.getenv("AppData");
            subtitlor = "\\subtitlor";
        }
//Otherwise, we assume Linux or Mac
        else {
            //in either case, we would start in the user's home directory
            workingDirectory = System.getProperty("user.home");
            //if we are on a Mac, we are not done, we look for "Application Support"
            workingDirectory += "/Library/Application Support";
            subtitlor = "/subtitlor";
        }
        createDirectories(workingDirectory);//On créer le répertoire Subtitlor s'il n'existe aps dans AppData
        return (workingDirectory + subtitlor);
    }
}