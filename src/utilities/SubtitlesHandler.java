package utilities;

import Exceptions.SubtitlesFileException;
import beans.Subtitle;
import beans.SubtitlesFile;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SubtitlesHandler {
    private ArrayList<String> originalSubtitles = null;

    /**
     * Prend le fichier texte et met chaque ligne sous-forme de tableau
     * @throws SubtitlesFileException
     */

    public SubtitlesHandler() throws SubtitlesFileException {
        originalSubtitles = new ArrayList<String>();
    }

    /**
     * Génère un Array de chaque ligne du contenu du fichier
     * @param part : Fichier dont on doit extraire chaque ligne
     * @return
     */
    private ArrayList<String> GenerateSubArray(Part part){
        BufferedReader br;
        char[] buf = new char[1024];
        ArrayList<String> subs = new ArrayList<String>();

        try {
            br = new BufferedReader(new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line=br.readLine()) != null) {
                subs.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subs;
    }

    /**
     * Creer l'objet SubtitlesFile à partir du contenu brut du fichier.
     * La fonction va générer un par un le tableau de sous-titres et décomposer chaque donnée.
     * @param name : Nom du fichier
     * @param rawContent : Tableau de String contenant chaque ligne du fichier
     * @return
     */
    public SubtitlesFile toSubFile(String name, ArrayList<String> rawContent){
        SubtitlesFile subtitlesFile = new SubtitlesFile(name);
        ArrayList<Subtitle> subtitlesResult = new ArrayList<Subtitle>();

        for (int i = 0; i < rawContent.size()-3; i=i) {
            Subtitle subtitle = new Subtitle();
            subtitle.setNumber(Integer.parseInt(rawContent.get(i)));
            String[] times = getTimes(rawContent.get(i+1));//on décompose la ligne de temps
            subtitle.setStartTime(times[0]);//première ligne = numéro de sous-titre
            subtitle.setEndTime(times[1]);//2e ligne = les temps de début et de fin
            subtitle.setText(rawContent.get(i+2));//3e ligne = texte
            subtitlesResult.add(subtitle);//on l'ajoute à notre liste de sous-titres

            if (!rawContent.get(i+3).isEmpty() && !rawContent.get(i+3).equals("")){//deux lignes de texte
                Subtitle subtitleSecondLine = null;
                try {
                    subtitleSecondLine = subtitle.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    System.out.println("Echec du clonage du sous-titre");
                }
                subtitleSecondLine.setText(rawContent.get(i+3));//4e ligne = 2e ligne de texte
                subtitlesResult.add(subtitleSecondLine);
                i = i + 5;
            }
            else if (!rawContent.get(i+4).isEmpty()){ //une seule ligne de texte
                i = i + 4;
            }
        }

        subtitlesFile.setSubtitles(subtitlesResult);
        return subtitlesFile;
    }

    public ArrayList<String> getSubtitles() {
        return originalSubtitles;
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
        System.out.println(times[0]);
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
