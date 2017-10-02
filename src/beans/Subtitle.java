package beans;

/**
 * Correspond à un sous-titre unique d'un fichier srt
 */
public class Subtitle {
    private int number;
    private String startTime;
    private String endTime;
    private String text;
    private String translatedText;

    public Subtitle(){ text = null;
    translatedText = null;}

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (this.text != null && this.text != text) { //si le sous-titre a déjà une première ligne alors on ajoute une deuxième ligne
            this.text += "\n" + text;
        } else {
            this.text = text;
        }
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    @Override
    public Subtitle clone() throws CloneNotSupportedException {
        Subtitle subtitle = new Subtitle();

        subtitle.setNumber(this.getNumber());
        subtitle.setText(this.getText());
        subtitle.setTranslatedText(this.getTranslatedText());
        subtitle.setStartTime(this.getStartTime());
        subtitle.setEndTime(this.getEndTime());

        return subtitle;
    }
}
