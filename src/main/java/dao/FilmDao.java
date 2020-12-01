package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import dao.impl.DataSourceProvider;
import entity.Film;
import entity.FilmDto;
import exception.*;

public interface FilmDao {

	public List<Film> listFilms();
	public List<Film> listFilms(String colonne);
	public Film updateFilm(Film film, String name) throws FilmNotFoundException;
	public Film getFilm(Integer id) throws FilmNotFoundException;
	public Film getFilmByTitle(String name) throws FilmNotFoundException;
	public Film addFilm(Film film, InputStream in) throws FilmAlreadyExistingException, FilmNotFoundException;
	public Film deleteFilm(Integer id) throws FilmNotFoundException;
	public Film activeFilm(Integer id) throws FilmNotFoundException, FilmAlreadyActiveException;
	public Film desactiveFilm(Integer id) throws FilmNotFoundException, FilmAlreadyDesactiveException;
	public int getSqlIdFilm(Film film) throws FilmNotFoundException;

	public Film suppFavori (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;
	public Film addFavori (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;
	public Film addLike (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;
	public Film addDislike (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;
	public Film removeAvis (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException;

	public List<FilmDto> listFilmsDto(Integer idUtilisateur);
	public List<Film> listFavorisFilm(Integer idUtilisateur) throws UserNotFoundException;
	public Integer getPourcentageFilm (Integer id) throws FilmNotFoundException;
}
