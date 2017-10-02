package beans;

public class Film {

    private String name;
    private int idFilm;

    public Film() {
    }

    public Film(String name, int idFilm) {
        this.name = name;
        this.idFilm = idFilm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    @Override
    public String toString() {
        return name;
    }
}
