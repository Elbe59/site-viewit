package service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dao.FilmDao;
import dao.GenreDao;
import dao.impl.FilmDaoImpl;
import dao.impl.GenreDaoImpl;
import entity.Film;
import entity.FilmDto;
import entity.Genre;
import entity.GenreDto;
import exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FilmService {
	private final Logger LOG = LogManager.getLogger();

	private FilmDao filmDao = new FilmDaoImpl();
	private GenreDao genreDao = new GenreDaoImpl();

	private static class FilmHolder {
		private final static FilmService instance = new FilmService();
	}
	
	public static FilmService getInstance() {
		return FilmHolder.instance;
	}
	
	/*private FilmService() {
		
	}*/
	
	/*--------- METHODE FILM ------------*/
	
	//ListFilm sans paramètre
	public List<Film> listFilms() {
		return filmDao.listFilms();
	}
	
	//ListFilm avec paramètre
	public List<Film> listFilms(String colonne) {
		String param = "";
		if(colonne == null)
			param = "titreFilm";
		else if(colonne.equals("alpha"))
			param = "titreFilm";
		else if (colonne.equals("recent"))
			param = "dateSortie DESC";
		else if (colonne.equals("ancien"))
			param = "dateSortie";
		else if (colonne.equals("genre"))
			param = "nomGenre";
		else if (colonne.contentEquals("valide"))
			param = "valide";
		else {
			param = "valide";//popularité
		}
		LOG.info("Liste les films");
		return filmDao.listFilms(param);
	}
	
	public Film getFilm(int id) throws FilmNotFoundException {
		try{
			return filmDao.getFilm(id);
		}catch (FilmNotFoundException e){
			return null;
		}
	}

	//Ajout d'un film
	public Film addFilm(String titre,String resume,String dateSortieStr,int duree,String realisateur,String acteur,String imageName,String urlBA,Genre genre1) throws FilmAlreadyExistingException, FilmNotFoundException, IOException {
		Film res = null;
		LocalDate dateSortie = formaterDate(dateSortieStr);
		Film film=new Film(1,titre,resume,dateSortie,duree,realisateur,acteur,imageName,urlBA,genre1,0);
		try{
			res = filmDao.addFilm(film);
		}catch (FilmAlreadyExistingException e)
		{
			e.printStackTrace();
		}
		return res;
	}
	
	public Film deleteFilm(int id) throws FilmNotFoundException {
		try{
			return filmDao.deleteFilm(id);
		}catch (FilmNotFoundException e){
			return null;
		}
	}

	//Activation - Rendre visible le film sur la page d'accueil
	public Film activeFilm(Integer id) throws FilmNotFoundException, FilmAlreadyActiveException
	{
		try{
			return filmDao.activeFilm(id);
		}catch (FilmAlreadyActiveException | FilmNotFoundException e)
		{
			return null;
		}
	}

	//Désactivation - Rendre invisible le film sur la page d'accueil
	public Film desactiveFilm(Integer id) throws FilmNotFoundException, FilmAlreadyDesactiveException
	{
		try{
			return filmDao.desactiveFilm(id);
		}catch (FilmAlreadyDesactiveException | FilmNotFoundException e)
		{
			return null;
		}
	}
	
	
	public Film addFavori(int idFilm, Integer idUtilisateur) throws FilmNotFoundException
	{
		try{
			return filmDao.addFavori(idFilm,idUtilisateur);
		}catch (FilmNotFoundException | UserNotFoundException e)
		{
			//e.printStackTrace();
			return null;
		}
	}

	public Film suppFavori(int idFilm, Integer idUtilisateur) throws FilmNotFoundException, SQLException
	{
		try{
			return filmDao.suppFavori(idFilm,idUtilisateur);
		}catch (FilmNotFoundException | UserNotFoundException e)
		{
			return null;
		}
	}
	
	//Mettre un like
	public Film addLike(int idFilm, Integer idUtilisateur) throws FilmNotFoundException
	{
		try{
			return filmDao.addLike(idFilm,idUtilisateur);
		}catch (FilmNotFoundException | UserNotFoundException e)
		{
			//e.printStackTrace();
			return null;
		}
	}

	//Mettre un dislike
	public Film addDislike(int idFilm, Integer idUtilisateur) throws FilmNotFoundException
	{
		try{
			return filmDao.addDislike(idFilm,idUtilisateur);
		}catch (FilmNotFoundException | UserNotFoundException e)
		{
			//e.printStackTrace();
			return null;
		}
	}
	
	//Retirer son like ou dislike
	public Film removeAvis(int idFilm, Integer idUtilisateur) throws FilmNotFoundException, SQLException
	{
		try{
			return filmDao.removeAvis(idFilm,idUtilisateur);
		}catch (FilmNotFoundException | UserNotFoundException e)
		{
			return null;
		}
	}
	
	public List<FilmDto> listFilmsDto(Integer idUtilisateur) {
		return filmDao.listFilmsDto(idUtilisateur);
	}

	public List<Film> listFavorisFilm(Integer idUtilisateur, String trie) throws FilmNotFoundException, UserNotFoundException
	{
		String param = "";
		if(trie == null)
			param = "titreFilm";
		else if(trie.equals("alpha"))
			param = "titreFilm";
		else if (trie.equals("recent"))
			param = "dateSortie DESC";
		else if (trie.equals("ancien"))
			param = "dateSortie";
		else {
			param = "titreFilm";
		}
		System.out.println("list : " + param);
		try{
			return filmDao.listFavorisFilm(idUtilisateur, param);
		}catch(UserNotFoundException e){
			return null;
		}
	}

	/*--------- METHODE GENRE ------------*/
	public Genre addGenre(String name) throws GenreAlreadyExistingException, GenreNotFoundException {
		try{
			return genreDao.addGenre(name);
		}catch (GenreAlreadyExistingException e){
			return null;
		}
	}
	
	public Genre deleteGenre(Integer id,int nbFilmLie) throws GenreNotFoundException, SQLException, GenreLinkToFilmException {
		if (nbFilmLie > 0) {
			throw new GenreLinkToFilmException();
		} else {
			try {
				return genreDao.deleteGenre(id);
			} catch (GenreNotFoundException e) {
				return null;
			}
		}
	}

	public Genre getGenre(Integer id) throws GenreNotFoundException {
		try{
			return genreDao.getGenre(id);
		}catch (GenreNotFoundException e){
			return null;
		}
	}
	
	public List<Genre> listGenre() {
		return genreDao.listGenre();
	}
	
	public List<GenreDto> listGenreDto(){
		List<Genre> list=FilmService.getInstance().listGenre();
		return genreDao.listGenreDto(list);
	}

	
	/*--------- AUTRE METHODE ------------*/
	
	public LocalDate formaterDate(String date)
	{
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	public String urlVerification(String url) throws UrlDoesNotMatch {
		if(url.contains("youtu.be/") || url.contains("youtube.com/")) {
			String urlCheck = "";
			String way = "";
			int charInWay = 0;
			System.out.println("Lien youtube OK");
			if(url.contains("youtu.be/")) {
				way = ".be/";
				charInWay = way.length();
			}
			if(url.contains("youtube.com/")) {
				way = "watch?v=";
				charInWay = way.length();
			}
			boolean parcour = true;
			int cpt = url.indexOf(way) + charInWay;
			while(parcour) {
				if(cpt < url.length() && (url.charAt(cpt) != '&' && url.charAt(cpt) != '=' && url.charAt(cpt) != '?')) {
					urlCheck += url.charAt(cpt);
					cpt++;
				}
				else {
					parcour = false;
				}
			}
			return urlCheck;
		}
		else {
			throw new UrlDoesNotMatch(url);
		}
	}
	
	public Integer getPourcentageFilm (Integer id) throws FilmNotFoundException {
		return filmDao.getPourcentageFilm(id);
	}
}
