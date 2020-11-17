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

	public void deleteFilm(int id){filmDao.deleteFilm(id);}
	
}
