package dao.impl;

import dao.GenreDao;
import entity.Film;
import entity.Genre;
import entity.Utilisateur;
import exception.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDaoImpl implements GenreDao {

    public List<Genre> listGenre() {
        List<Genre> listOfGenres = new ArrayList<Genre>();
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
            try (Statement stm = co.createStatement()) {
                try (ResultSet rs = stm.executeQuery("SELECT * FROM GENRE ORDER BY nomGenre;")) {
                    while (rs.next()) {
                        listOfGenres.add(new Genre(rs.getInt("idGenre"), rs.getString("nomGenre")));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfGenres;
    }

    @Override
    public Genre getGenre(Integer id) {
        Genre genre = null;
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
            try (PreparedStatement pStm = co.prepareStatement("SELECT * FROM GENRE WHERE idGenre =?;")) {
                pStm.setInt(1, id);
                try (ResultSet rs = pStm.executeQuery()) {
                    while (rs.next()) {
                        genre = new Genre(rs.getInt("idGenre"), rs.getString("nomGenre"));
                    }
                    if (genre == null)
                        throw new GenreNotFoundException();
                }
            }
        } catch (SQLException | GenreNotFoundException e) {
            e.printStackTrace();
        }
        return genre;
    }

    public Genre deleteGenre(Integer id) {
        Genre genre = null;
        genre = getGenre(id);
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
            try (PreparedStatement pStm = co.prepareStatement("DELETE FROM genre WHERE idGenre = ?;")) {
                pStm.setInt(1, id);
                pStm.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return genre;
    }

    @Override
    public void addGenre(String name) throws GenreAlreadyExistingException {
        List<Genre> genres = listGenre();
        boolean existing = false;
        for (int i = 0; i < genres.size(); i++) {
            if (genres.get(i).getNom().toLowerCase().equals(name.toLowerCase())) {
                existing = true;
            }
        }
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
            if (existing) {
                throw new GenreAlreadyExistingException();
            } else {
                try (PreparedStatement pStm = co.prepareStatement("INSERT INTO Genre (nomGenre) VALUES (?);")) {
                    pStm.setString(1, name);
                    pStm.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
