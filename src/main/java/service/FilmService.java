package service;

import java.util.List;

import dao.FilmDao;
import dao.impl.FilmDaoImpl;
import entity.Film;

public class FilmService {

	private static class FilmHolder {
		private final static FilmService instance = new FilmService();
	}
	
	public static FilmService getInstance() {
		return FilmHolder.instance;
	}
	
	private FilmDao filmDao = new FilmDaoImpl();
	
	private FilmService() {
		
	}
	
	public List<Film> listFilms() {
		return filmDao.listFilms();
	}
	
	public Film getFilm(int id) {
		return filmDao.getFilm(id);
	}
	
	public Film addFilm(Film film) {
		if(film == null) {
			throw new IllegalArgumentException("Erreur : Ajout d'un film null impossible.");
		}
		if(film.getTitre() == null) {
			throw new IllegalArgumentException("Erreur : Ajout d'un film null impossible.");
		}
		
		return film;
	}

	public void deleteFilm(int id) {
		filmDao.deleteFilm(id);
	}
	
}
