package dao.impl;

import dao.GenreDao;
import entity.Film;
import entity.Genre;
import entity.GenreDto;
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

    public List<GenreDto> listGenreDto(List<Genre> genreList) {
        List<GenreDto> listOfGenresDto = new ArrayList<GenreDto>();
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
            try (Statement stm = co.createStatement()) {
                for (Genre genre: genreList) {
                    try (ResultSet rs = stm.executeQuery("SELECT COUNT(*) from film join genre on genre.idGenre=film.idGenre WHERE film.idGenre="+genre.getId())) {
                        while (rs.next()) {
                            listOfGenresDto.add(new GenreDto(genre.getId(), genre.getNom(), rs.getInt(1)));
                        }
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfGenresDto;
    }

    @Override
    public Genre getGenre(Integer id) throws GenreNotFoundException {
        Genre genre = null;
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
            try (PreparedStatement pStm = co.prepareStatement("SELECT * FROM GENRE WHERE idGenre =?;")) {
                pStm.setInt(1, id);
                try (ResultSet rs = pStm.executeQuery()) {
                    while (rs.next()) {
                        genre = new Genre(rs.getInt("idGenre"), rs.getString("nomGenre"));
                    }
                    if (genre == null)
                        throw new GenreNotFoundException("Genre introuvable.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genre;
    }

    public Genre deleteGenre(Integer id) throws GenreNotFoundException {
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
    public Genre addGenre(String name) throws GenreAlreadyExistingException, GenreNotFoundException {
        List<Genre> genres = listGenre();
        boolean existing = false;
        Genre res = new Genre(name);
        for (int i = 0; i < genres.size(); i++) {
            if (genres.get(i).getNom().toLowerCase().equals(name.toLowerCase())) {
                existing = true;
            }
        }
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
            if (existing) {
                throw new GenreAlreadyExistingException("Le genre que vous essayé d'ajouter existe déjà.");
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
        res.setId(getSqlIdGenre(res));
        return res;
    }

    public int getSqlIdGenre(Genre genre) throws GenreNotFoundException {
        Integer id = null;
        try(Connection co = DataSourceProvider.getDataSource().getConnection()){
            try(PreparedStatement pStm = co.prepareStatement("SELECT idGenre FROM Genre WHERE nomGenre =? ")) {
                pStm.setString(1, genre.getNom());
                try(ResultSet rs = pStm.executeQuery()) {
                    while(rs.next()) {
                        id = rs.getInt("idGenre");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (id == null)
            throw new GenreNotFoundException("L'id du genre que vous essayez de récupérer est introuvable.");
        else
            return id;
    }
}
