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
			throw new FilmNotFoundException("Film non trouvé.");
		}
	}

	//Ajout d'un film
	public Film addFilm(String titre,String resume,String dateSortieStr,int duree,String realisateur,String acteur,String imageName,String urlBA,Genre genre1) throws FilmAlreadyExistingException, FilmNotFoundException, IOException {
		//Film res = null;
		LocalDate dateSortie = formaterDate(dateSortieStr);
		Film film=new Film(1,titre,resume,dateSortie,duree,realisateur,acteur,imageName,urlBA,genre1,0);
		try{
			return filmDao.addFilm(film);
		}catch (FilmAlreadyExistingException e)
		{
			throw new FilmAlreadyExistingException("Le film " + titre + " existe déjà.");
		}
		//return res;
	}
	
	public Film deleteFilm(int id) throws FilmNotFoundException {
		try{
			return filmDao.deleteFilm(id);
		}catch (FilmNotFoundException e){
			throw new FilmNotFoundException("Le film que vous essayez de supprimer n'existe pas.");
		}
	}

	//Activation - Rendre visible le film sur la page d'accueil
	public Film activeFilm(Integer id) throws FilmNotFoundException, FilmAlreadyActiveException
	{
		try {
			return filmDao.activeFilm(id);
		}
		catch(FilmNotFoundException e){
			throw new FilmNotFoundException("Film non trouvé lors de la tentative d'activation");
		}
		catch(FilmAlreadyActiveException e) {
			throw new FilmAlreadyActiveException("Film déjà activé.");
		}
	}

	//Désactivation - Rendre invisible le film sur la page d'accueil
	public Film desactiveFilm(Integer id) throws FilmNotFoundException, FilmAlreadyDesactiveException
	{
		try{
			return filmDao.desactiveFilm(id);
		}
		catch (FilmAlreadyDesactiveException e){
			throw new FilmAlreadyDesactiveException("Film déjà désactivé.");
		}
		catch(FilmNotFoundException e) {
			throw new FilmNotFoundException("Film non trouvé lors de la tentative de désactivation");
		}
	}
	
	
	public Film addFavori(int idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException {
		try{
			return filmDao.addFavori(idFilm,idUtilisateur);
		}
		catch (FilmNotFoundException e){
			throw new FilmNotFoundException("Film non trouvé lors de la tentative d'ajout aux favoris");
		}
		catch(UserNotFoundException e){
			throw new UserNotFoundException("Impossible de trouvé l'utilisateur pour l'ajout d'un favori");
		}
	}

	public Film suppFavori(int idFilm, Integer idUtilisateur) throws FilmNotFoundException, SQLException, UserNotFoundException {
		try{
			return filmDao.suppFavori(idFilm,idUtilisateur);
		}
		catch (FilmNotFoundException e){
			throw new FilmNotFoundException("Film non trouvé lors de la tentative pour retirer le film aux favoris");
		}
		catch(UserNotFoundException e){
			throw new UserNotFoundException("Impossible de trouvé l'utilisateur pour retirer le film aux favoris");
		}
	}
	
	//Mettre un like
	public Film addLike(int idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException
	{
		try{
			return filmDao.addLike(idFilm,idUtilisateur);
		}
		catch (FilmNotFoundException e){
			throw new FilmNotFoundException("Film non trouvé lors de la tentative d'ajout au favori");
		}
		catch(UserNotFoundException e){
			throw new UserNotFoundException("Impossible de trouvé l'utilisateur pour retirer le film des favoris");
		}
	}

	//Mettre un dislike
	public Film addDislike(int idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException {
		try{
			return filmDao.addDislike(idFilm,idUtilisateur);
		}
		catch (FilmNotFoundException e){
			throw new FilmNotFoundException("Film non trouvé lors de la tentative d'un dislike");
		}
		catch(UserNotFoundException e){
			throw new UserNotFoundException("Impossible de trouvé l'utilisateur lors de la tentative d'un dislike");
		}
	}
	
	//Retirer son like ou dislike
	public Film removeAvis(int idFilm, Integer idUtilisateur) throws FilmNotFoundException, SQLException, UserNotFoundException {
		try{
			return filmDao.removeAvis(idFilm,idUtilisateur);
		}
		catch (FilmNotFoundException e){
			throw new FilmNotFoundException("Film non trouvé lors de la tentative d'un retrait d'un like ou dislike");
		}
		catch(UserNotFoundException e){
			throw new UserNotFoundException("Impossible de trouvé l'utilisateur lors de la tentative d'un retrait d'un like ou dislike");
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
		}
		catch(UserNotFoundException e){
			throw new UserNotFoundException("Impossible de trouvé l'utilisateur pour lister ses favoris");
		}
	}

	/*--------- METHODE GENRE ------------*/
	public Genre addGenre(String name) throws GenreAlreadyExistingException, GenreNotFoundException {
		try{
			return genreDao.addGenre(name);
		}catch (GenreAlreadyExistingException e){
			throw new GenreAlreadyExistingException("Echec de l'ajout d'un Genre : Genre déjà existant.");
		}
	}
	
	public Genre deleteGenre(Integer id,int nbFilmLie) throws GenreNotFoundException, SQLException, GenreLinkToFilmException {
		if (nbFilmLie > 0) {
			throw new GenreLinkToFilmException("Le genre que vous essayez de supprimer est lié à " + nbFilmLie + " film(s).");
		} else {
			try {
				return genreDao.deleteGenre(id);
			} catch (GenreNotFoundException e) {
				throw new GenreNotFoundException("Echec de suppresion d'un Genre : Genre non trouvé.");
			}
		}
	}

	public Genre getGenre(Integer id) throws GenreNotFoundException {
		try{
			return genreDao.getGenre(id);
		}catch (GenreNotFoundException e){
			throw new GenreNotFoundException("Le genre que vous essayez de récupérer n'a pas été trouvé.");
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
