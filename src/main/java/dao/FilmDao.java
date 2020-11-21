package dao;

import java.util.List;

import entity.Film;
import exception.FilmAlreadyActiveException;
import exception.FilmAlreadyDesactiveException;
import exception.FilmAlreadyExistingException;
import exception.FilmNotFoundException;

public interface FilmDao {

	public List<Film> listFilms();
	public List<Film> listFilms(String colonne);
	public Film getFilm(Integer id) throws FilmNotFoundException;
	public Film addFilm(Film film) throws FilmAlreadyExistingException;
	public Film deleteFilm(Integer id) throws FilmNotFoundException;
	public Film activeFilm(Integer id) throws FilmNotFoundException, FilmAlreadyActiveException;
	public Film desactiveFilm(Integer id) throws FilmNotFoundException, FilmAlreadyDesactiveException;
	public List<Film> getFilmByUtilisateur(Integer idUtilisateur) throws FilmNotFoundException;
	
}
