package dao;

import java.util.List;

import entity.Film;
import entity.FilmDto;
import exception.*;

public interface FilmDao {

	public List<Film> listFilms();
	public List<Film> listFilms(String colonne);
	public Film getFilm(Integer id) throws FilmNotFoundException;

	public Film addFilm(Film film) throws FilmAlreadyExistingException;
	public Film deleteFilm(Integer id) throws FilmNotFoundException;
	public Film updateFilm(Film newFilm, Integer previousidFilm) throws FilmNotFoundException;

	public Film activeFilm(Integer id) throws FilmNotFoundException, FilmAlreadyActiveException;
	public Film desactiveFilm(Integer id) throws FilmNotFoundException, FilmAlreadyDesactiveException;

	public Film suppFavori (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;
	public Film addFavori (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;
	public Film addLike (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;
	public Film addDislike (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;
	public Film removeAvis (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;

	public List<FilmDto> listFilmsDto(Integer idUtilisateur);
	public List<Film> listFavorisFilm(Integer idUtilisateur, String trie) throws UserNotFoundException;
	public Integer getPourcentageFilm (Integer id) throws FilmNotFoundException;
	public List<FilmDto> trierListFilms (List<FilmDto> listFilmsDto);
}
