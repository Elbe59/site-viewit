package service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dao.FilmDao;
import dao.GenreDao;
import dao.impl.FilmDaoImpl;
import dao.impl.GenreDaoImpl;
import entity.Film;
import entity.Genre;
import exception.*;

public class FilmService {

	private FilmDao filmDao = new FilmDaoImpl();
	private GenreDao genreDao = new GenreDaoImpl();
	private StockageImageService stockageService = new StockageImageService();

	private static class FilmHolder {
		private final static FilmService instance = new FilmService();
	}
	
	public static FilmService getInstance() {
		return FilmHolder.instance;
	}
	
	/*private FilmService() {
		
	}*/
	
	public List<Film> listFilms() {
		return filmDao.listFilms();
	}

	public List<Film> listFilms(String colonne) {
		return filmDao.listFilms(colonne);
	}

	public Film addFilm(String titre,String resume,String dateSortieStr,int duree,String realisateur,String acteur,String imageName,String urlBA,Genre genre1, InputStream in) throws FilmAlreadyExistingException, FilmNotFoundException, IOException {
		Film res = null;
		LocalDate dateSortie = formaterDate(dateSortieStr);
		Film film=new Film(titre,resume,dateSortie,duree,realisateur,acteur,imageName,urlBA,genre1,0,"");
		try{
			res = filmDao.addFilm(film,in);
		}catch (FilmAlreadyExistingException e)
		{
			//e.printStackTrace();
		}
		finally {
			return res;
		}
	}

	public Film activeFilm(Integer id) throws FilmNotFoundException, FilmAlreadyActiveException
	{
		try{
			return filmDao.activeFilm(id);
		}catch (FilmAlreadyActiveException | FilmNotFoundException e)
		{
			return null;
		}
	}

	public Film desactiveFilm(Integer id) throws FilmNotFoundException, FilmAlreadyDesactiveException
	{
		try{
			return filmDao.desactiveFilm(id);
		}catch (FilmAlreadyDesactiveException | FilmNotFoundException e)
		{
			return null;
		}
	}
	
	public Film getFilm(int id) throws FilmNotFoundException {
		try{
			return filmDao.getFilm(id);
		}catch (FilmNotFoundException e){
			return null;
		}
	}
	
	/*public void addFilm(File img) {
		stockageService.addImage(img);
	}*/

	public Film deleteFilm(int id) throws FilmNotFoundException {
		try{
			return filmDao.deleteFilm(id);
		}catch (FilmNotFoundException e){
			return null;
		}
	}

	public List<Genre> listGenre()
	{
		return genreDao.listGenre();
	}

	public Genre deleteGenre(Integer id) throws GenreNotFoundException, SQLException
	{
		try{
			return genreDao.deleteGenre(id);
		}catch (GenreNotFoundException e){
			return null;
		}
	}

	public Genre getGenre(Integer id) throws GenreNotFoundException
	{
		try{
			return genreDao.getGenre(id);
		}catch (GenreNotFoundException e){
			return null;
		}
	}

	public List<Film> getFilmByUtilisateur(Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException
	{
		try{
			return filmDao.getFilmByUtilisateur(idUtilisateur);
		}catch (UserNotFoundException e){
			return null;
		}
	}

	public Genre addGenre(String name) throws GenreAlreadyExistingException, GenreNotFoundException {
		try{
			return genreDao.addGenre(name);
		}catch (GenreAlreadyExistingException e){
			return null;
		}
	}

	public LocalDate formaterDate(String date)
	{
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}
