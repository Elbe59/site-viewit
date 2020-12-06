package dao;

import entity.Genre;
import entity.GenreDto;
import exception.GenreAlreadyExistingException;
import exception.GenreNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface GenreDao {
    public List<Genre> listGenre();
    public List<GenreDto> listGenreDto(List<Genre> genreList);
    public Genre getGenre(Integer id) throws GenreNotFoundException;
    public Genre deleteGenre(Integer id) throws GenreNotFoundException, SQLException;
    public Genre addGenre(String name) throws GenreAlreadyExistingException;
}
