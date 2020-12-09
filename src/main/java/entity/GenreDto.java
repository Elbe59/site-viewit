package entity;

public class GenreDto extends Genre{
	
    private int nbFilmLie;

    public GenreDto(Integer id, String nom, int nbFilmLie) {
        super(id,nom);
        this.nbFilmLie=nbFilmLie;
    }

    public int getNbFilmLie() {
        return nbFilmLie;
    }

    public void setNbFilmLie(int nbFilmLie) {
        this.nbFilmLie = nbFilmLie;
    }
}