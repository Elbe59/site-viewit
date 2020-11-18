package dao.impl;

import dao.GenreDao;
import entity.Film;
import entity.Genre;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GenreDaoImpl implements GenreDao {
    public List<Genre> listGenre() {
        List<Genre> listOfGenres = new ArrayList<Genre>();
        try(Connection co = DataSourceProvider.getDataSource().getConnection()){
            try(Statement stm = co.createStatement()) {
                try(ResultSet rs = stm.executeQuery("SELECT * FROM GENRE ORDER BY idGenre;")) {
                    while(rs.next()) {
                        listOfGenres.add(new Genre(rs.getInt("idGenre"),rs.getString("nomGenre")));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfGenres;
    }
}
