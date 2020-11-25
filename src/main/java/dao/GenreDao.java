package dao;

import entity.Genre;
import exception.GenreAlreadyExistingException;
import exception.GenreNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface GenreDao {
    public List<Genre> listGenre();
    public Genre getGenre(Integer id) throws GenreNotFoundException;
    public Genre deleteGenre(Integer id) throws GenreNotFoundException, SQLException;

    public void addGenre(String name) throws GenreAlreadyExistingException;
}
