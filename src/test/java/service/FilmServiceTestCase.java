package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import dao.FilmDao;
import dao.GenreDao;
import dao.impl.DataSourceProvider;
import dao.impl.FilmDaoImpl;
import dao.impl.GenreDaoImpl;
import entity.Film;
import entity.Genre;

import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class FilmServiceTestCase {
	
    @InjectMocks
    FilmService filmService;
    @Mock
    FilmDao filmDao = new FilmDaoImpl();
    @Mock
    GenreDao genreDao = new GenreDaoImpl();
    
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
        }
    }
    @Test
    public void shouldListFilm()
    {
    	//GIVEN
    	Film film1 = new Film();
    	Film film2 = new Film();
    	try {
			film1 = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", new Genre(1,"Aventure"), 1,"");
			film2 = new Film(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtube.com/2", new Genre(2,"Action"), 0,"");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	List<Film> films = new ArrayList<Film>();
    	films.add(film1);
    	films.add(film2);
    	Mockito.when(filmService.listFilms()).thenReturn(films);
        //WHEN
    	List<Film> result = filmService.listFilms();
    	//THEN
        assertThat(result).containsExactlyInAnyOrderElementsOf(films);
    }
}