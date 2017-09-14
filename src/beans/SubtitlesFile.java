package beans;

import java.util.ArrayList;

public class SubtitlesFile {
    private String name;
    private ArrayList<Subtitle> subtitles;

    public SubtitlesFile(String fileName) {
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
}
