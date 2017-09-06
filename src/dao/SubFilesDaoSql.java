package dao;

import Exceptions.SubtitlesFileException;
import beans.Subtitle;
import beans.SubtitlesFile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubFilesDaoSql implements SubFilesDao {
    DaoFactory daoFactory;

    public SubFilesDaoSql(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Fonction qui va enregistrer un fichier de sous-titres en BDD.
     *
     * @param subtitlesFile
     * @throws SubtitlesFileException
     */
    @Override
    public void saveUploadSubFile(SubtitlesFile subtitlesFile) throws SubtitlesFileException {
        boolean exist;

        String tableName = ClearSpecialChar(subtitlesFile.getName());

            System.out.println("creation de la table");
            //La table a le nom du fichier qui aura été reformaté par ClearSpecialCara
            exist = createTable(tableName);
            //Si la table n'existait pas déjà, on enregistre les sous-titres
            if (exist == false) {
                SaveSubFilesInDB(tableName, subtitlesFile);
            } else { //si la table existait alors on met à jour les infos
                UpdateTranslatedSubtitles(tableName, subtitlesFile.getSubtitles());
            }
    }

    /**
     * Permet de reconstituer le bean SubtitleFiles à partir du fichier sélectionné en bdd
     *
     * @param fileName : le fichier à récupérer en bdd
     * @return
     */
    public SubtitlesFile getSubtitlesFile(String fileName) {
        fileName = ClearSpecialChar(fileName);//on enlève tous les caractères spéciaux comme les noms dans la bdd
        SubtitlesFile subtitlesFile = new SubtitlesFile(fileName);
        ArrayList<Subtitle> subtitles = new ArrayList<Subtitle>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;



//        Requête préparé pour récupérer la table ayant pour nom fileName
        String req = "SELECT * FROM " + fileName;

        try {
            connection = daoFactory.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(req); //on récupère les sous-titres

            while (resultSet.next()) {
                Subtitle subtitle = new Subtitle();
                subtitle.setNumber(resultSet.getInt("sub_number"));
                subtitle.setText(resultSet.getString("subtitle"));
                subtitle.setTranslatedText(resultSet.getString("translated_subtitle"));
                subtitle.setStartTime(resultSet.getString("start_time"));
                subtitle.setEndTime(resultSet.getString("end_time"));

                subtitles.add(subtitle);
            }

            subtitlesFile.setSubtitles(subtitles);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur lors du getSubtitlesFile");
        }
        finally {
            closeConnection(resultSet, statement, connection);
        }

        return subtitlesFile;
    }

    /**
     * Créer une table si elle n'existe pas déjà
     *
     * @param tableName : nom de la table à créer
     * @throws SQLException
     */
    private boolean createTable(String tableName)  {
        tableName = ClearSpecialChar(tableName);
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        //On vérifie l'existance de la table et si elle existe on renvoie true
        String state = "SHOW TABLES ";


//        connection = daoFactory.getConnection();
        state = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n" +
                "                  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "                  `sub_number` int(11) NOT NULL COMMENT 'Correspond au numéro de sous-titre',\n" +
                "                 `subtitle` varchar(300) NOT NULL,\n" +
                "                 `translated_subtitle` varchar(300) DEFAULT NULL,\n" +
                "                 `start_time` varchar(12) NOT NULL COMMENT 'démarrage sous titre dans le temps',\n" +
                "                 `end_time` varchar(12) NOT NULL COMMENT 'fin du sous titre',\n" +
                "                   PRIMARY KEY (id)\n" +
                "                ) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Table sous-titres de la video de présentation'";


        try {
            connection = daoFactory.getConnection();
            resultSet = connection.createStatement().executeQuery(state);
            while (resultSet.next()) {
                if (resultSet.getString("Tables_in_subtitles").equals(tableName)) {
                    System.out.println("Table déjà existante");
                    return true;
                }
            }
            statement = connection.createStatement();
            statement.execute(state);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur creation table");
        }

        return false;
    }

    public static String ClearSpecialChar(String str) {
        Pattern pattern = Pattern.compile("\\W");//regex pour selectionner tous les caractères spéciaux
        Matcher matcher = pattern.matcher(str);//on  place la chaine à traiter
        matcher.replaceAll("_"); //on définit le caractère de remplacement
        return str;
    }


    /**
     * On enregistre les sous-titres en base de données.
     * Cette fonction sera utilisée dès lors qu'un utilisateur upload un nouveau fichier en BDD
     *
     * @param tableName     : le nom de la table dans laquelle on enregistre les données
     * @param subtitlesFile : les sous-titres à enregistrer
     */
    private void SaveSubFilesInDB(String tableName, SubtitlesFile subtitlesFile) {
        String req = "INSERT INTO " + tableName +
                "(`sub_number`, `subtitle`, `translated_subtitle`, `start_time`, `end_time`) VALUES (?,?,?,?,?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = daoFactory.getConnection();
            //boucle de parcours de tous les sous-titres du fichier de sous-titres
            for (int i = 0; i < subtitlesFile.getSubtitles().size(); i++) {
                int number = subtitlesFile.getSubtitles().get(i).getNumber();
                String text = subtitlesFile.getSubtitles().get(i).getText();
                String translatedText = subtitlesFile.getSubtitles().get(i).getTranslatedText();
                String startTime = subtitlesFile.getSubtitles().get(i).getStartTime();
                String endTime = subtitlesFile.getSubtitles().get(i).getEndTime();
                preparedStatement = connection.prepareStatement(req);

                preparedStatement.setInt(1, number);
                preparedStatement.setString(2, text);
                preparedStatement.setString(3, translatedText);
                preparedStatement.setString(4, startTime);
                preparedStatement.setString(5, endTime);

                preparedStatement.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SaveSubFilesInDB : fail");
        }
    }


    /**
     * Met à jour la traduction en base de données pour un fichier donné
     *
     * @param tableName : Nom de la table (qui est le nom du fichier reformaté) à traiter
     * @param subtitles : Les sous-titres contenants le texte traduit à enregistrer
     */
    public void UpdateTranslatedSubtitles(String tableName, ArrayList<Subtitle> subtitles) {
        String req = "UPDATE " + tableName + "SET `translated_subtitle`=? WHERE `subtitle`= ?";
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement(req);

            //Parcours de tous les sous-titres pour mettre à jour la traduction en BDD
            for (Subtitle subtitle :
                    subtitles) {
                try {
                    preparedStatement.setString(1, subtitle.getTranslatedText());
                    preparedStatement.setString(2, subtitle.getText());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Erreur requête préparée UPDATE pour enregistrer la mise à jour des sous-titres traduits. Echec à l'indice i = " + subtitles.indexOf(subtitle));
                    System.out.println("requete : " + req);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur dans l'enregistrement de la traduction");
        }

        finally {
            closeConnection(null, preparedStatement, connection);
        }

    }

    /**
     * Permet d'avoir une liste des tables (et donc des fichiers de sous-titres)
     *
     * @param dataBaseName : Base de données à traiter
     * @return Liste des tables de dataBaseName
     */
    public ArrayList<String> getFilesInDB(String dataBaseName) {
        String req = "SHOW TABLES IN " + dataBaseName;
        ArrayList<String> res = new ArrayList<String>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = daoFactory.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(req);
            while (resultSet.next()) {
                res.add(resultSet.getString("Tables_in_" + dataBaseName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(resultSet, statement, connection);
        }
        return res;
    }

    private void closeConnection(ResultSet resultSet, Statement statement, Connection connection){
//        if (resultSet != null){
//            try {
//                resultSet.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (resultSet != null){
//            try {
//                statement.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (resultSet != null){
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
