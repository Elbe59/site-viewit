package dao.impl;

import dao.FilmDao;
import dao.GenreDao;
import dao.UtilisateurDao;
import entity.Film;
import entity.Genre;
import exception.*;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import service.FilmService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

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

    @Before
    public void initDb() throws Exception {
        try (Connection co = DataSourceProvider.getDataSource().getConnection();
             Statement stm = co.createStatement()) {
            stm.executeUpdate("DELETE FROM film");
            stm.executeUpdate("DELETE FROM genre");
            stm.executeUpdate("DELETE FROM commentaire");
            stm.executeUpdate("DELETE FROM preferer");
            stm.executeUpdate("DELETE FROM UTILISATEUR");
            stm.executeUpdate(
                    "INSERT INTO UTILISATEUR ( idUtilisateur, prenomUtilisateur, nomUtilisateur, email, mdp, mdpHash, admin) "
                            + "VALUES (1,'prenom1', 'nom1', 'email1@gmail.com', 'mdp1', 'mdpHash1', 0),"
                            + "(2,'prenom2', 'nom2', 'email2@gmail.com', 'mdp2', 'mdpHash2', 1);");
            stm.executeUpdate("INSERT INTO genre (idGenre, nomGenre) "
                    + "VALUES (1,'Aventure'), "
                    +"(2,'Action');");
            stm.executeUpdate(
                    "INSERT INTO film(idFilm, titreFilm, resumeFilm, dateSortie, dureeFilm, realisateur, acteur, imgFilm, urlBA, idGenre, valide, image) "
                            + "VALUES (1, 'titre 1', 'resume 1', '2020-11-11', 123, 'realisateur 1', 'acteur 1', 'image1.png', 'youtube.com/1', 1, 1, ''),"
                            + "(2, 'titre 2', 'resume 2', '2020-11-12', 123, 'realisateur 2', 'acteur 2', 'image2.png', 'youtube.com/2', 2, 0, '');");
            stm.executeUpdate(
                    "INSERT INTO preferer (favoris, idFilm, idUtilisateur, liker) VALUES (1, 1, 1,1);");
            stm.executeUpdate(
                    "INSERT INTO preferer (favoris, idFilm, idUtilisateur, liker) VALUES (1, 2, 1,1);");
        }
    }

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
                Film::getValide,
                Film::getBase64Image).containsOnly(
                tuple(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", 1, "Aventure", 1,""),
                tuple(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtube.com/2", 2, "Action", 0,""));
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
                Film::getValide,
                Film::getBase64Image).contains(
                tuple(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", 1, "Aventure", 1,""),
                tuple(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtube.com/2", 2, "Action", 0, ""));
    }

    @Test
    public void shouldAddFilm() throws IOException, FilmAlreadyExistingException, FilmNotFoundException {
        //given
        Genre genre = new Genre(1,"aventure");
        Film film = new Film("titre3","resume3",LocalDate.of(2019,12,20), 120, "realisateur3","acteur3","image3.png","youtube.com/3",genre,0,"");
        //when
        Film res = filmService.getInstance().addFilm("titre3","resume3","2019-12-20", 120,"realisateur3","acteur3","image3.png","youtube.com/3",genre,null);
        //then
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
        assertThat(res.getBase64Image()).isEqualTo(film.getBase64Image());
    }

    @Test
    public void shouldAddFilmThrowFilmAlreadyExistingException() throws IOException, FilmAlreadyExistingException, FilmNotFoundException {
        //given
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", genre, 1, "");
        //when
        Film res = filmService.getInstance().addFilm("titre 1", "resume 1", "2020-11-11", 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", genre, null);
        //then
        Mockito.verify(filmDao, Mockito.never()).getSqlIdFilm(film);
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldActiveFilm() throws FilmAlreadyActiveException, FilmNotFoundException {
        //given
        int id = 2;
        //when
        Film res = filmService.getInstance().activeFilm(id);
        //then
        Assertions.assertThat(res.getValide()).isEqualTo(1);
    }

    @Test
    public void shouldActiveFilmThrowFilmAlreadyActiveException() throws FilmAlreadyActiveException, FilmNotFoundException {
        //given
        int id = 1;
        //when
        Film res = filmService.getInstance().activeFilm(id);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldActiveFilmThrowFilmNotFoundException() throws FilmAlreadyActiveException, FilmNotFoundException {
        //given
        int id = 3;
        //Mockito.when(filmDao.getFilm(id)).thenThrow(FilmNotFoundException.class);
        //when
        Film res = filmService.getInstance().activeFilm(id);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldDesactiveFilm() throws FilmAlreadyDesactiveException, FilmNotFoundException {
        //given
        int id = 1;
        //when
        Film res = filmService.getInstance().desactiveFilm(id);
        //then
        Assertions.assertThat(res.getValide()).isEqualTo(0);
    }

    @Test
    public void shouldDesactiveFilmThrowFilmAlreadyDesactiveException() throws FilmAlreadyDesactiveException, FilmNotFoundException {
        //given
        int id = 2;
        //when
        Film res = filmService.getInstance().desactiveFilm(id);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldDesactiveFilmThrowFilmNotFoundException() throws FilmAlreadyDesactiveException, FilmNotFoundException {
        //given
        int id = 3;
        //Mockito.when(filmDao.getFilm(id)).thenThrow(FilmNotFoundException.class);
        //when
        Film res = filmService.getInstance().desactiveFilm(id);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldGetFilm() throws IOException, FilmNotFoundException {
        //given
        int id = 1;
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", genre, 1, "");
        //when
        Film res = filmService.getInstance().getFilm(id);
        //then
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
        assertThat(res.getBase64Image()).isEqualTo(film.getBase64Image());
    }

    @Test
    public void shouldGetFilmThrowFilmNotFoundException() throws FilmNotFoundException
    {
        //given
        int id = 3;
        //when
        Film res = filmService.getFilm(id);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldDeleteFilm() throws IOException, FilmNotFoundException {
        //given
        int id = 1;
        Genre genre = new Genre(1, "Aventure");
        Film film = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", genre, 1, "");
        //when
        Film res = filmService.getInstance().deleteFilm(id);
        //then
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
        assertThat(res.getBase64Image()).isEqualTo(film.getBase64Image());
    }

    @Test
    public void shouldDeleteFilmTrowFilmNotFoundException() throws FilmNotFoundException
    {
        //given
        int id = 3;
        //Mockito.when(filmDao.getFilm(id)).thenThrow(FilmNotFoundException.class);
        //when
        Film res = filmService.getInstance().deleteFilm(id);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldListGenre()
    {
        //when
        List<Genre> genres = filmService.getInstance().listGenre();
        //then
        assertThat(genres).hasSize(2);
        assertThat(genres).extracting(
                Genre::getId,
                Genre::getNom).containsOnly(
                tuple(1,"Aventure"), tuple(2,"Action"));
    }

    @Test
    public void shouldDelteGenre() throws GenreNotFoundException, SQLException, GenreLinkToFilmException {
        //given
        Genre genre = new Genre(1,"Aventure");
        int id = 1;
        //when
        Genre res = filmService.getInstance().deleteGenre(id,0);
        //then
        Assertions.assertThat(res).isEqualToComparingFieldByField(genre);
    }

    @Test
    public void shouldDelteGenreThrowGenreNotFoundException() throws GenreNotFoundException, SQLException, GenreLinkToFilmException {
        //given
        int id = 3;
        //Mockito.when(genreDao.getGenre(id)).thenThrow(GenreNotFoundException.class);
        //when
        Genre res = filmService.getInstance().deleteGenre(id, 0);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldGetGenre() throws GenreNotFoundException {
        //given
        Genre genre = new Genre(1,"Aventure");
        int id = 1;
        //when
        Genre res = filmService.getInstance().getGenre(id);
        //then
        Assertions.assertThat(res).isEqualToComparingFieldByField(genre);
    }

    @Test
    public void shouldGetGenreTrowGenreNotFoundException() throws GenreNotFoundException {
        //given
        int id = 3;
        //when
        Genre res = filmService.getInstance().getGenre(id);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldGetFilmByUser() throws FilmNotFoundException, UserNotFoundException {
        //given
        int id = 1;
        //when
        List<Film> res = filmService.getInstance().listFavorisFilm(id);
        //then
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
                Film::getValide,
                Film::getBase64Image).containsOnly(
                tuple(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", 1, "Aventure", 1,""),
                tuple(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtube.com/2", 2, "Action", 0,""));
    }

    @Test
    public void shouldGetFilmByUserThrowUserNotFoundException() throws UserNotFoundException, FilmNotFoundException {
        //given
        int id = 3;
        //Mockito.when(userDao.getUser(id)).thenThrow(UserNotFoundException.class);
        //when
        List<Film> res = filmService.getInstance().listFavorisFilm(id);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldAddGenre() throws GenreAlreadyExistingException, GenreNotFoundException {
        //given
        String name = "genre3";
        //when
        Genre res = filmService.getInstance().addGenre(name);
        //then
        Assertions.assertThat(res.getNom()).isEqualTo(name);
    }

    @Test
    public void shouldAddGenreThrowGenreAlreadyExistingException() throws GenreAlreadyExistingException, GenreNotFoundException {
        //given
        String name = "Aventure";
        //when
        Genre res = filmService.getInstance().addGenre(name);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldFormaterDate()
    {
        //given
        String dateStr = "2020-12-26";
        LocalDate date = LocalDate.of(2020,12,26);
        //when
        LocalDate res = filmService.getInstance().formaterDate(dateStr);
        //then
        Assertions.assertThat(res).isEqualTo(res);
    }
}
