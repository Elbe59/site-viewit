package service;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.FilmDao;
import dao.GenreDao;
import dao.UtilisateurDao;
import dao.impl.FilmDaoImpl;
import dao.impl.GenreDaoImpl;
import dao.impl.UtilisateurDaoImpl;
import entity.Film;
import entity.FilmDto;
import entity.Genre;
import entity.GenreDto;
import exception.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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
    
    @Test
    public void shouldListFilm() throws IOException {
    	
    	//GIVEN
    	Film film1 = new Film();
    	Film film2 = new Film();
    	
		film1 = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", new Genre(1,"Aventure"), 1);
		film2 = new Film(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtube.com/2", new Genre(2,"Action"), 0);
    	
		List<Film> films = new ArrayList<Film>();
    	films.add(film1);
    	films.add(film2);
    	Mockito.when(filmDao.listFilms()).thenReturn(films);
        
    	//WHEN
    	List<Film> result = filmService.listFilms();
    	
    	//THEN
        assertThat(result).containsExactlyInAnyOrderElementsOf(films);
    }
    
    @Test
    public void souldListFilmWithParameter() throws IOException {
        
    	//GIVEN
        Film film1 = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", new Genre(1,"Aventure"), 1);
        Film film2 = new Film(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtube.com/2", new Genre(2,"Action"), 0);
        
        List<Film> films = new ArrayList<Film>();
        films.add(film1);
        films.add(film2);
        Mockito.when(filmDao.listFilms("dateSortie DESC")).thenReturn(films);
        
        //WHEN
        List<Film> res = filmService.listFilms("recent");
        
        //THEN
        assertThat(res).hasSize(2);
        assertThat(res.get(1).getDateSortie()).isEqualTo(LocalDate.of(2020, 11, 12));
        assertThat(res.get(0).getDateSortie()).isEqualTo(LocalDate.of(2020, 11, 11));
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
                Film::getValide).contains(
                tuple(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", 1, "Aventure", 1),
                tuple(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtube.com/2", 2, "Action", 0));
    }
    
    @Test
    public void shouldGetFilm() throws IOException, FilmNotFoundException {
        //GIVEN
        int id = 1;
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", genre, 1);
        Mockito.when(filmDao.getFilm(id)).thenReturn(film);
        //WHEN
        Film res = filmService.getFilm(id);
        //THEN
        assertThat(res).isNotNull();
        assertThat(res).isEqualToComparingFieldByField(film);
    }

    @Test
    public void shouldGetFilmThrowFilmNotFoundException() throws FilmNotFoundException {
        
    	//GIVEN
        int id = 42;
        Mockito.when(filmDao.getFilm(id)).thenThrow(new FilmNotFoundException("film non trouvé"));
        
        //WHEN
        Film res=null;
        try{
            res = filmService.getFilm(id);
        } catch (FilmNotFoundException e) {
            Assertions.assertThatExceptionOfType(FilmNotFoundException.class);
        }
        
        //THEN
        assertThat(res).isNull();
    }

    @Test
    public void shouldAddFilm() throws IOException, FilmAlreadyExistingException, FilmNotFoundException, UrlDoesNotMatchException {
        
    	//GIVEN
        Genre genre = new Genre(1,"aventure");
        Film film = new Film(1,"titre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png",".com/3",genre,0);
        Film film1 = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", new Genre(1,"Aventure"), 1);
        Film film2 = new Film(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtube.com/2", new Genre(2,"Action"), 0);
        
        List<Film> films = new ArrayList<Film>();
        films.add(film1);
        films.add(film2);
        
        Mockito.when(filmDao.listFilms()).thenReturn(films);
        Mockito.when(filmDao.addFilm(Mockito.any())).thenReturn(film);
        
        //WHEN
        Film res = filmService.addFilm("titre3","resume3","2019-12-20", 120,"realisateur3","acteur3","image3.png","youtube.com/3",genre);
        
        //THEN
        assertThat(res).isNotNull();
        assertThat(res).isEqualToComparingFieldByField(film);
    }

    @Test
    public void shouldAddFilmThrowFilmAlreadyExistingException() throws IOException, FilmAlreadyExistingException, UrlDoesNotMatchException {
        
    	//GIVEN
        Genre genre = new Genre(1,"aventure");
        Film film = new Film(1,"titre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png",".com/3",genre,0);
        
        List<Film> films = new ArrayList<Film>();
        films.add(film);
        
        Mockito.when(filmDao.listFilms()).thenReturn(films);
        
        //WHEN
        try {
            filmService.addFilm("titre3","resume3","2019-12-20", 120,"realisateur3","acteur3","image3.png","youtube.com/3",genre);
        } catch (Exception e) {
        //THEN
            assertThat(e).isInstanceOf(FilmAlreadyExistingException.class);
        }
    }
     
    @Test
    public void shouldAddFilmButThrowUrlDoesNotMatchException() throws IOException, FilmAlreadyExistingException, UrlDoesNotMatchException {
        
    	//GIVEN
        Genre genre = new Genre(1, "Aventure");
        
        //WHEN
        Film res = filmService.addFilm("titre 1", "resume 1", "2020-11-11", 123, "realisateur 1", "acteur 1", "image1.png", "daylimotion.com/1", genre);
        
        //THEN
        Assertions.assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(UrlDoesNotMatchException.class);
    }

    @Test
    public void shouldDeleteFilm() throws IOException, FilmNotFoundException {
        
    	//GIVEN
        int id = 1;
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtu.be/1", genre, 1);
        
        Mockito.when(filmDao.deleteFilm(id)).thenReturn(film);
        
        //WHEN
        Film res = filmService.deleteFilm(id);
        
        //THEN
        assertThat(res).isNotNull();
        assertThat(res).isNotNull();
        assertThat(res).isEqualToComparingFieldByField(film);
    }

    @Test
    public void shouldDeleteFilmTrowFilmNotFoundException() throws FilmNotFoundException {
        
    	//GIVEN
        int id = 3;
        
        Mockito.when(filmDao.deleteFilm(id)).thenThrow(new FilmNotFoundException("film not found"));
        
        //WHEN
        Film res = filmService.deleteFilm(id);
        
        //THEN
        Assertions.assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(FileNotFoundException.class);
    }
    
    @Test
    public void shouldUpdateFilm() throws IOException, FilmAlreadyExistingException, FilmNotFoundException, UrlDoesNotMatchException {
        
    	//GIVEN
        Genre genre = new Genre(1,"aventure");
        Film film = new Film(1,"newTitre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",genre,0);
        
        Mockito.when(filmDao.getFilm(1)).thenReturn(film);
        Mockito.when(filmDao.updateFilm(Mockito.any(),Mockito.any())).thenReturn(film);
        
        //WHEN
        Film res = filmService.updateFilm(1, "newTitre3", "resume3", "2019-12-20", 120, "realisateur3", "acteur3", "image3.png", "youtu.be/3", genre);
        
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
    public void shouldUpdateFilmTrowFilmNotFoundException() throws FilmNotFoundException, IOException {
        
    	//GIVEN
    	Genre genre = new Genre(1, "Aventure");
        
    	Mockito.when(filmDao.getFilm(1)).thenThrow(new FilmNotFoundException("film not found"));
        
    	//WHEN
        Film res = filmService.updateFilm(1, "newTitre3", "resume3", "2019-12-20", 120, "realisateur3", "acteur3", "image3.png", "youtu.be/3", genre);
        
        //THEN
        Assertions.assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(FileNotFoundException.class);
    }
    
    @Test
    public void shouldUpdateFilmButThrowUrlDoesNotMatchException() throws IOException, FilmAlreadyExistingException, UrlDoesNotMatchException, FilmNotFoundException {
        
    	//GIVEN
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1,"newTitre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",genre,0);
        
        Mockito.when(filmDao.getFilm(1)).thenReturn(film);
        
        //WHEN
        Film res = filmService.updateFilm(1, "newTitre3", "resume3", "2019-12-20", 120, "realisateur3", "acteur3", "image3.png", "1234567", genre);
        
        //THEN
        Assertions.assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(UrlDoesNotMatchException.class);
    }
    
    @Test
    public void shouldActiveFilm() throws FilmAlreadyActiveException, FilmNotFoundException, IOException {
        
    	//GIVEN
        int id = 1;
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1,"newTitre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",genre,1);

        Mockito.when(filmDao.activeFilm(id)).thenReturn(film);
        
        //WHEN
        Film res = filmService.activeFilm(id);
        
        //THEN
        Assertions.assertThat(res.getValide()).isEqualTo(1);
        Assertions.assertThat(res).isEqualToComparingFieldByField(film);
    }

    @Test 
    public void shouldActiveFilmThrowFilmAlreadyActiveException() throws FilmAlreadyActiveException, FilmNotFoundException, IOException {
        
    	//GIVEN
        int id = 1;

        Mockito.when(filmDao.activeFilm(id)).thenThrow(new FilmAlreadyActiveException("film already active"));
        
        //WHEN
        Film res = filmService.activeFilm(id);
        
        //THEN
        Assertions.assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(FilmAlreadyActiveException.class);
    }

    @Test 
    public void shouldActiveFilmThrowFilmNotFoundException() throws FilmAlreadyActiveException, FilmNotFoundException, IOException {
        
    	//GIVEN
        int id = 1;

        Mockito.when(filmDao.activeFilm(id)).thenThrow(new FilmNotFoundException("film not found"));
        
        //WHEN
        Film res = filmService.activeFilm(id);
        
        //THEN
        Assertions.assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(FileNotFoundException.class);
    }

    @Test
    public void shouldDesactiveFilm() throws FilmAlreadyDesactiveException, FilmNotFoundException, IOException {
        
    	//GIVEN
        int id = 1;
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1,"newTitre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",genre,0);

        Mockito.when(filmDao.desactiveFilm(id)).thenReturn(film);
        
        //WHEN
        Film res = filmService.desactiveFilm(id);
        
        //THEN
        Assertions.assertThat(res.getValide()).isEqualTo(0);
        Assertions.assertThat(res).isEqualToComparingFieldByField(film);
    }

    @Test
    public void shouldDesactiveFilmThrowFilmAlreadyDesactiveException() throws FilmAlreadyDesactiveException, FilmNotFoundException, IOException {
        
    	//GIVEN
        int id = 1;

        Mockito.when(filmDao.desactiveFilm(id)).thenThrow(new FilmAlreadyDesactiveException("film already desactive"));
        
        //WHEN
        Film res = filmService.desactiveFilm(id);
        
        //THEN
        Assertions.assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(FilmAlreadyDesactiveException.class);
    }

    @Test
    public void shouldDesactiveFilmThrowFilmNotFoundException() throws FilmAlreadyDesactiveException, FilmNotFoundException, IOException {
        
    	//GIVEN
        int id = 1;

        Mockito.when(filmDao.desactiveFilm(id)).thenThrow(new FilmNotFoundException("film not found"));
        
        //WHEN
        Film res=null;
        try{
            res = filmService.desactiveFilm(id);
        } catch (FilmNotFoundException e) {
            Assertions.assertThatExceptionOfType(FilmNotFoundException.class);
        }
        
        //THEN
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldAddFavoris() throws IOException, FilmNotFoundException, UserNotFoundException {
        
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;
        
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1,"newTitre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",genre,0);

        Mockito.when(filmDao.addFavori(idFilm,idUser)).thenReturn(film);
        
        //WHEN
        Film res = filmService.addFavori(idFilm, idUser);
        
        //THEN
        assertThat(res).isNotNull();
        Mockito.verify(filmDao,Mockito.times(1)).addFavori(idFilm,idUser);
    }

    @Test
    public void shouldAddFavorisThrowUserNotFound() throws IOException, FilmNotFoundException, UserNotFoundException {
        
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;

        Mockito.when(filmDao.addFavori(idFilm,idUser)).thenThrow(UserNotFoundException.class);
        
        //WHEN
        Film res = filmService.addFavori(idFilm, idUser);
        
        //THEN
        assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(UserNotFoundException.class);
        Mockito.verify(filmDao,Mockito.times(1)).addFavori(idFilm,idUser);
    }

    @Test
    public void shouldAddFavorisThrowFilmNotFoundException() throws IOException, FilmNotFoundException, UserNotFoundException {
        
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;

        Mockito.when(filmDao.addFavori(idFilm,idUser)).thenThrow(FilmNotFoundException.class);
        
        //WHEN
        Film res = filmService.addFavori(idFilm, idUser);
        
        //THEN
        assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(FilmNotFoundException.class);
        Mockito.verify(filmDao,Mockito.times(1)).addFavori(idFilm,idUser);
    }

    @Test
    public void shouldSuppFavoris() throws IOException, FilmNotFoundException, SQLException, UserNotFoundException {
        
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;
        
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1,"newTitre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",genre,0);

        Mockito.when(filmDao.suppFavori(idFilm,idUser)).thenReturn(film);
        
        //WHEN
        Film res = filmService.suppFavori(idFilm, idUser);
        
        //THEN
        assertThat(res).isNotNull();
        Mockito.verify(filmDao,Mockito.times(1)).suppFavori(idFilm,idUser);
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
        int idUser = 2;
        int idFilm = 1;

        Mockito.when(filmDao.suppFavori(idFilm,idUser)).thenThrow(FilmNotFoundException.class);
        
        //WHEN
        Film res = filmService.suppFavori(idFilm, idUser);
        
        //THEN
        assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(FilmNotFoundException.class);
        Mockito.verify(filmDao,Mockito.times(1)).suppFavori(idFilm,idUser);
    }

    @Test
    public void shouldSuppFavorisThrowFilmNotFoundException() throws IOException, FilmNotFoundException, SQLException, UserNotFoundException {
        
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;

        Mockito.when(filmDao.suppFavori(idFilm,idUser)).thenThrow(FilmNotFoundException.class);
        
        //WHEN
        Film res = filmService.suppFavori(idFilm, idUser);
        
        //THEN
        assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(FilmNotFoundException.class);
        Mockito.verify(filmDao,Mockito.times(1)).suppFavori(idFilm,idUser);
    }    
    
    @Test
    public void shouldAddLike() throws FilmNotFoundException, UserNotFoundException, IOException {
        
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;
        
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1,"newTitre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",genre,0);

        Mockito.when(filmDao.addLike(idFilm,idUser)).thenReturn(film);
        
        //WHEN
        Film res = filmService.addLike(idFilm, idUser);
        
        //THEN
        assertThat(res).isNotNull();
        Mockito.verify(filmDao,Mockito.times(1)).addLike(idFilm,idUser);
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
    public void shouldAddLikeButThrowFilmNotFoundException() throws FilmNotFoundException, UserNotFoundException, IOException {
        
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;

        Mockito.when(filmDao.addLike(idFilm,idUser)).thenThrow(FilmNotFoundException.class);
        
        //WHEN
        Film res = filmService.addLike(idFilm, idUser);
        
        //THEN
        assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(FilmNotFoundException.class);
        Mockito.verify(filmDao,Mockito.times(1)).addLike(idFilm,idUser);
    }
    
    @Test
    public void shouldAddLikeButThrowUserNotFoundException() throws FilmNotFoundException, UserNotFoundException, IOException {
        
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;

        Mockito.when(filmDao.addLike(idFilm,idUser)).thenThrow(UserNotFoundException.class);
        
        //WHEN
        Film res = filmService.addLike(idFilm, idUser);
        
        //THEN
        assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(UserNotFoundException.class);
        Mockito.verify(filmDao,Mockito.times(1)).addLike(idFilm,idUser);
    }
    
    @Test
    public void shouldAddDislike() throws FilmNotFoundException, UserNotFoundException, IOException {
        
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;
        
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1,"newTitre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",genre,0);

        Mockito.when(filmDao.addDislike(idFilm,idUser)).thenReturn(film);
        
        //WHEN
        Film res = filmService.addDislike(idFilm, idUser);
        
        //THEN
        assertThat(res).isNotNull();
        Mockito.verify(filmDao,Mockito.times(1)).addDislike(idFilm,idUser);
        Assertions.assertThat(res).isEqualTo(film);
    }
    
    @Test
    public void shouldAddDislikeButThrowFilmNotFoundException() throws FilmNotFoundException, UserNotFoundException, IOException {
        
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;

        Mockito.when(filmDao.addDislike(idFilm,idUser)).thenThrow(FilmNotFoundException.class);
        
        //WHEN
        Film res = filmService.addDislike(idFilm, idUser);
        
        //THEN
        assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(FilmNotFoundException.class);
        Mockito.verify(filmDao,Mockito.times(1)).addDislike(idFilm,idUser);
    }
    
    @Test
    public void shouldAddDislikeButThrowUserNotFoundException() throws FilmNotFoundException, UserNotFoundException, IOException {
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;

        Mockito.when(filmDao.addDislike(idFilm,idUser)).thenThrow(UserNotFoundException.class);
        
        //WHEN
        Film res = filmService.addDislike(idFilm, idUser);
        
        //THEN
        assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(UserNotFoundException.class);
        Mockito.verify(filmDao,Mockito.times(1)).addDislike(idFilm,idUser);
    }
    
    @Test
    public void shouldRemoveAvis() throws FilmNotFoundException, UserNotFoundException, IOException, SQLException {
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;
        
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1,"newTitre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",genre,0);

        Mockito.when(filmDao.removeAvis(idFilm,idUser)).thenReturn(film);
        
        //WHEN
        Film res = filmService.removeAvis(idFilm, idUser);
        
        //THEN
        assertThat(res).isNotNull();
        Mockito.verify(filmDao,Mockito.times(1)).removeAvis(idFilm,idUser);
        Assertions.assertThat(res).isEqualTo(film);
    }
    
    @Test
    public void shouldRemoveAvisButThrowFilmNotFoundException() throws FilmNotFoundException, UserNotFoundException, IOException, SQLException {
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;

        Mockito.when(filmDao.removeAvis(idFilm,idUser)).thenThrow(FilmNotFoundException.class);
        
        //WHEN
        Film res = filmService.removeAvis(idFilm, idUser);
        
        //THEN
        assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(FilmNotFoundException.class);
        Mockito.verify(filmDao,Mockito.times(1)).removeAvis(idFilm,idUser);
    }
    
    @Test
    public void shouldRemoveAvisButThrowUserNotFoundException() throws FilmNotFoundException, UserNotFoundException, IOException, SQLException {    
    	//GIVEN
        int idUser = 2;
        int idFilm = 1;

        Mockito.when(filmDao.removeAvis(idFilm,idUser)).thenThrow(UserNotFoundException.class);
        
        //WHEN
        Film res = filmService.removeAvis(idFilm, idUser);
        
        //THEN
        assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(UserNotFoundException.class);
        Mockito.verify(filmDao,Mockito.times(1)).removeAvis(idFilm,idUser);
    }
    
    @Test
    public void shouldListFilmDto() throws IOException {
    	//GIVEN
        int idUser = 1;
        
        List<FilmDto> filmDtoList = new ArrayList<>();
        FilmDto film1 = new FilmDto(1,"newTitre1","+",true,95);
        FilmDto film2 = new FilmDto(2,"newTitre2","-",false,10);
        filmDtoList.add(film1);
        filmDtoList.add(film2);
        
        Mockito.when(filmDao.listFilmsDto(idUser)).thenReturn(filmDtoList);
        
        //WHEN
        List<FilmDto> res = filmService.listFilmsDto(idUser);
        
        //THEN
        assertThat(res).hasSize(2);
        Assertions.assertThat(res).isEqualTo(filmDtoList);
        Mockito.verify(filmDao,Mockito.times(1)).listFilmsDto(idUser);
    }
    
    @Test
    public void shouldReturnPourcentageFilm() throws IOException, FilmNotFoundException {
    	
    	//GIVEN
        Film film = new Film(1,"newTitre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",new Genre(1,"Drame"),0);
        int id = 1;
        
        Mockito.when(filmDao.getFilm(id)).thenReturn(film);
        Mockito.when(filmDao.getPourcentageFilm(id)).thenReturn(85);
    	
        //WHEN
    	int res = filmService.getPourcentageFilm(id);
    	
    	//THEN
    	assertThat(res).isEqualTo(85);
        Mockito.verify(filmDao,Mockito.times(1)).getFilm(1);
        Mockito.verify(filmDao,Mockito.times(1)).getPourcentageFilm(1);
    }
    
    @Test
    public void shouldReturnPourcentageFilmButThrowFilmNotFoundException() throws IOException, FilmNotFoundException {
        
    	//GIVEN
        int id = 1;
        Mockito.when(filmDao.getFilm(id)).thenThrow(FilmNotFoundException.class);
        
        //WHEN
        int res = filmService.getPourcentageFilm(id);
        
        //THEN
        assertThat(res).isEqualTo(-1);
        Mockito.verify(filmDao,Mockito.times(1)).getFilm(1);
        Mockito.verify(filmDao,Mockito.never()).getPourcentageFilm(Mockito.any());
    }
    
    @Test
    public void shouldTrierListFilms () throws IOException {
    	
    	//GIVEN
    	FilmDto f1 = new FilmDto(1, "A-titre 1", "positif", true, 100);
    	FilmDto f2 = new FilmDto(2, "B-titre 2", "negatif", true, 0);
    	
    	List<FilmDto> listFilmsDto = new ArrayList<FilmDto>();
    	listFilmsDto.add(f1);
    	listFilmsDto.add(f2);
    	
    	Mockito.when(filmDao.trierListFilms(listFilmsDto)).thenReturn(listFilmsDto);
    	
    	//WHEN
    	List<FilmDto> res = filmService.trierListFilms(listFilmsDto);
    	
    	//THEN
        assertThat(res).containsExactlyElementsOf(listFilmsDto);
        Mockito.verify(filmDao,Mockito.times(1)).trierListFilms(listFilmsDto);

    }

    @Test
    public void shouldListFavorisFilmOfUser() throws IOException, UserNotFoundException {
    	//GIVEN
        String trie="cc";
        int idUser = 1;
        
        Genre genre = new Genre(1, "Aventure");
        Genre genre2 = new Genre(2, "Drame");
        
        List<Film> filmList = new ArrayList<>();
        Film film = new Film(1,"newTitre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","3",genre,0);
        Film film2 = new Film(2,"newTitre2","resume2",LocalDate.of(2009,12,20), 120, "realisateur2","acteur2","image2.png","2",genre2,1);
        filmList.add(film);
        filmList.add(film2);
        
        Mockito.when(filmDao.listFavorisFilm(idUser,"titreFilm")).thenReturn(filmList);
        
        //WHEN
        List<Film> res = filmService.listFavorisFilm(idUser,trie);
        
        //THEN
        assertThat(res).hasSize(2);
        Assertions.assertThat(res).isEqualTo(filmList);
        Mockito.verify(filmDao,Mockito.times(1)).listFavorisFilm(idUser,"titreFilm");
    }

    @Test
    public void shouldListFavorisFilmOfUserButThrowUserNotFoundException() throws UserNotFoundException, FilmNotFoundException {
        
    	//GIVEN
    	int idUser = 1;
        
    	Mockito.when(filmDao.listFavorisFilm(idUser,"titreFilm")).thenThrow(UserNotFoundException.class);
        
    	//WHEN
    	List<Film> res= filmService.listFavorisFilm(idUser,"cc");
        
        //THEN
        Assertions.assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(UserNotFoundException.class);
    }
    
    @Test
    public void shouldListGenre() {
        
    	//GIVEN
    	Genre genre=new Genre(1,"A");
        Genre genre2=new Genre(2,"Z");
        
        List<Genre> genreList=new ArrayList<>();
        genreList.add(genre);
        genreList.add(genre2);
        
        Mockito.when(genreDao.listGenre()).thenReturn(genreList);
        
        //WHEN
        List<Genre> res = filmService.listGenre();
        
        //THEN
        Assertions.assertThat(res).hasSize(2);
        Assertions.assertThat(res).isEqualTo(genreList);
        Mockito.verify(genreDao,Mockito.times(1)).listGenre();
    }

    @Test
    public void shouldDeleteGenre() throws GenreNotFoundException, SQLException, GenreLinkToFilmException {
        
    	//GIVEN
    	Genre genre = new Genre(1, "Aventure");
    	
    	Mockito.when(genreDao.getGenre(1)).thenReturn(genre);
    	Mockito.when(genreDao.deleteGenre(1)).thenReturn(genre);
        
    	//WHEN
        Genre res = filmService.deleteGenre(1,0);
        
        //THEN
        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res).isEqualTo(genre);
        Mockito.verify(genreDao,Mockito.times(1)).deleteGenre(1);
        Mockito.verify(genreDao,Mockito.times(1)).getGenre(1);

    }

    @Test
    public void shouldDeleteGenreThrowGenreNotFoundException() throws GenreNotFoundException, SQLException, GenreLinkToFilmException {
        
    	//GIVEN
        Mockito.when(genreDao.getGenre(1)).thenThrow(GenreNotFoundException.class);
        
        //WHEN
        Genre res = filmService.deleteGenre(1,0);
        
        //THEN
        Assertions.assertThat(res).isNull();
        Mockito.verify(genreDao,Mockito.never()).deleteGenre(Mockito.any());
        Mockito.verify(genreDao,Mockito.times(1)).getGenre(1);
    }
    
    @Test
    public void shouldDeleteGenreThrowGenreLinkToFilmException() throws GenreLinkToFilmException, GenreNotFoundException, SQLException {
        
    	//GIVEN
        int idGenre = 2;
        int nbFilmLink = 1;
        Genre res=null;
        
        //WHEN
        try{
            res = filmService.deleteGenre(idGenre,nbFilmLink);
        }
        
        //THEN
        catch (GenreLinkToFilmException e) {
            Assertions.assertThatExceptionOfType(GenreLinkToFilmException.class);
        }
        Assertions.assertThat(res).isNull();
        Mockito.verify(genreDao,Mockito.never()).deleteGenre(Mockito.any());
        Mockito.verify(genreDao,Mockito.never()).getGenre(Mockito.any());
    }

    @Test
    public void shouldGetGenre() throws GenreNotFoundException {
        
    	//GIVEN
        Genre genre = new Genre(1,"Aventure");
        int id = 1;
        Mockito.when(genreDao.getGenre(id)).thenReturn(genre);
        
        //WHEN
        Genre res = filmService.getGenre(id);
        
        //THEN
        Assertions.assertThat(res).isEqualToComparingFieldByField(genre);
        Mockito.verify(genreDao,Mockito.times(1)).getGenre(id);
    }

    @Test
    public void shouldGetGenreTrowGenreNotFoundException() throws GenreNotFoundException {
        
    	//GIVEN
        int id = 1;
        Mockito.when(genreDao.getGenre(id)).thenThrow(GenreNotFoundException.class);
        
        //WHEN
        Genre res = filmService.getGenre(id);
        
        //THEN
        Assertions.assertThat(res).isNull();
        Assertions.assertThatExceptionOfType(GenreNotFoundException.class);
        Mockito.verify(genreDao,Mockito.times(1)).getGenre(id);
    }

    @Test
    public void shouldAddGenre() throws GenreAlreadyExistingException, GenreNotFoundException {
        
    	//GIVEN
    	Genre genre=new Genre(1,"A");
        Genre genre2=new Genre(2,"Z");
        Genre new_genre=new Genre(3,"C");
        
        List<Genre> genreList=new ArrayList<>();
        genreList.add(genre);
        genreList.add(genre2);
        String name="C" ;
        
        Mockito.when(genreDao.listGenre()).thenReturn(genreList);
        Mockito.when(genreDao.addGenre(name)).thenReturn(new_genre);
        
        //WHEN
        Genre res=filmService.addGenre(name);
        
        //THEN
        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res).isEqualTo(new_genre);
        Mockito.verify(genreDao,Mockito.times(1)).listGenre();
        Mockito.verify(genreDao,Mockito.times(1)).addGenre(name);
    }

    @Test
    public void shouldAddGenreButThrowGenreAlreadyExistingException() throws GenreAlreadyExistingException, GenreNotFoundException {
        
    	//GIVEN
    	Genre genre=new Genre(1,"A");
        Genre genre2=new Genre(2,"Z");
        
        List<Genre> genreList=new ArrayList<>();
        genreList.add(genre);
        genreList.add(genre2);
        String name="A" ;
        
        Mockito.when(genreDao.listGenre()).thenReturn(genreList);
        Genre res=null;
        
        //When
        try{
            res=filmService.addGenre(name);
        } catch (GenreAlreadyExistingException e) {
            Assertions.assertThatExceptionOfType(GenreAlreadyExistingException.class);
        }
        
        //Then
        Assertions.assertThatExceptionOfType(GenreAlreadyExistingException.class);
        Assertions.assertThat(res).isNull();
        Mockito.verify(genreDao,Mockito.times(1)).listGenre();
        Mockito.verify(genreDao,Mockito.never()).addGenre(name);
    }

    @Test
    public void shouldListGenreDto() {
        
    	//GIVEN
        Genre genre=new Genre(1,"A");
        Genre genre2=new Genre(2,"Z");
        Genre new_genre=new Genre(3,"C");
        
        List<Genre> genreList=new ArrayList<>();
        genreList.add(genre);
        genreList.add(genre2);
        genreList.add(new_genre);
        
        GenreDto genreDto1 = new GenreDto(1, "Aventure", 1);
        GenreDto genreDto2 = new GenreDto(2, "Action", 1);
        GenreDto genreDto3 = new GenreDto(3, "Horreur", 1);
        List<GenreDto> ListGenreDto =new ArrayList<GenreDto>();
        ListGenreDto.add(genreDto1);
        ListGenreDto.add(genreDto2);
        ListGenreDto.add(genreDto3);
        
        //WHEN
        Mockito.when(genreDao.listGenre()).thenReturn(genreList);
        Mockito.when(genreDao.listGenreDto(genreList)).thenReturn(ListGenreDto);
        
        //THEN
        List<GenreDto> genres = filmService.listGenreDto();
        assertThat(genreList).hasSize(3);
        assertThat(genreList).hasSize(3);
        assertThat(genres).hasSize(3);
        Mockito.verify(genreDao,Mockito.times(1)).listGenre();
        Mockito.verify(genreDao,Mockito.times(1)).listGenreDto(genreList);
    }
    
  //----------Test Autre----------//
    
    @Test
    public void shouldFormaterDate() {
        
    	//GIVEN
        String dateStr = "2020-12-26";
        
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
    	String res1 = FilmService.getInstance().urlVerification(url1);
    	String res2 = FilmService.getInstance().urlVerification(url2);
    	
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
    		filmService.urlVerification(url);
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
    	String t5 = "Popularite";
    	
    	//WHEN
    	String res1 = filmService.parametreTrie(t1);
    	String res2 = filmService.parametreTrie(t2);
    	String res3 = filmService.parametreTrie(t3);
    	String res4 = filmService.parametreTrie(t4);
    	String res5 = filmService.parametreTrie(t5);
    	
    	//THEN
    	assertThat(res1).isEqualTo("titreFilm");
    	assertThat(res2).isEqualTo("titreFilm");
    	assertThat(res3).isEqualTo("dateSortie DESC");
    	assertThat(res4).isEqualTo("dateSortie");
    	assertThat(res5).isEqualTo("Popularite");
    }
}

