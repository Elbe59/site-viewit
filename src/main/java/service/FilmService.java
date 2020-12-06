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
	
	/*--------- METHODE FILM ------------*/
	
	//ListFilm sans paramètre
	public List<Film> listFilms() {
		return filmDao.listFilms();
	}
	
	//ListFilm avec paramètre
	public List<Film> listFilms(String trie) {
		String param = parametreTrie(trie);
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
	public Film addFilm(String titre,String resume,String dateSortieStr,int duree,String realisateur,String acteur,String imageName,String url,Genre genre1) throws FilmAlreadyExistingException, IOException {
		LocalDate dateSortie = formaterDate(dateSortieStr);
		String urlBA = "";
		try {
			urlBA = urlVerification(url);
		} catch (UrlDoesNotMatchException e) {
			return null;
		}
		Film film=new Film(1,titre,resume,dateSortie,duree,realisateur,acteur,imageName,urlBA,genre1,0);
		List<Film> films = listFilms();
		boolean existing  = false;
		
		for (int i = 0; i<films.size(); i++){
			if (films.get(i).getTitre().equals(film.getTitre())){
				if(films.get(i).getDateSortie().equals(film.getDateSortie()))
					existing = true;
			}
		}
		
		if (existing) {
			throw new FilmAlreadyExistingException("Le film que vous essayez d'ajouter existe déjà.");
		}
		else {
			return filmDao.addFilm(film);
		}
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
		try {
			return filmDao.activeFilm(id);
		}
		catch(FilmNotFoundException | FilmAlreadyActiveException e){
			return null;
		}
	}

	//Désactivation - Rendre invisible le film sur la page d'accueil
	public Film desactiveFilm(Integer id) throws FilmNotFoundException, FilmAlreadyDesactiveException
	{
		try{
			return filmDao.desactiveFilm(id);
		}
		catch (FilmAlreadyDesactiveException | FilmNotFoundException e){
			return null;
		}
	}
	
	
	public Film addFavori(int idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException {
		try{
			return filmDao.addFavori(idFilm,idUtilisateur);
		}
		catch (FilmNotFoundException | UserNotFoundException e){
			return null;
		}
	}

	public Film suppFavori(int idFilm, Integer idUtilisateur) throws FilmNotFoundException, SQLException, UserNotFoundException {
		try{
			return filmDao.suppFavori(idFilm,idUtilisateur);
		}
		catch (FilmNotFoundException | UserNotFoundException e){
			return null;
		}
	}
	
	//Mettre un like
	public Film addLike(int idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException
	{
		try{
			return filmDao.addLike(idFilm,idUtilisateur);
		}
		catch (FilmNotFoundException | UserNotFoundException e){
			return null;
		}
	}

	//Mettre un dislike
	public Film addDislike(int idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException {
		try{
			return filmDao.addDislike(idFilm,idUtilisateur);
		}
		catch (FilmNotFoundException | UserNotFoundException e){
			return null;
		}
	}
	
	//Retirer son like ou dislike
	public Film removeAvis(int idFilm, Integer idUtilisateur) throws FilmNotFoundException, SQLException, UserNotFoundException {
		try{
			return filmDao.removeAvis(idFilm,idUtilisateur);
		}
		catch (FilmNotFoundException | UserNotFoundException e){
			return null;
		}
	}
	
	public List<FilmDto> listFilmsDto(Integer idUtilisateur) {
		return filmDao.listFilmsDto(idUtilisateur);
	}

	public List<Film> listFavorisFilm(Integer idUtilisateur, String trie) throws UserNotFoundException
	{
		String param = parametreTrie(trie);
		
		System.out.println("list : " + param);
		try{
			return filmDao.listFavorisFilm(idUtilisateur, param);
		}
		catch(UserNotFoundException e){
			return null;
		}
	}

	/*--------- METHODE GENRE ------------*/
	public Genre addGenre(String name) throws GenreAlreadyExistingException {
        List<Genre> genres = listGenre();
        boolean existing = false;
        for (int i = 0; i < genres.size(); i++) {
            if (genres.get(i).getNom().toLowerCase().equals(name.toLowerCase())) {
                existing = true;
            }
        }
        if (existing) {
            throw new GenreAlreadyExistingException("Le genre que vous essayé d'ajouter existe déjà.");
        }
        else {
    		return genreDao.addGenre(name);
        }
	}
	
	public Genre deleteGenre(Integer id,int nbFilmLie) throws GenreNotFoundException, SQLException, GenreLinkToFilmException {
		if (nbFilmLie > 0) {
			throw new GenreLinkToFilmException("Le genre que vous essayez de supprimer est lié à " + nbFilmLie + " film(s).");
		} else {
			try {
				genreDao.getGenre(id);
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
	
	public String urlVerification(String url) throws UrlDoesNotMatchException {
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
			throw new UrlDoesNotMatchException(url);
		}
	}
	
	public Integer getPourcentageFilm (Integer id) throws FilmNotFoundException {
		return filmDao.getPourcentageFilm(id);
	}
	
	public String parametreTrie(String choix) {
		if(choix == null)
			return ("titreFilm");
		else if(choix.equals("alpha"))
			return ("titreFilm");
		else if (choix.equals("recent"))
			return ("dateSortie DESC");
		else if (choix.equals("ancien"))
			return ("dateSortie");
		else {
			return ("titreFilm");
		}
	}
}
