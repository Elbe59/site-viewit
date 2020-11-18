package service;

import java.util.List;

import dao.FilmDao;
import dao.GenreDao;
import dao.impl.FilmDaoImpl;
import dao.impl.GenreDaoImpl;
import entity.Film;
import entity.Genre;

public class FilmService {

	private static class FilmHolder {
		private final static FilmService instance = new FilmService();
	}
	
	public static FilmService getInstance() {
		return FilmHolder.instance;
	}
	
	private FilmDao filmDao = new FilmDaoImpl();
	private GenreDao genreDao = new GenreDaoImpl();
	
	private FilmService() {
		
	}
	
	public List<Film> listFilms() {
		return filmDao.listFilms();
	}
	public List<Film> listFilms(String colonne) {
		return filmDao.listFilms(colonne);
	}
	
	public Film getFilm(int id) {
		return filmDao.getFilm(id);
	}

	public void deleteFilm(int id){
		filmDao.deleteFilm(id);
	}

	public List<Genre> listGenre(){
		return genreDao.listGenre();
	}
	
}
