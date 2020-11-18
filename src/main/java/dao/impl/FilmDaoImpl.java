package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.FilmDao;
import entity.Film;
import entity.Genre;

public class FilmDaoImpl implements FilmDao {

	@Override
	public List<Film> listFilms() {
		List<Film> listOfFilms = new ArrayList<Film>();
		
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(Statement stm = co.createStatement()) {
				try(ResultSet rs = stm.executeQuery("SELECT * FROM FILM JOIN GENRE ON film.idGenre = genre.idGenre ORDER BY titreFilm;")) {
					while(rs.next()) {
						listOfFilms.add(new Film(
								rs.getInt("idFilm"),
								rs.getString("titreFilm"),
								rs.getString("resumeFilm"),
								rs.getDate("dateSortie").toLocalDate(),
								rs.getInt("dureeFilm"),
								rs.getString("realisateur"),
								rs.getString("acteur"),
								rs.getString("imgFilm"),
								rs.getString("urlBA"),
								new Genre(rs.getInt("idGenre"),rs.getString("nomGenre")),
								rs.getInt("valide")));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listOfFilms;
	}


	public List<Film> listFilms(String colonne) {
		List<Film> listOfFilms = new ArrayList<Film>();
		String param = "";
		if(colonne.equals("alpha"))
			param = "titreFilm";
		else if (colonne.equals("recent"))
			param = "dateSortie DESC";
		else if (colonne.equals("ancien"))
			param = "dateSortie";
		else if (colonne.equals("genre"))
			param = "nomGenre";
		else if (colonne.contentEquals("valide"))
			param = "valide";
		else {
			param = "valide";//popularité
		}
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			String sqlQuery="SELECT * FROM FILM JOIN GENRE ON film.idGenre = genre.idGenre ORDER BY "+param;
			try(PreparedStatement pStm = co.prepareStatement(sqlQuery)) {
				//pStm.setString(1, param);
				try(ResultSet rs = pStm.executeQuery()) {
					while(rs.next()) {
						listOfFilms.add(new Film(
								rs.getInt("idFilm"),
								rs.getString("titreFilm"),
								rs.getString("resumeFilm"),
								rs.getDate("dateSortie").toLocalDate(),
								rs.getInt("dureeFilm"),
								rs.getString("realisateur"),
								rs.getString("acteur"),
								rs.getString("imgFilm"),
								rs.getString("urlBA"),
								new Genre(rs.getInt("idGenre"),rs.getString("nomGenre")),
								rs.getInt("valide")));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listOfFilms;
	}

	@Override
	public Film getFilm(Integer id) {
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(PreparedStatement pStm = co.prepareStatement("SELECT * FROM FILM JOIN GENRE ON film.idGenre = genre.idGenre AND film.idFilm = ?;")) {
				pStm.setInt(1, id);
				try(ResultSet rs = pStm.executeQuery()) {
					while(rs.next()) {
						return new Film(
								rs.getInt("idFilm"),
								rs.getString("titreFilm"),
								rs.getString("resumeFilm"),
								rs.getDate("dateSortie").toLocalDate(),
								rs.getInt("dureeFilm"),
								rs.getString("realisateur"),
								rs.getString("acteur"),
								rs.getString("imgFilm"),
								rs.getString("urlBA"),
								new Genre(rs.getInt("idGenre"),rs.getString("nomGenre")),
								rs.getInt("valide"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Film addFilm(Film film) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Film deleteFilm(Integer id) {
		Film film = null;
		try(Connection connection=DataSourceProvider.getDataSource().getConnection()){
			String sqlQuery="DELETE FROM `film` WHERE idFilm = ?";
			film = getFilm(id);
			//String sqlQuery2="UPDATE film SET idFilm = ? WHERE idFilm = ?";
			PreparedStatement statement= connection.prepareStatement(sqlQuery);
			statement.setInt(1,id);
			int nb= statement.executeUpdate();
			statement.close();
			/*for(int i=id+1;i< listFilms().size()+2;i++){
				PreparedStatement statement1= connection.prepareStatement(sqlQuery2);
				statement1.setInt(1,(i-1));
				statement1.setInt(2,i);
				int nb1= statement1.executeUpdate();
				System.out.println("J'ai décalé l'indice"+i);
				statement1.close();
			}*/
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return film;
	}

}
