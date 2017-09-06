package dao;

import Exceptions.SubtitlesFileException;
import beans.Subtitle;
import beans.SubtitlesFile;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubFilesDaoDisk implements SubFilesDao {

    private static final int BUFFER_SIZE = 10240; // Taille de la mémoire tampon : 10 ko
    protected File subtitlesFile;
    private Part file_part;
    private String sendMessage;//Message de succès (ou échec) quand l'utilisateur a envoyé l'upload
    private Map<String, String> errors = new HashMap<String, String>();//Tableau contenant les erreurs

    public SubFilesDaoDisk(DaoFactory daoFactory) throws IOException, ServletException {
        this.subtitlesFile = subtitlesFile;
//        this.file_part = part;
    }


    public void saveUploadSubFile() {
//
//        InputStream fileContent = null;
//        String fileName = subtitlesFile.getName();
//
//        try {
//            if (fileName != null && !fileName.isEmpty()) {
//                /* Retire le path superflu que pourrait mettre certains navigateurs internet
//            * Comme Internet Explorer */
//                fileName = fileName.substring(fileName.lastIndexOf('/') + 1)
//                        .substring(fileName.lastIndexOf('\\') + 1);
//
//                fileContent = file_part.getInputStream();
//            }
//
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//            setError(SubFormConstants.SUBFILE_FIELD_NAME, "Les données envoyées sont trop volumineuses.");
//        } catch (IOException e) { //Répertoire inexsitant, droits d'accès restreints, ...
//            e.printStackTrace();
//            setError(SubFormConstants.SUBFILE_FIELD_NAME, "Erreur de répertoire sur le serveur");
//        }
//        if (errors.isEmpty()) {
//            try {
//                emptyFileVerif(fileName, fileContent);
//            } catch (Exception e) {
//                setError(SubFormConstants.SUBFILE_FIELD_NAME, "Fichier non valide : " + e.getMessage());
//            }
//            if (errors.isEmpty()) {
//                try {
//                    writeOnDisk(fileContent);
//                } catch (Exception e) {
//                    setError(SubFormConstants.SUBFILE_FIELD_NAME, "Erreur lors de l'écriture du fichier sur le disque");
//                }
//            }
//            if (errors.isEmpty()) {
//                sendMessage = "Le fichier a bien été envoyé";
//            } else
//                sendMessage = "Erreur lors de l'envoi du fichier";
//        }
    }

    /**
     * Writes the uploaded file to disk at path PATH_MEDIA_DIR. Place the file in the correct folder.
     *
     * @param inputStream : Stream of data received in the multipart / form-data POST request
     */
    private void writeOnDisk(InputStream inputStream) throws IOException, SubtitlesFileException {

        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        createDirectories();//Créer les dossiers de stockage s'ils n'existent pas
        try {
            in = new BufferedInputStream(inputStream, BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(subtitlesFile.getAbsolutePath()));

            byte[] tampon = new byte[BUFFER_SIZE];
            int longueur;
            /*
            Lecture du fichier uploadé
            Ecriture de son contenu sur le disque
             */
            while ((longueur = in.read(tampon)) > 0) {
                out.write(tampon, 0, longueur);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Impossible de fermer le flux d'entrée");
            }

            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Impossible de fermer le flux de sortie");
            }
        }

    }

    /**
     * Adds an error message associated with a form field.
     *
     * @param field   : Field targeted by the error
     * @param message : The error message
     */
    private void setError(String field, String message) {
        errors.put(field, message);
    }


    /**
     * Test the file uploaded by the user.
     * If the file does not have a name or content it is rejected.
     *
     * @param fileName    : Name of the file to upload
     * @param fileContent : Content of the file to upload
     * @throws Exception
     */
    private void emptyFileVerif(String fileName, InputStream fileContent) throws Exception {
        if (fileName == null || fileContent == null) {
            throw new SubtitlesFileException("Merci de sélectionner un fichier valide à envoyer.");
        }

        String typeMime = file_part.getContentType();//Récupération du MIME par l'extension pour connaitre le type

        if (!fileName.contains(".srt") && !fileName.contains(".ssa") && !fileName.contains(".sub")) {
            throw new SubtitlesFileException("Merci de sélectionner un fichier de sous titres avec extension sub, ssa ou srt");
        }
        if (!typeMime.contains("text") && !typeMime.contains("srt") && !typeMime.contains("application/octet-stream")){
            //si le MIME type ne correspond pas à un fichier srt
            throw new SubtitlesFileException("Merci de sélectionner un fichier SRT à envoyer.");
        }

    }

    /**
     * Creation of the tree for the storage of uploaded files.
     */
    public void createDirectories() throws SubtitlesFileException {
        if (!new File(subtitlesFile.getParent()).exists()) {
            System.out.println("if");
            new File(subtitlesFile.getParent()).mkdirs();
        }
    }


    @Override
    public void saveUploadSubFile(SubtitlesFile subtitlesFile) throws SubtitlesFileException {

    }

    @Override
    public SubtitlesFile getSubtitlesFile(String fileName) {
        return null;
    }

    @Override
    public ArrayList<String> getFilesInDB(String dataBaseName) {
        return null;
    }

    @Override
    public void UpdateTranslatedSubtitles(String tableName, ArrayList<Subtitle> subtitles) {

    }

}
