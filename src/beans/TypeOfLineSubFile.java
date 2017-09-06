package beans;

/**
 * Enumération des différentes informations que l'on trouve
 * dans un fichier de sous-titres
 */
public enum TypeOfLineSubFile {
    SubNumber(1), //correspond au numéro du sous-titre
    TimeSlot(2), //Correspond au temps de début et temps de fin
    SubText(3), //Correspond au texte du sous-titre
    Separator(4); //Correspond aux retours chariots ou séparateurs des sous-titres

    private int type;

    TypeOfLineSubFile(int i) {
        type = i;
    }

    public Integer getType(){
        return type;
    }
}
