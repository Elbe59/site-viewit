package dao;

import java.util.List;

import entity.Film;

public interface FilmDao {

	public List<Film> listFilms();
	public Film getFilm(Integer id);
	public Film addFilm(Film film);
	
}
