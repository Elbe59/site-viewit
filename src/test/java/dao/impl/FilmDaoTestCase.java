package dao.impl;

import dao.FilmDao;
import entity.Film;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import exception.FilmAlreadyActiveException;
import exception.FilmAlreadyDesactiveException;
import exception.FilmNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


public class FilmDaoTestCase {

	private FilmDao filmDao = new FilmDaoImpl();
	
	@Before
	public void initDb() throws Exception {
		try (Connection co = DataSourceProvider.getDataSource().getConnection();
				Statement stm = co.createStatement()) {
			stm.executeUpdate("DELETE FROM film");
			stm.executeUpdate("DELETE FROM genre");
			stm.executeUpdate("INSERT INTO genre (idGenre, nomGenre) "
					+ "VALUES (1,'Aventure'), "
					+"(2,'Action');");
			stm.executeUpdate(
					"INSERT INTO film(idFilm, titreFilm, resumeFilm, dateSortie, dureeFilm, realisateur, acteur, imgFilm, urlBA, idGenre, valide) "
							+ "VALUES (1, 'titre 1', 'resume 1', '2020-11-11', 123, 'realisateur 1', 'acteur 1', 'image1.png', 'youtube.com/1', 1, 1),"
							+ "(2, 'titre 2', 'resume 2', '2020-11-12', 123, 'realisateur 2', 'acteur 2', 'image2.png', 'youtube.com/2', 2, 0);");
		}
	}
	
	@Test
	public void souldListFilmWithParameter() {
		//WHEN
		List<Film> films = filmDao.listFilms("recent");
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
						tuple(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", 1, "Aventure", 1),
						tuple(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtube.com/2", 2, "Action", 0));
	}

	@Test
	public void souldListFilm()
	{
		//WHEN
		List<Film> films = filmDao.listFilms();
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
				tuple(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", 1, "Aventure", 1),
				tuple(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtube.com/2", 2, "Action", 0));
	}

	@Test
	public void shouldGetFilm() throws FilmNotFoundException {
		//WHEN
		Film film = filmDao.getFilm(1);
		//THEN
		assertThat(film).isNotNull();
		assertThat(film.getId()).isEqualTo(1);
		assertThat(film.getTitre()).isEqualTo("titre 1");
		assertThat(film.getResume()).isEqualTo("resume 1");
		assertThat(film.getDateSortie()).isEqualTo(LocalDate.of(2020,11,11));
		assertThat(film.getDuree()).isEqualTo(123);
		assertThat(film.getRealisateur()).isEqualTo("realisateur 1");
		assertThat(film.getActeur()).isEqualTo("acteur 1");
		assertThat(film.getImageName()).isEqualTo("image1.png");
		assertThat(film.getUrlBA()).isEqualTo("youtube.com/1");
		assertThat(film.getGenre().getId()).isEqualTo(1);
		assertThat(film.getGenre().getNom()).isEqualTo("Aventure");
		assertThat(film.getValide()).isEqualTo(1);
	}

	@Test (expected = FilmNotFoundException.class)
	public void shouldGetFilmThrowFilmNotFoundExcpetion() throws FilmNotFoundException
	{
		//given
		int id = 3;
		//when
		Film film = filmDao.getFilm(id);
		//then
		fail("Film not found not throw as expected");
	}

	@Test
	public void shouldActiveFilm() throws FilmNotFoundException, FilmAlreadyActiveException {
		//given
		int id = 2;
		//when
		Film res = filmDao.activeFilm(id);
		//then
		Assertions.assertThat(res.getValide()).isEqualTo(1);
	}

	@Test (expected = FilmNotFoundException.class)
	public void shouldActivateFilmThrowFilmNotFoundException() throws FilmNotFoundException, FilmAlreadyActiveException {
		//given
		int id = 3;
		//When
		Film res = filmDao.activeFilm(id);
		//then
		fail("Film not found not throw as expected");
	}

	@Test (expected = FilmAlreadyActiveException.class)
	public void shouldActivateFilmThrowFilmAlreadyActiveException() throws FilmAlreadyActiveException, FilmNotFoundException {
		//given
		int id = 1;
		//when
		Film res = filmDao.activeFilm(id);
		//then
		fail("Film already active not throw as expected");
	}

	@Test
	public void shouldDesactiveFilm() throws FilmNotFoundException, FilmAlreadyDesactiveException {
		//given
		int id = 1;
		//when
		Film res = filmDao.desactiveFilm(id);
		//then
		Assertions.assertThat(res.getValide()).isEqualTo(0);
	}

	@Test (expected = FilmNotFoundException.class)
	public void shouldDesactiveFilmThrowFilmNotFoundException() throws FilmNotFoundException, FilmAlreadyDesactiveException {
		//given
		int id = 3;
		//when
		Film res = filmDao.desactiveFilm(id);
		//then
		fail("Film not found not throw as expected");
	}

	@Test (expected = FilmAlreadyDesactiveException.class)
	public void shouldDesactiveFilmThrowFilmAlreadyDesactiveException() throws FilmNotFoundException, FilmAlreadyDesactiveException {
		//given
		int id = 2;
		//when
		Film res = filmDao.desactiveFilm(id);
		//then
		fail("Film already desactive not throw as expected");
	}

	@Test
	public void shouldDeleteFilm() throws SQLException, FilmNotFoundException {
		//GIVEN
		int id = 2;
		String title = "titre 2";
		//WHEN
		Film filmDelete = filmDao.deleteFilm(id);
		//Then
		try (Connection connection = DataSourceProvider.getDataSource().getConnection();
			 Statement stmt = connection.createStatement()) {
			try (ResultSet rs = stmt.executeQuery("SELECT * FROM film WHERE title = 'titre 2'")) {
				assertThat(rs.next()).isFalse();
			} catch (SQLException e) {
			}
			assertThat(filmDelete.getTitre()).isEqualTo(title);
		}
	}

	@Test (expected = FilmNotFoundException.class)
	public void shouldDelteFilmthrowFilmNotFoundException() throws FilmNotFoundException
	{
		//given
		int id = 3;
		//when
		Film res = filmDao.deleteFilm(id);
		//then
		fail("Film not found not throw as expected");
	}
	
}
