package dao;

import Exceptions.SubtitlesFileException;
import beans.Subtitle;
import beans.SubtitlesFile;

import java.util.ArrayList;
import java.util.List;

public interface SubFilesDao {

    /**
     * Enrehistre en BDD le fichier uploadé
     * @throws SubtitlesFileException
     */
    public void saveUploadSubFile(SubtitlesFile subtitlesFile) throws SubtitlesFileException;

    /**
     * Restitue le fichier de sous-titres depuis la base de données
     * @param fileName : nom du fichier à restituer
     * @return
     */
    public SubtitlesFile getSubtitlesFile(String fileName);

    /**
     * Permet d'obtenir toutes les tables contenues dans une base de données
     * Et donc tous les fichiers de sous-titres
     * @param dataBaseName
     * @return
     */
    public ArrayList<String> getFilesInDB(String dataBaseName);

    public void UpdateTranslatedSubtitles(String tableName, ArrayList<Subtitle> subtitles);
}
