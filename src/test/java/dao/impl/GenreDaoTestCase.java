package dao.impl;

import dao.GenreDao;
import entity.Genre;
import entity.Utilisateur;
import exception.*;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class GenreDaoTestCase {

    private GenreDao genreDao = new GenreDaoImpl();

    @Before
    public void initDb() throws Exception {
        try (Connection co = DataSourceProvider.getDataSource().getConnection();
             Statement stm = co.createStatement()) {
            stm.executeUpdate("DELETE FROM FILM");
            stm.executeUpdate("DELETE FROM preferer");
            stm.executeUpdate("DELETE FROM commentaire");
            stm.executeUpdate("DELETE FROM GENRE");
            stm.executeUpdate(
                    "INSERT INTO GENRE ( idGenre, nomGenre) VALUES (1,'genre1'), (2,'genre2');");
        }
    }

    @Test
    public void shouldListGenre()
    {
        //when
        List<Genre> genres = genreDao.listGenre();
        //then
        assertThat(genres).hasSize(2);
        assertThat(genres).extracting(
                Genre::getId,
                Genre::getNom).containsOnly(
                tuple(1,"genre1"), tuple(2,"genre2"));
    }

    @Test
    public void shouldGetGenre() throws GenreNotFoundException {
        //given
        int id = 1;
        Genre genre = new Genre(1, "genre1");
        //when
        Genre res = genreDao.getGenre(id);
        //then
        Assertions.assertThat(res).isEqualToComparingFieldByField(genre);
    }

    @Test (expected = GenreNotFoundException.class)
    public void shouldGetGenreThrowGenreNotFoundException() throws GenreNotFoundException
    {
        //given
        int id = 3;
        //when
        Genre res = genreDao.getGenre(id);
        //then
        fail("Genre not found not throw as expected");
    }

    @Test
    public void shouldDeleteGenre() throws GenreNotFoundException, SQLException {
        //given
        Genre genre = new Genre(1, "genre1");
        //when
        Genre res = genreDao.deleteGenre(genre.getId());
        //then
        Assertions.assertThat(res).isEqualToComparingFieldByField(genre);
        Genre search =null;
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
            try (PreparedStatement pStm = co.prepareStatement("SELECT * FROM GENRE WHERE idGenre =?;")) {
                pStm.setInt(1, genre.getId());
                try (ResultSet rs = pStm.executeQuery()) {
                    while (rs.next()) {
                        search = new Genre(rs.getInt("idGenre"), rs.getString("nomGenre"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); }
        Assertions.assertThat(search).isNull();
    }

    @Test (expected = GenreNotFoundException.class)
    public void shouldDeleteGenreThrowGenreNotFoundException() throws GenreNotFoundException, SQLException {
        //given
        int id = 3;
        //when
        Genre res = genreDao.deleteGenre(id);
        //then
        fail("Genre not found not throw as expected");
    }

    @Test
    public void shouldAddGenre() throws GenreAlreadyExistingException, GenreNotFoundException {
        //given
        String name = "genre3";
        //when
        genreDao.addGenre(name);
        //then
        Genre search =null;
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
            try (PreparedStatement pStm = co.prepareStatement("SELECT * FROM GENRE WHERE nomGenre =?;")) {
                pStm.setString(1, name);
                try (ResultSet rs = pStm.executeQuery()) {
                    while (rs.next()) {
                        search = new Genre(rs.getInt("idGenre"), rs.getString("nomGenre"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); }
        Assertions.assertThat(search).isNotNull();
        Assertions.assertThat(search.getNom()).isEqualTo(name);
    }

    @Test (expected = GenreAlreadyExistingException.class)
    public void shouldAddGenreThrowGenreAlreadyExistingException() throws GenreAlreadyExistingException, GenreNotFoundException {
        //given
        String name = "genre1";
        //when
        genreDao.addGenre(name);
        //then
        fail("genre already existing not throw as expected");
    }
}
