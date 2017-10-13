package utilities;

import Exceptions.SubtitlesFileException;
import beans.Subtitle;
import beans.SubtitleFile;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubtitlesHandler {
    private ArrayList<String> originalSubtitles;

    /**
     * Prend le fichier texte et met chaque ligne sous-forme de tableau
     * @throws SubtitlesFileException
     */

    public SubtitlesHandler() throws SubtitlesFileException {
        originalSubtitles = new ArrayList<String>();
    }

    /**
     * Créer l'objet SubtitleFile à partir du fichier part uploadé par l'utilisateur
     * @param part
     * @return
     */
    public SubtitleFile PartToSubFile(Part part, String name){
        ArrayList<String> text = GenerateSubArray(part);
        SubtitleFile subtitleFile = ArraytoSubFile(text);
        subtitleFile.setName(name);

        return subtitleFile;
    }

    /**
     * Génère un Array<String> contenant chaque ligne du du fichier
     * @param part : Fichier dont on doit extraire chaque ligne
     * @return
     */
    private ArrayList<String> GenerateSubArray(Part part){
        BufferedReader br;
        ArrayList<String> subs = new ArrayList<String>();

        try {
            br = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
            String line;
            while ((line=br.readLine()) != null) {
                subs.add(new String(line.getBytes("iso-8859-1"), "UTF-8"));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subs;
    }

    /**
     * Creer l'objet SubtitleFile à partir du contenu brut du fichier.
     * La fonction va générer un par un le tableau de sous-titres et décomposer chaque donnée.
     * @param rawContent : Tableau de String contenant chaque ligne du fichier
     * @return
     */
    private SubtitleFile ArraytoSubFile(ArrayList<String> rawContent){
        SubtitleFile subtitleFile = new SubtitleFile();
        ArrayList<Subtitle> subtitlesResult = new ArrayList<Subtitle>();
        Subtitle subtitle = null;

        Pattern pattern1 = Pattern.compile("^([0-9]){1,9}$", Pattern.MULTILINE);
        Pattern pattern2 = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);
        Matcher matcher1;

        for (int i = 0; i < rawContent.size(); i++) {
            String line = rawContent.get(i);
            matcher1 = pattern1.matcher(line);
            //numéro de sous-titre => nouveau sous-titre + on stock le numéro de sous-titre
            if (matcher1.find() == true) {
                subtitle = new Subtitle();
                subtitle.setNumber(Integer.parseInt(line));
            } else if (i == rawContent.size()) { //dernière ligne du fichier
                if (line.equals("\n") || line.equals("\r") || line.equals("")) { //dernière ligne qui est vide on enregistre
                    subtitlesResult.add(subtitle);
                } else { //dernière ligne est un texte de sous-titre
                    subtitle.setText(line);
                }
            } else if (line.contains(" --> ")) { //une ligne de temps => on stock le temps de début et de fin
                String[] times = getTimes(line);
                subtitle.setStartTime(times[0]);
                subtitle.setEndTime(times[1]);
            } else if (pattern2.matcher(line).find() == true) { //Du texte (présence de lettres) => sauvegarde
                subtitle.setText(line);
            } else { //Si c'est la séparation entre deux blocs
                subtitlesResult.add(subtitle);
            }
        }

        subtitleFile.setSubtitles(subtitlesResult);
        return subtitleFile;
    }

    /**
     * Si line contient un créneau pour un sous-titre, alors on renvoie le
     *
     * @param line : ligne à analyser pour avoir le temps de début et le temps de fin
     * @return Un tableau à 2 cases. La première case String[0] correspond au temps de début.
     * La deuxième (String[1]) correspond au temps de fin.
     */
    public String[] getTimes(String line) {
        String[] times;
        times = line.split(" --> ");
        return times;
    }

    /**
     * Reconstitue la ligne correspondant au temps du sous-titre
     * qui est de la forme
     * @param start : temps de début
     * @param end : temps de fin
     * @return
     */
    public String getSlotTimeLine (String start, String end){
        return(start + " --> " + end);
    }

}
