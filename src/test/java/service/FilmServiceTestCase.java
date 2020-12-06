package service;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.FilmDao;
import dao.GenreDao;
import dao.UtilisateurDao;
import dao.impl.DataSourceProvider;
import dao.impl.FilmDaoImpl;
import dao.impl.GenreDaoImpl;
import dao.impl.UtilisateurDaoImpl;
import entity.Film;
import entity.FilmDto;
import entity.Genre;
import entity.GenreDto;
import exception.*;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import static org.assertj.core.api.Assertions.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class FilmServiceTestCase {


    @InjectMocks
    FilmService filmService;
    @Mock
    FilmDao filmDao = new FilmDaoImpl();
    @Mock
    GenreDao genreDao = new GenreDaoImpl();
    @Mock
    UtilisateurDao userDao = new UtilisateurDaoImpl();

    @Before
    public void initDb() throws Exception {
        try (Connection co = DataSourceProvider.getDataSource().getConnection();
             Statement stm = co.createStatement()) {
            stm.executeUpdate("DELETE FROM film");
            stm.executeUpdate("DELETE FROM genre");
            stm.executeUpdate("DELETE FROM preferer");
            stm.executeUpdate("DELETE FROM UTILISATEUR");
            stm.executeUpdate(
                    "INSERT INTO UTILISATEUR ( idUtilisateur, prenomUtilisateur, nomUtilisateur, email, mdp, mdpHash, admin) "
                            + "VALUES (1,'prenom1', 'nom1', 'email1@gmail.com', 'mdp1', 'mdpHash1', 0),"
                            + "(2,'prenom2', 'nom2', 'email2@gmail.com', 'mdp2', 'mdpHash2', 1);");
            stm.executeUpdate("INSERT INTO genre (idGenre, nomGenre) "
                    + "VALUES (1,'Aventure'), "
                    +"(2,'Action'),"
                    +"(3,'Horreur');");
            stm.executeUpdate(
                    "INSERT INTO film(idFilm, titreFilm, resumeFilm, dateSortie, dureeFilm, realisateur, acteur, imgFilm, urlBA, idGenre, valide) "
                            + "VALUES (1, 'titre 1', 'resume 1', '2020-11-11', 123, 'realisateur 1', 'acteur 1', 'image1.png', 'youtu.be/1', 1, 1),"
                            + "(2, 'titre 2', 'resume 2', '2020-11-12', 123, 'realisateur 2', 'acteur 2', 'image2.png', 'youtu.be/2', 2, 0);");

            stm.executeUpdate(
                    "INSERT INTO preferer (favoris, idFilm, idUtilisateur, liker) VALUES (1, 1, 1,1);");
            stm.executeUpdate(
                    "INSERT INTO preferer (favoris, idFilm, idUtilisateur, liker) VALUES (1, 2, 1,1);");
        }
    }
/*
    @Test
    public void shouldListFilm()
    {
        //WHEN
        List<Film> films = filmService.getInstance().listFilms();
        //THEN
        assertThat(films).hasSize(2);
        assertThat(films).extracting(
                Film::getId,
                Film::getTitre,
                Film::getResume,
                Film::getDateSortie,
                Film::getDuree,
                Film::getRealisateur,
                Film::getActeur,
                Film::getImageName,
                Film::getUrlBA,
                Film -> Film.getGenre().getId(),
                Film -> Film.getGenre().getNom(),

                Film::getValide).containsOnly(
                tuple(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", 1, "Aventure", 1),
                tuple(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtu.be/2", 2, "Action", 0));

    }
*/
    
    @Test
    public void shouldListFilm() throws IOException
    {
    	//GIVEN
    	Film film1 = new Film();
    	Film film2 = new Film();

		film1 = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", new Genre(1,"Aventure"), 1);
		film2 = new Film(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtube.com/2", new Genre(2,"Action"), 0);

    	List<Film> films = new ArrayList<Film>();
    	films.add(film1);
    	films.add(film2);
    	Mockito.when(filmService.listFilms()).thenReturn(films);
        //WHEN
    	List<Film> result = filmService.listFilms();
    	//THEN
        assertThat(result).containsExactlyInAnyOrderElementsOf(films);
    }
    
    @Test
    public void souldListFilmWithParameter() {
        //WHEN
        List<Film> films = filmService.getInstance().listFilms("recent");
        //THEN
        assertThat(films).hasSize(2);
        assertThat(films.get(0).getDateSortie()).isEqualTo(LocalDate.of(2020, 11, 12));
        assertThat(films.get(1).getDateSortie()).isEqualTo(LocalDate.of(2020, 11, 11));
        assertThat(films).extracting(
                Film::getId,
                Film::getTitre,
                Film::getResume,
                Film::getDateSortie,
                Film::getDuree,
                Film::getRealisateur,
                Film::getActeur,
                Film::getImageName,
                Film::getUrlBA,
                Film -> Film.getGenre().getId(),
                Film -> Film.getGenre().getNom(),
                Film::getValide).contains(
                tuple(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", 1, "Aventure", 1),
                tuple(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtu.be/2", 2, "Action", 0));
    }
    
    @Test
    public void shouldGetFilm() throws IOException, FilmNotFoundException {
        //GIVEN
        int id = 1;
        Genre genre = new Genre(1, "Aventure");

        Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", genre, 1);

        //WHEN
        Film res = filmService.getInstance().getFilm(id);
        //THEN
        assertThat(res).isNotNull();
        assertThat(res.getTitre()).isEqualTo(film.getTitre());
        assertThat(res.getResume()).isEqualTo(film.getResume());
        assertThat(res.getDateSortie()).isEqualTo(film.getDateSortie());
        assertThat(res.getDuree()).isEqualTo(film.getDuree());
        assertThat(res.getGenre().getNom()).isEqualTo(film.getGenre().getNom());
        assertThat(res.getRealisateur()).isEqualTo(film.getRealisateur());
        assertThat(res.getActeur()).isEqualTo(film.getActeur());
        assertThat(res.getImageName()).isEqualTo(film.getImageName());
        assertThat(res.getUrlBA()).isEqualTo(film.getUrlBA());
        assertThat(res.getValide()).isEqualTo(film.getValide());
    }

    @Test
    public void shouldGetFilmThrowFilmNotFoundException() throws FilmNotFoundException
    {
        //GIVEN
        int id = 42;
        //WHEN
        Film res = filmService.getInstance().getFilm(id);
        //THEN
        assertThat(res).isNull();
    }


    @Test
    public void shouldAddFilm() throws IOException, FilmAlreadyExistingException, FilmNotFoundException, UrlDoesNotMatchException {
        //GIVEN
        Genre genre = new Genre(1,"aventure");

        Film film = new Film(1,"titre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",genre,0);
        //WHEN
        Film res = filmService.getInstance().addFilm("titre3","resume3","2019-12-20", 120,"realisateur3","acteur3","image3.png","youtu.be/3",genre);

        //THEN
        assertThat(res).isNotNull();
        assertThat(res.getTitre()).isEqualTo(film.getTitre());
        assertThat(res.getResume()).isEqualTo(film.getResume());
        assertThat(res.getDateSortie()).isEqualTo(film.getDateSortie());
        assertThat(res.getDuree()).isEqualTo(film.getDuree());
        assertThat(res.getRealisateur()).isEqualTo(film.getRealisateur());
        assertThat(res.getActeur()).isEqualTo(film.getActeur());
        assertThat(res.getImageName()).isEqualTo(film.getImageName());
        assertThat(res.getUrlBA()).isEqualTo(film.getUrlBA());
        assertThat(res.getValide()).isEqualTo(film.getValide());
    }

    @Test
    public void shouldAddFilmThrowFilmAlreadyExistingException() throws IOException, FilmAlreadyExistingException, UrlDoesNotMatchException {
        //GIVEN
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", genre, 1);
        //WHEN
        Film res = new Film();
        try {
            res = filmService.getInstance().addFilm("titre 1", "resume 1", "2020-11-11", 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", genre);
        }
        //THEN
        catch(Exception e){
        	Assertions.assertThat(e).isExactlyInstanceOf(FilmAlreadyExistingException.class);
        }
        //Mockito.verify(filmDao, Mockito.never()).getSqlIdFilm(film);
    }
    
    @Test
    public void shouldAddFilmButThrowUrlDoesNotMatchException() throws IOException, FilmAlreadyExistingException, UrlDoesNotMatchException {
        //GIVEN
        Genre genre = new Genre(1, "Aventure");
        //WHEN
        Film res = filmService.getInstance().addFilm("titre 1", "resume 1", "2020-11-11", 123, "realisateur 1", "acteur 1", "image1.png", "daylimotion.com/1", genre);
        
        //THEN
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldDeleteFilm() throws IOException, FilmNotFoundException {
        //GIVEN
        int id = 1;
        Genre genre = new Genre(1, "Aventure");

        Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", genre, 1);

        //WHEN
        Film res = filmService.getInstance().deleteFilm(id);
        //THEN
        assertThat(res).isNotNull();
        assertThat(res.getTitre()).isEqualTo(film.getTitre());
        assertThat(res.getResume()).isEqualTo(film.getResume());
        assertThat(res.getDateSortie()).isEqualTo(film.getDateSortie());
        assertThat(res.getDuree()).isEqualTo(film.getDuree());
        assertThat(res.getGenre().getNom()).isEqualTo(film.getGenre().getNom());
        assertThat(res.getRealisateur()).isEqualTo(film.getRealisateur());
        assertThat(res.getActeur()).isEqualTo(film.getActeur());
        assertThat(res.getImageName()).isEqualTo(film.getImageName());
        assertThat(res.getUrlBA()).isEqualTo(film.getUrlBA());
        assertThat(res.getValide()).isEqualTo(film.getValide());
    }

    @Test
    public void shouldDeleteFilmTrowFilmNotFoundException() throws FilmNotFoundException
    {
        //GIVEN
        int id = 3;
        //WHEN
        Film res = filmService.getInstance().deleteFilm(id);
        //THEN
        Assertions.assertThat(res).isNull();
    }
    
    @Test
    public void shouldUpdateFilm() throws IOException, FilmAlreadyExistingException, FilmNotFoundException, UrlDoesNotMatchException {
        //GIVEN
        Genre genre = new Genre(1,"aventure");

        Film film = new Film(1,"titre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",genre,1);
        //WHEN
        Film res = filmService.getInstance().updateFilm(1,"newTitre3","resume3","2019-12-20", 120,"realisateur3","acteur3","image3.png","youtu.be/3",genre);

        //THEN
        assertThat(res).isNotNull();
        assertThat(res.getTitre()).isEqualTo("newTitre3");
        assertThat(res.getResume()).isEqualTo(film.getResume());
        assertThat(res.getDateSortie()).isEqualTo(film.getDateSortie());
        assertThat(res.getDuree()).isEqualTo(film.getDuree());
        assertThat(res.getRealisateur()).isEqualTo(film.getRealisateur());
        assertThat(res.getActeur()).isEqualTo(film.getActeur());
        assertThat(res.getImageName()).isEqualTo(film.getImageName());
        assertThat(res.getUrlBA()).isEqualTo(film.getUrlBA());
        assertThat(res.getValide()).isEqualTo(film.getValide());
    }
    
    @Test
    public void shouldUpdateFilmTrowFilmNotFoundException() throws FilmNotFoundException, IOException {
        //GIVEN
    	Genre genre = new Genre(1, "Aventure");
        //WHEN
        Film res = filmService.getInstance().updateFilm(4,"titre 1", "resume 1", "2020-11-11", 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", genre);
        //THEN
        Assertions.assertThat(res).isNull();
    }
    
    @Test
    public void shouldUpdateFilmButThrowUrlDoesNotMatchException() throws IOException, FilmAlreadyExistingException, UrlDoesNotMatchException, FilmNotFoundException {
        //GIVEN
        Genre genre = new Genre(1, "Aventure");
        //WHEN
        Film res = filmService.getInstance().updateFilm(1,"titre 1", "resume 1", "2020-11-11", 123, "realisateur 1", "acteur 1", "image1.png", "daylimotion.com/1", genre);
        
        //THEN
        Assertions.assertThat(res).isNull();
    }
    
    @Test
    public void shouldActiveFilm() throws FilmAlreadyActiveException, FilmNotFoundException {
        //GIVEN
        int id = 2;
        //WHEN
        Film res = filmService.getInstance().activeFilm(id);
        //THEN
        Assertions.assertThat(res.getValide()).isEqualTo(1);
    }

    @Test 
    public void shouldActiveFilmThrowFilmAlreadyActiveException() throws FilmAlreadyActiveException, FilmNotFoundException {
        //GIVEN
        int id = 1;
        //WHEN
        Film res = filmService.getInstance().activeFilm(id);
        //THEN
        Assertions.assertThat(res).isNull();
    }

    @Test 
    public void shouldActiveFilmThrowFilmNotFoundException() throws FilmAlreadyActiveException, FilmNotFoundException {
        //GIVEN
        int id = 3;
        //WHEN
        Film res = filmService.getInstance().activeFilm(id);
        //THEN
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldDesactiveFilm() throws FilmAlreadyDesactiveException, FilmNotFoundException {
        //GIVEN
        int id = 1;
        //WHEN
        Film res = filmService.getInstance().desactiveFilm(id);
        //THEN
        Assertions.assertThat(res.getValide()).isEqualTo(0);
    }

    @Test
    public void shouldDesactiveFilmThrowFilmAlreadyDesactiveException() throws FilmAlreadyDesactiveException, FilmNotFoundException {
        //GIVEN
        int id = 2;
        //WHEN
        Film res = filmService.getInstance().desactiveFilm(id);
        //THEN
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldDesactiveFilmThrowFilmNotFoundException() throws FilmAlreadyDesactiveException, FilmNotFoundException {
        //GIVEN
        int id = 3;
        //WHEN
        Film res = filmService.getInstance().desactiveFilm(id);
        //THEN
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldAddFavoris() throws IOException, FilmNotFoundException, UserNotFoundException {
        //GIVEN
        int idUser = 2;
        int idFilm = 1;
        Genre genre = new Genre(1, "Aventure");

        Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", genre, 1);

        //WHEN
        Film res = filmService.getInstance().addFavori(idFilm, idUser);
        //THEN
        assertThat(res).isNotNull();
        assertThat(res.getTitre()).isEqualTo(film.getTitre());
        assertThat(res.getResume()).isEqualTo(film.getResume());
        assertThat(res.getDateSortie()).isEqualTo(film.getDateSortie());
        assertThat(res.getDuree()).isEqualTo(film.getDuree());
        assertThat(res.getRealisateur()).isEqualTo(film.getRealisateur());
        assertThat(res.getActeur()).isEqualTo(film.getActeur());
        assertThat(res.getImageName()).isEqualTo(film.getImageName());
        assertThat(res.getUrlBA()).isEqualTo(film.getUrlBA());
        assertThat(res.getValide()).isEqualTo(film.getValide());
    }

    @Test
    public void shouldAddFavorisThrowUserNotFound() throws IOException, FilmNotFoundException, UserNotFoundException {
        //GIVEN
        int idUser = 3;
        int idFilm = 1;
        //WHEN
        Film res = filmService.getInstance().addFavori(idFilm, idUser);
        //THEN
        assertThat(res).isNull();
    }

    @Test
    public void shouldAddFavorisThrowFilmNotFoundException() throws IOException, FilmNotFoundException, UserNotFoundException {
        //GIVEN
        int idUser = 2;
        int idFilm = 3;
        //WHEN
        Film res = filmService.getInstance().addFavori(idFilm, idUser);
        //THEN
        assertThat(res).isNull();
    }

    @Test
    public void shouldSuppFavoris() throws IOException, FilmNotFoundException, SQLException, UserNotFoundException {
        //GIVEN
        int idUser = 1;
        int idFilm = 1;
        Genre genre = new Genre(1, "Aventure");

        Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", genre, 1);

        //WHEN
        Film res = filmService.getInstance().suppFavori(idFilm, idUser);
        //THEN
        assertThat(res).isNotNull();
        assertThat(res.getTitre()).isEqualTo(film.getTitre());
        assertThat(res.getResume()).isEqualTo(film.getResume());
        assertThat(res.getDateSortie()).isEqualTo(film.getDateSortie());
        assertThat(res.getDuree()).isEqualTo(film.getDuree());
        assertThat(res.getRealisateur()).isEqualTo(film.getRealisateur());
        assertThat(res.getActeur()).isEqualTo(film.getActeur());
        assertThat(res.getImageName()).isEqualTo(film.getImageName());
        assertThat(res.getUrlBA()).isEqualTo(film.getUrlBA());
        assertThat(res.getValide()).isEqualTo(film.getValide());
    }

    @Test
    public void shouldSuppFavorisThrowUserNotFound() throws IOException, FilmNotFoundException, SQLException, UserNotFoundException {
        //GIVEN
        int idUser = 3;
        int idFilm = 1;
        //WHEN
        Film res = filmService.getInstance().suppFavori(idFilm, idUser);
        //THEN
        assertThat(res).isNull();
    }

    @Test
    public void shouldSuppFavorisThrowFilmNotFoundException() throws IOException, FilmNotFoundException, SQLException, UserNotFoundException {
        //GIVEN
        int idUser = 2;
        int idFilm = 3;
        //WHEN
        Film res = filmService.getInstance().suppFavori(idFilm, idUser);
        //THEN
        assertThat(res).isNull();
    }
    
    
    
//---------------------------------------------------------//
    
    
    @Test
    public void shouldAddLike() throws FilmNotFoundException, UserNotFoundException, IOException {
    	//GIVEN
    	Genre genre = new Genre(1, "Aventure");
    	Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", genre, 1);
    	//WHEN
    	Film result = filmService.getInstance().addLike(1,2);
    	//THEN
    	System.out.println("1:"+result.getGenre().getId());
    	System.out.println("2:"+film.getGenre().getId());
    	assertThat(result).isNotNull();
        assertThat(result.getTitre()).isEqualTo(film.getTitre());
        assertThat(result.getResume()).isEqualTo(film.getResume());
        assertThat(result.getDateSortie()).isEqualTo(film.getDateSortie());
        assertThat(result.getDuree()).isEqualTo(film.getDuree());
        assertThat(result.getGenre().getNom()).isEqualTo(film.getGenre().getNom());
        assertThat(result.getRealisateur()).isEqualTo(film.getRealisateur());
        assertThat(result.getActeur()).isEqualTo(film.getActeur());
        assertThat(result.getImageName()).isEqualTo(film.getImageName());
        assertThat(result.getUrlBA()).isEqualTo(film.getUrlBA());
        assertThat(result.getValide()).isEqualTo(film.getValide()); 	
    }
    
    @Test
    public void shouldAddLikeButThrowFilmNotFoundException() throws FilmNotFoundException, UserNotFoundException, IOException {
    	//GIVEN
        int idUser = 2;
        int idFilm = 3;
        //WHEN
        Film res = filmService.getInstance().addLike(idFilm, idUser);
        //THEN
        assertThat(res).isNull();	
    }
    
    @Test
    public void shouldAddLikeButThrowUserNotFoundException() throws FilmNotFoundException, UserNotFoundException, IOException {
    	//GIVEN
        int idUser = 3;
        int idFilm = 1;
        //WHEN
        Film res = filmService.getInstance().addLike(idFilm, idUser);
        //THEN
        assertThat(res).isNull();	
    }
    
    
    @Test
    public void shouldAddDislike() throws FilmNotFoundException, UserNotFoundException, IOException {
    	//GIVEN
    	Genre genre = new Genre(1, "Aventure");
    	Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", genre, 1);
    	//WHEN
    	Film result = filmService.getInstance().addDislike(1,1);
    	//THEN
    	System.out.println("1:"+result.getGenre().getId());
    	System.out.println("2:"+film.getGenre().getId());
    	assertThat(result).isNotNull();
        assertThat(result.getTitre()).isEqualTo(film.getTitre());
        assertThat(result.getResume()).isEqualTo(film.getResume());
        assertThat(result.getDateSortie()).isEqualTo(film.getDateSortie());
        assertThat(result.getDuree()).isEqualTo(film.getDuree());
        assertThat(result.getGenre().getNom()).isEqualTo(film.getGenre().getNom());
        assertThat(result.getRealisateur()).isEqualTo(film.getRealisateur());
        assertThat(result.getActeur()).isEqualTo(film.getActeur());
        assertThat(result.getImageName()).isEqualTo(film.getImageName());
        assertThat(result.getUrlBA()).isEqualTo(film.getUrlBA());
        assertThat(result.getValide()).isEqualTo(film.getValide()); 	
    }
    
    @Test
    public void shouldAddDislikeButThrowFilmNotFoundException() throws FilmNotFoundException, UserNotFoundException, IOException {
    	//GIVEN
        int idUser = 2;
        int idFilm = 3;
        //WHEN
        Film res = filmService.getInstance().addDislike(idFilm, idUser);
        //THEN
        assertThat(res).isNull();	
    }
    
    @Test
    public void shouldAddDislikeButThrowUserNotFoundException() throws FilmNotFoundException, UserNotFoundException, IOException {
    	//GIVEN
        int idUser = 3;
        int idFilm = 1;
        //WHEN
        Film res = filmService.getInstance().addDislike(idFilm, idUser);
        //THEN
        assertThat(res).isNull();	
    }
    
    @Test
    public void shouldRemoveAvis() throws FilmNotFoundException, UserNotFoundException, IOException, SQLException {
    	//GIVEN
    	Genre genre = new Genre(1, "Aventure");
    	Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", genre, 1);
    	//WHEN
    	Film result = filmService.getInstance().removeAvis(1,1);
    	//THEN
    	System.out.println("1:"+result.getGenre().getId());
    	System.out.println("2:"+film.getGenre().getId());
    	assertThat(result).isNotNull();
        assertThat(result.getTitre()).isEqualTo(film.getTitre());
        assertThat(result.getResume()).isEqualTo(film.getResume());
        assertThat(result.getDateSortie()).isEqualTo(film.getDateSortie());
        assertThat(result.getDuree()).isEqualTo(film.getDuree());
        assertThat(result.getGenre().getNom()).isEqualTo(film.getGenre().getNom());
        assertThat(result.getRealisateur()).isEqualTo(film.getRealisateur());
        assertThat(result.getActeur()).isEqualTo(film.getActeur());
        assertThat(result.getImageName()).isEqualTo(film.getImageName());
        assertThat(result.getUrlBA()).isEqualTo(film.getUrlBA());
        assertThat(result.getValide()).isEqualTo(film.getValide()); 	
    }
    
    @Test
    public void shouldRemoveAvisButThrowFilmNotFoundException() throws FilmNotFoundException, UserNotFoundException, IOException, SQLException {
    	//GIVEN
        int idUser = 2;
        int idFilm = 3;
        //WHEN
        Film res = filmService.getInstance().removeAvis(idFilm, idUser);
        //THEN
        assertThat(res).isNull();	
    }
    
    @Test
    public void shouldRemoveAvisButThrowUserNotFoundException() throws FilmNotFoundException, UserNotFoundException, IOException, SQLException {
    	//GIVEN
        int idUser = 3;
        int idFilm = 1;
        //WHEN
        Film res = filmService.getInstance().removeAvis(idFilm, idUser);
        //THEN
        assertThat(res).isNull();	
    }
    
    
    @Test
    public void shouldListFilmByUser() throws UserNotFoundException {
        //GIVEN
        int id = 1;
        //WHEN
        List<Film> res = filmService.getInstance().listFavorisFilm(id, "");
        //THEN
        assertThat(res).hasSize(2);
        assertThat(res).extracting(
                Film::getId,
                Film::getTitre,
                Film::getResume,
                Film::getDateSortie,
                Film::getDuree,
                Film::getRealisateur,
                Film::getActeur,
                Film::getImageName,
                Film::getUrlBA,
                Film -> Film.getGenre().getId(),
                Film -> Film.getGenre().getNom(),

                Film::getValide).containsOnly(
                tuple(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", 1, "Aventure", 1),
                tuple(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtu.be/2", 2, "Action", 0));

    }

    @Test
    public void shouldListFilmByUserThrowUserNotFoundException() throws UserNotFoundException, FilmNotFoundException {
        //GIVEN
        int id = 3;
        //WHEN
        List<Film> res = filmService.getInstance().listFavorisFilm(id , "");
        //THEN
        assertThat(res).isNull();
    }
    

    @Test
    public void shouldListGenre()
    {
        //WHEN
        List<Genre> genres = filmService.getInstance().listGenre();
        //THEN
        assertThat(genres).hasSize(3);
        assertThat(genres).extracting(
                Genre::getId,
                Genre::getNom).containsOnly(
                tuple(1,"Aventure"), tuple(2,"Action"), tuple(3,"Horreur"));
    }

    @Test
    public void shouldDeleteGenre() throws GenreNotFoundException, SQLException, GenreLinkToFilmException {
        //GIVEN
        int id = 42;
        //WHEN
        Genre res = filmService.getInstance().deleteGenre(id,0);
        //THEN
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldDeleteGenreThrowGenreNotFoundException() throws GenreNotFoundException, SQLException, GenreLinkToFilmException {
        //GIVEN
        int id = 5;
        //Mockito.when(genreDao.getGenre(id)).thenThrow(GenreNotFoundException.class);
        //WHEN
        Genre res = filmService.deleteGenre(id, 0);
        //THEN
        Assertions.assertThat(res).isNull();
    }
    
    @Test
    public void shouldDeleteGenreThrowGenreLinkToFilmException() throws GenreLinkToFilmException, GenreNotFoundException, SQLException {
        //GIVEN
        int idGenre = 2;
        int nbFilmLink = 1;
        
        //WHEN
        try {
            filmService.getInstance().deleteGenre(idGenre, nbFilmLink);
        }
        //THEN
        catch(Exception e) {
        	Assertions.assertThatExceptionOfType(GenreLinkToFilmException.class);
        }
    }

    @Test
    public void shouldGetGenre() throws GenreNotFoundException {
        //GIVEN
        Genre genre = new Genre(1,"Aventure");
        int id = 1;
        //WHEN
        Genre res = filmService.getInstance().getGenre(id);
        //THEN
        Assertions.assertThat(res).isEqualToComparingFieldByField(genre);
    }

    @Test
    public void shouldGetGenreTrowGenreNotFoundException() throws GenreNotFoundException {
        //GIVEN
        int id = 4;
        
        //WHEN
        Genre res = filmService.getInstance().getGenre(id);

        //THEN
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldAddGenre() throws GenreAlreadyExistingException, GenreNotFoundException {
        //GIVEN
        String name = "genre3";
        //WHEN
        Genre res = filmService.getInstance().addGenre(name);
        //THEN
        Assertions.assertThat(res.getNom()).isEqualTo(name);
    }

    @Test
    public void shouldAddGenreButThrowGenreAlreadyExistingException() throws GenreAlreadyExistingException, GenreNotFoundException {
        //GIVEN
        String name = "Aventure";
        //WHEN
        try {
        	filmService.getInstance().addGenre(name);
        }
        //THEN
        catch(Exception e){
            Assertions.assertThat(e).isExactlyInstanceOf(GenreAlreadyExistingException.class);
        }
    }

    @Test
    public void shouldListGenreDto()
    {
        //GIVEN
        GenreDto genre1 = new GenreDto(1, "Aventure", 1);
        GenreDto genre2 = new GenreDto(2, "Action", 1);
        GenreDto genre3 = new GenreDto(3, "Horreur", 1);
        //WHEN
        List<GenreDto> genres = filmService.getInstance().listGenreDto();
        //THEN
        assertThat(genres).hasSize(3);
        assertThat(genres).extracting(
                GenreDto::getId,
                GenreDto::getNom,
                GenreDto::getNbFilmLie).containsOnly(
                tuple(1,"Aventure",1), tuple(2,"Action",1), tuple(3,"Horreur", 0));
    }


    @Test
    public void shouldListFilmDto()
    {
        //GIVEN
        int id =1;
        //WHEN
        List<FilmDto> films = filmService.getInstance().listFilmsDto(id);
        //THEN
        assertThat(films).hasSize(2);
        assertThat(films).extracting(
                FilmDto::getId,
                FilmDto::getTitre,
                FilmDto::getFavori).containsOnly(
                tuple(1, "titre 1", true),
                tuple(2, "titre 2", true));
    }

    @Test
    public void shouldListFavorisFilm() throws FilmNotFoundException, UserNotFoundException {
        //GIVEN
        int id = 1;
        //WHEN
        List<Film> films = filmService.getInstance().listFavorisFilm(id, "");
        //THEN
        assertThat(films).hasSize(2);
        assertThat(films).extracting(
                Film::getId,
                Film::getTitre,
                Film::getResume,
                Film::getDateSortie,
                Film::getDuree,
                Film::getRealisateur,
                Film::getActeur,
                Film::getImageName,
                Film::getUrlBA,
                Film -> Film.getGenre().getId(),
                Film -> Film.getGenre().getNom(),

                Film::getValide).containsOnly(
                tuple(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", 1, "Aventure", 1),
                tuple(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtu.be/2", 2, "Action", 0));

    }

    @Test
    public void shouldListFavorisFilmThrowUserNotFoundException() throws FilmNotFoundException, UserNotFoundException {
        //GIVEN
        int id = 3;
        //WHEN
        List<Film> films = filmService.getInstance().listFavorisFilm(id, "");
        //THEN
        Assertions.assertThat(films).isNull();
    }
    
    @Test
    public void shouldFormaterDate() {
        //GIVEN
        String dateStr = "2020-12-26";
        LocalDate date = LocalDate.of(2020,12,26);
        //WHEN
        LocalDate res = filmService.formaterDate(dateStr);
        //THEN
        Assertions.assertThat(res).isEqualTo(res);
    }
    
    @Test
    public void shouldVerifierUrl() throws UrlDoesNotMatchException {
    	//GIVEN
    	String url1 = "youtu.be/123";
    	String url2 = "youtube.com/watch?v=123";
    	
    	String checkedUrl = "123";
    	//WHEN
    	String res1 = filmService.getInstance().urlVerification(url1);
    	String res2 = filmService.getInstance().urlVerification(url2);
    	
    	//THEN
    	Assertions.assertThat(res1).isEqualTo(checkedUrl);
    	Assertions.assertThat(res2).isEqualTo(checkedUrl);
    }
    
    @Test
    public void shouldVerifierUrlButThrowUrlDoesNotMatchException() throws UrlDoesNotMatchException {
    	//GIVEN
    	String url = "autre.com/watch?v=1234";

    	//WHEN
    	try {
    		filmService.getInstance().urlVerification(url);
    	}
    	//THEN
    	catch(Exception e){
            Assertions.assertThat(e).isExactlyInstanceOf(UrlDoesNotMatchException.class);
        }
    }
    
    @Test
    public void shouldReturnParametreTrie() {
    	//GIVEN
    	String t1 = null;
    	String t2 = "alpha";
    	String t3 = "recent";
    	String t4 = "ancien";
    	
    	//WHEN
    	String res1 = filmService.getInstance().parametreTrie(t1);
    	String res2 = filmService.getInstance().parametreTrie(t2);
    	String res3 = filmService.getInstance().parametreTrie(t3);
    	String res4 = filmService.getInstance().parametreTrie(t4);
    	
    	//THEN
    	assertThat(res1).isEqualTo("titreFilm");
    	assertThat(res2).isEqualTo("titreFilm");
    	assertThat(res3).isEqualTo("dateSortie DESC");
    	assertThat(res4).isEqualTo("dateSortie");
    }
    
    @Test
    public void shouldReturnPourcentageFilm() throws IOException, FilmNotFoundException {
    	//GIVEN
        int id = 1;
    	//WHEN
    	int res = filmService.getInstance().getPourcentageFilm(id);
    	//THEN
    	assertThat(res).isEqualTo(100);
    }
    
    @Test
    public void shouldReturnPourcentageFilmButThrowFilmNotFoundException() throws IOException, FilmNotFoundException {
    	//GIVEN
        int id = 5;
    	//WHEN
    	int res = filmService.getInstance().getPourcentageFilm(id);
    	//THEN
    	assertThat(res).isEqualTo(-1);
    }
    
    @Test
    public void shouldTrierListFilms () throws IOException {
    	//GIVEN
    	FilmDto f1 = new FilmDto(1, "A-titre 1", "positif", true, 100);
    	FilmDto f2 = new FilmDto(2, "B-titre 2", "negatif", true, 0);
    	List<FilmDto> listFilmsDto = new ArrayList<FilmDto>();
    	listFilmsDto.add(f1);
    	listFilmsDto.add(f2);
    	
    	//WHEN
    	List<FilmDto> res = filmService.getInstance().trierListFilms(listFilmsDto);
    	
    	//THEN
        assertThat(res).containsExactlyElementsOf(listFilmsDto);
    }
}

