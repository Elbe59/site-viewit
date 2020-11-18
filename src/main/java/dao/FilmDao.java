package dao;

import java.util.List;

import entity.Film;

public interface FilmDao {

	public List<Film> listFilms();
	public List<Film> listFilms(String colonne);
	public Film getFilm(Integer id);
	public Film addFilm(Film film);
	public Film deleteFilm(Integer id);
	public Film activeFilm(Integer id);
	public Film desactiveFilm(Integer id);
	
}
