package dao;

import entity.Genre;
import exception.GenreNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface GenreDao {
    public List<Genre> listGenre();
    public Genre getGenre(Integer id);
    public Genre deleteGenre(Integer id) throws GenreNotFoundException, SQLException;
}
