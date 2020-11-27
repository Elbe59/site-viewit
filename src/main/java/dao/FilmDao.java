package dao;

import java.io.InputStream;
import java.util.List;

import entity.Film;
import entity.FilmDto;
import exception.*;

public interface FilmDao {

	public List<Film> listFilms();
	public List<Film> listFilms(String colonne);
	public Film getFilm(Integer id) throws FilmNotFoundException;
	public Film addFilm(Film film, InputStream in) throws FilmAlreadyExistingException, FilmNotFoundException;
	public Film deleteFilm(Integer id) throws FilmNotFoundException;
	public Film activeFilm(Integer id) throws FilmNotFoundException, FilmAlreadyActiveException;
	public Film desactiveFilm(Integer id) throws FilmNotFoundException, FilmAlreadyDesactiveException;
	public int getSqlIdFilm(Film film) throws FilmNotFoundException;
	public Film suppFavori (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;
	public Film addFavori (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;
	public List<FilmDto> listFilmsDto(Integer idUtilisateur);
	public List<Film> listFavorisFilm(Integer idUtilisateur) throws UserNotFoundException;
}
