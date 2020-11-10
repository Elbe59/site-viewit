package dao.impl;

import dao.FilmDao;
import entity.Film;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.Before;
import org.junit.Test;



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
					"INSERT INTO film(idFilm, titreFilm, resumeFilm, dateSortie, dureeFilm, realisateur, acteur, imgFilm, urlBA, idGenre) "
							+ "VALUES (1, 'titre 1', 'resume 1', '2020-11-11', 123, 'realisateur 1', 'acteur 1', 'image1.png', 'youtube.com/1', 1),"
							+ "(2, 'titre 2', 'resume 2', '2020-11-12', 123, 'realisateur 2', 'acteur 2', 'image2.png', 'youtube.com/2', 2);");
		}
	}
	
	@Test
	public void shouldListFilm() {
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
				Film -> Film.getGenre().getNom()).containsOnly(
						tuple(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", 1, "Aventure"),
						tuple(2, "titre 2", "resume 2", LocalDate.of(2020, 11, 12), 123, "realisateur 2", "acteur 2", "image2.png", "youtube.com/2", 2, "Action"));
	}
	
}
