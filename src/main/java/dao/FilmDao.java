package dao;

import java.io.InputStream;
import java.util.List;

import entity.Film;
import exception.*;

public interface FilmDao {

	public List<Film> listFilms();
	public List<Film> listFilms(String colonne);
	public Film getFilm(Integer id) throws FilmNotFoundException;
	public Film addFilm(Film film, InputStream in) throws FilmAlreadyExistingException, FilmNotFoundException;
	public Film deleteFilm(Integer id) throws FilmNotFoundException;
	public Film activeFilm(Integer id) throws FilmNotFoundException, FilmAlreadyActiveException;
	public Film desactiveFilm(Integer id) throws FilmNotFoundException, FilmAlreadyDesactiveException;
	public List<Film> getFilmByUtilisateur(Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;
	public int getSqlIdFilm(Film film) throws FilmNotFoundException;
	
}
