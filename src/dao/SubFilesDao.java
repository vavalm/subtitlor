package dao;

import Exceptions.SubtitlesFileException;
import beans.Film;
import beans.Subtitle;
import beans.SubtitleFile;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SubFilesDao {

    /**
     * Enrehistre en BDD le fichier de sous-titre uploadé
     * @throws SubtitlesFileException
     */
    public void UploadSubtitleFile(SubtitleFile subtitleFile) throws SubtitlesFileException;

    /**
     * Restitue le fichier de sous-titres depuis la base de données
     * @param idFilm : id fu fichier à restituer
     * @return
     */
    public SubtitleFile getSubtitleFile(int idFilm) throws SQLException;

    /**
     * Permet d'obtenir tous les noms de film disponibles en bdd
     * @param
     * @return
     */
    public ArrayList<Film> getFilms();

    /**
     * Permet de mettre à jour les traductions de sous-titres en base de données
     * @param idFilm
     * @param subtitles
     */
    public void UpdateTranslatedSubtitles(int idFilm, ArrayList<Subtitle> subtitles);
}
