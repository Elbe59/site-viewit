package dao.impl;

import dao.GenreDao;
import entity.Film;
import entity.Genre;
import entity.GenreDto;
import entity.Utilisateur;
import exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDaoImpl implements GenreDao {

    static final Logger LOGGER = LogManager.getLogger();

    public List<Genre> listGenre() {
        LOGGER.debug("trying to list genres");
        List<Genre> listOfGenres = new ArrayList<Genre>();
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
            try (Statement stm = co.createStatement()) {
                try (ResultSet rs = stm.executeQuery("SELECT * FROM genre ORDER BY nomGenre;")) {
                    while (rs.next()) {
                        listOfGenres.add(new Genre(rs.getInt("idGenre"), rs.getString("nomGenre")));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("error listing genres");
            e.printStackTrace();
        }
        LOGGER.debug("returned list genres of size "+listOfGenres.size());
        return listOfGenres;
    }

    public List<GenreDto> listGenreDto(List<Genre> genreList) {
        LOGGER.debug("trying to list genres, dto format");
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
            LOGGER.error("error listing genres dto");
            e.printStackTrace();
        }
        LOGGER.debug("returned list genres dto of size "+listOfGenresDto.size());
        return listOfGenresDto;
    }

    public Genre getGenre(Integer id) throws GenreNotFoundException {
        LOGGER.debug("Trying to get genre nb "+id);
        Genre genre = null;
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
            try (PreparedStatement pStm = co.prepareStatement("SELECT * FROM genre WHERE idGenre =?;")) {
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
            LOGGER.error("error while getting genre nb "+id);
            e.printStackTrace();
        }
        LOGGER.debug("Returned genre "+genre.getNom());
        return genre;
    }

    public Genre deleteGenre(Integer id) {
        LOGGER.debug("Trying to delete genre nb "+id);
        Genre genre = null;
        try {
			genre = getGenre(id);
		} catch (GenreNotFoundException e) {
			e.printStackTrace();
		}
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {    	
            try (PreparedStatement pStm = co.prepareStatement("DELETE FROM genre WHERE idGenre = ?;")) {
                pStm.setInt(1, id);
                pStm.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.error("error while deleting genre nb "+id);
            e.printStackTrace();
        }
        if (genre != null) {
        	LOGGER.info("genre "+genre.getNom()+" has been delete");
        } else {
        	LOGGER.error("Ce genre est inexistant");
        }
        return genre;
    }

    public Genre addGenre(String name) throws GenreAlreadyExistingException {
        LOGGER.debug("Trying to add genre "+name);
        Genre res = new Genre(name);
        try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
        	try (PreparedStatement pStm = co.prepareStatement("INSERT INTO Genre (nomGenre) VALUES (?);", Statement.RETURN_GENERATED_KEYS)) {
        		pStm.setString(1, name);
                pStm.executeUpdate();
                ResultSet ids = pStm.getGeneratedKeys();
                if(ids.next()) {
                	res.setId(ids.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("error while adding genre nb "+name);
            e.printStackTrace();
        }
        LOGGER.info("added genre "+name+" at id "+res.getId());
        return res;
    }
}
