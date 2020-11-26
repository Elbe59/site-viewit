package service;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.FilmDao;
import dao.GenreDao;
import dao.impl.FilmDaoImpl;
import dao.impl.GenreDaoImpl;
import entity.Film;
import entity.Genre;
import entity.GenreDto;
import exception.*;

public class FilmService {

	private FilmDao filmDao = new FilmDaoImpl();
	private GenreDao genreDao = new GenreDaoImpl();
	private StockageImageService stockageService = new StockageImageService();

	public Film addFilm(Film film, InputStream in) throws FilmAlreadyExistingException, FilmNotFoundException {
		return filmDao.addFilm(film,in);
	}

	private static class FilmHolder {
		private final static FilmService instance = new FilmService();
	}
	
	public static FilmService getInstance() {
		return FilmHolder.instance;
	}
	
	private FilmService() {
		
	}
	
	public List<Film> listFilms() {
		return filmDao.listFilms();
	}
	public List<Film> listFilms(String colonne) {
		return filmDao.listFilms(colonne);
	}

	public Film activeFilm(Integer id) throws FilmNotFoundException, FilmAlreadyActiveException {return filmDao.activeFilm(id);}

	public Film desactiveFilm(Integer id) throws FilmNotFoundException, FilmAlreadyDesactiveException {return filmDao.desactiveFilm(id);}
	
	public Film getFilm(int id) throws FilmNotFoundException {
		return filmDao.getFilm(id);
	}
	
	public void addFilm(File img) {
		stockageService.addImage(img);
	}

	public void deleteFilm(int id) throws FilmNotFoundException {
		filmDao.deleteFilm(id);
	}

	public List<Genre> listGenre(){
		return genreDao.listGenre();
	}
	public List<GenreDto> listGenreDto(){
		List<Genre> list=FilmService.getInstance().listGenre();
		return genreDao.listGenreDto(list);
	}

	public Genre deleteGenre(Integer id,int nbFilmLie) throws GenreNotFoundException, SQLException, GenreLinkToFilmException {
		if(nbFilmLie>0){
			throw new GenreLinkToFilmException();
		}
		else{
			return genreDao.deleteGenre(id);
		}

	}
	public Genre getGenre(Integer id) throws GenreNotFoundException {return genreDao.getGenre(id);}
	public List<Film> getFilmByUtilisateur(Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException {
		return filmDao.getFilmByUtilisateur(idUtilisateur);
	};

	public void addGenre(String name) throws GenreAlreadyExistingException {genreDao.addGenre(name);}
}
