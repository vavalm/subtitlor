package utilities;

import Exceptions.SubtitlesFileException;
import jdk.internal.util.xml.impl.Input;

import javax.servlet.http.Part;
import java.io.*;
import java.util.ArrayList;

public class UploadManager {

    /**
     * Analyze the "content-disposition" header + check if the "filename" parameter is present.
     *
     * @param part : Data received in the multipart / form-data POST request
     * @return The name of the file if the field treated is of type "file"
     * null if not
     */
    public static String getFileName(Part part) throws SubtitlesFileException {
        for (String contentDisposition : part.getHeader("content-disposition").split(";"))//content-disposition correspond aux données du formulaire
        {
            if (contentDisposition.trim().startsWith("filename"))//filename est présent
            {
                //Retire les guillemets du nom du fichier
                return contentDisposition.substring(contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        throw new SubtitlesFileException("Nom du fichier introuvable");
    }

    /**
     * Lis le fichier contenu dans le Part pour en extraire un tableau de String contenant chacune des lignes
     * @param part
     * @return
     * @throws IOException
     * @throws SubtitlesFileException
     */
    public ArrayList<String> PartToArray(Part part) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream()));
        ArrayList<String> res = new ArrayList<String>();


        try {
            String line;
            while ((line = reader.readLine()) != null) {
                res.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Impossible de fermer le flux d'entrée");
            }

        }
        return res;
    }
}
