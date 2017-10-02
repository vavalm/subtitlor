package beans;

import java.util.ArrayList;

public class SubtitleFile {
    private int idFilm;
    private String name;
    private ArrayList<Subtitle> subtitles;

    public SubtitleFile(){

    }

    public SubtitleFile(String fileName) {
        setName(fileName);
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Subtitle> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(ArrayList<Subtitle> subtitles) {
        this.subtitles = subtitles;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getStartTimes(){
        ArrayList<String> startTimes = new ArrayList<String>();

        for (Subtitle sub :
                subtitles) {
            startTimes.add(sub.getStartTime());
        }

        return startTimes;
    }

    public ArrayList<String> getEndTimes(){
        ArrayList<String> endTimes = new ArrayList<String>();

        for (Subtitle sub :
                subtitles) {
            endTimes.add(sub.getEndTime());
        }

        return endTimes;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

}
