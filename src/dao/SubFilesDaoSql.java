package dao;

import Exceptions.SubtitlesFileException;
import beans.Film;
import beans.Subtitle;
import beans.SubtitleFile;

import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubFilesDaoSql implements SubFilesDao {
    private DaoFactory daoFactory;
    private Connection connection;

    public SubFilesDaoSql(DaoFactory daoFactory) throws SQLException {
        this.daoFactory = daoFactory;
        connection = daoFactory.getConnection();
    }

    private void closeConnection(ResultSet resultSet, Statement statement, Connection connection){
        if (resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (resultSet != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (resultSet != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Trace tout le process d'enregistrement d'un SubtitleFile en base de données
     * @param subtitleFile : fichier à enregistrer en bdd
     * @throws SubtitlesFileException
     */
    @Override
    public void UploadSubtitleFile(SubtitleFile subtitleFile) throws SubtitlesFileException {
        //On creer l'entrée en bdd s'il n'est pas déjà présent
        int id = CreateFilmInDb(subtitleFile.getName());
        //si le film n'était pas en base de données on stock l'id du film
        if (id != 0) {
            subtitleFile.setIdFilm(id);
        }
        //On entre ligne par ligne les sous-titres
        SetTextInDb(subtitleFile);
    }

    /**
     * Créer le film en base de données s'il n'existe pas déjà
     * @param name : Le nom du film
     * @return int : l'id d'enregistrement du film
     */
    private int CreateFilmInDb(String name){

        Boolean isInDB = false;

        //Est-ce que le film existe déjà en base de données ?
        if (isInBdd(name)) {
            isInDB = true;
        }

        try {
            //si le film n'est pas en bdd on le créer
            if (isInDB == false) {
                String req = "INSERT INTO `films`(`titre`) VALUES (?)";
                PreparedStatement statement = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, name);
                statement.executeUpdate();//retourne l'id du film que l'on a enregistré
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                return resultSet.getInt(1);
            } else {
                System.out.println("Film déjà présent en bdd");
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * Enregistre les textes de sous-titres d'un film
     * @param subtitleFile : Fichier de sous-titres
     */
    private void SetTextInDb(SubtitleFile subtitleFile){
        String req = "INSERT INTO `sous_titres`(`id_film`, `numero_sous_titre`, `texte`, `texte_traduit`, `start_time`, `end_time`) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = null;

        try {
            //boucle de parcours de tous les sous-titres du fichier de sous-titres
            for (Subtitle subtitle : subtitleFile.getSubtitles()) {

                int idFilm = subtitleFile.getIdFilm();
                int number = subtitle.getNumber();
                String text = subtitle.getText();
                String translatedText = subtitle.getTranslatedText();
                String startTime = subtitle.getStartTime();
                String endTime = subtitle.getEndTime();
                preparedStatement = connection.prepareStatement(req);

                preparedStatement.setInt(1, idFilm);
                preparedStatement.setInt(2, number);
                preparedStatement.setString(3, text);
                preparedStatement.setString(4, translatedText);
                preparedStatement.setString(5, startTime);
                preparedStatement.setString(6, endTime);

                preparedStatement.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SaveSubFilesInDB : fail");
        }
    }

    /**
     * Restitue le fichier de sous-titre d'un film
     * @param idFilm : id fu fichier à restituer
     * @return
     */
    @Override
    public SubtitleFile getSubtitleFile(int idFilm) throws SQLException {
        SubtitleFile subtitleFile = new SubtitleFile();
        String name = "";
        ArrayList<Subtitle> subtitles = new ArrayList<Subtitle>();
        Subtitle subtitle;

        String req = "SELECT * FROM `films` JOIN `sous_titres` " +
                "ON films.id = sous_titres.id_film " +
                "WHERE films.id = ? " +
                "ORDER BY sous_titres.numero_sous_titre";

        PreparedStatement statement = connection.prepareStatement(req);
        statement.setInt(1, idFilm);
        ResultSet resultSet = statement.executeQuery();

        //Enregistrement de toutes les sous-titres dans un tableau
        while (resultSet.next()) {
            subtitle = new Subtitle();
            subtitle.setNumber(resultSet.getInt("numero_sous_titre"));
            subtitle.setText(resultSet.getString("texte"));
            subtitle.setTranslatedText(resultSet.getString("texte_traduit"));
            subtitle.setStartTime(resultSet.getString("start_time"));
            subtitle.setEndTime(resultSet.getString("end_time"));

            subtitles.add(subtitle);
            name = resultSet.getString("titre");
        }

        //Recomposition de subtitleFile
        subtitleFile.setName(name);
        subtitleFile.setIdFilm(idFilm);
        subtitleFile.setSubtitles(subtitles);

        return subtitleFile;
    }

    /**
     * Donne la liste des films présents en bdd
     * @return la liste des films
     */
    @Override
    public ArrayList<Film> getFilms() {
        String req = "SELECT * FROM films";
        ArrayList<Film> films = new ArrayList<Film>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(req);
            while (resultSet.next()){
                films.add(new Film(resultSet.getString("titre"), resultSet.getInt("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }

    @Override
    public boolean isInBdd(int idFilm) {
        ArrayList<Film> films = getFilms();
        Boolean exist = false;

        for (Film film :
                films) {
            if (film.getIdFilm() == idFilm) {
                exist = true;
            }
        }

        return exist;
    }

    @Override
    public boolean isInBdd(String nameFilm) {
        ArrayList<Film> films = getFilms();
        Boolean exist = false;

        for (Film film :
                films) {
            if (film.getName().equals(nameFilm)) {
                exist = true;
            }
        }

        return exist;
    }


    /**
     * Mise à jour des sous-titres traduits en base de données
     * @param idFilm : Le film concerné
     * @param subtitles : les sous-titres traduits
     */
    @Override
    public void UpdateTranslatedSubtitles(int idFilm, ArrayList<Subtitle> subtitles) {
        String req = "UPDATE `sous_titres` SET `texte_traduit` = ? WHERE `id_film` = ? AND `texte` = ? AND `numero_sous_titre`= ?";

        try {
//            Désactive l'auto commit pour éviter d'envoyer qu'une partie des requêtes en cas de problèmes
            connection.setAutoCommit(false);
            for (Subtitle subtitle : subtitles) {
                PreparedStatement statement = connection.prepareStatement(req);
                statement.setString(1, subtitle.getTranslatedText());
                statement.setInt(2, idFilm);
                statement.setString(3, subtitle.getText());
                statement.setInt(4, subtitle.getNumber());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.commit();
                connection.setAutoCommit(true);//On réactive l'auto commit pour la suite
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String ClearSpecialChar(String str) {
        Pattern pattern = Pattern.compile("\\W");//regex pour selectionner tous les caractères spéciaux
        Matcher matcher = pattern.matcher(str);//on  place la chaine à traiter
        matcher.replaceAll("_"); //on définit le caractère de remplacement
        return str;
    }

}
