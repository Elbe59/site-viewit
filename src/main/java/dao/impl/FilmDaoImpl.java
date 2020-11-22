package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dao.FilmDao;
import entity.Film;
import entity.Genre;
import exception.*;

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

	@Override
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
	public Film getFilm(Integer id) throws FilmNotFoundException{
		Film film = null;
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(PreparedStatement pStm = co.prepareStatement("SELECT * FROM FILM JOIN GENRE ON film.idGenre = genre.idGenre AND film.idFilm = ?;")) {
				pStm.setInt(1, id);
				try(ResultSet rs = pStm.executeQuery()) {
					while(rs.next()) {
						film = new Film(
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
					if(film == null)
						throw new FilmNotFoundException();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	@Override
	public List<Film> getFilmByUtilisateur(Integer idUtilisateur) throws FilmNotFoundException {
		List<Film> listOfFilms = new ArrayList<Film>();
		Film film = null;
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(PreparedStatement pStm = co.prepareStatement("SELECT idfilm FROM PREFERER WHERE idUtilisateur=? AND favoris = 1;")) {
				pStm.setInt(1, idUtilisateur);
				try(ResultSet rs = pStm.executeQuery()) {
					while(rs.next()) {
						Integer idFilm = rs.getInt("idFilm");
						film = getFilm(idFilm);
						listOfFilms.add(film);
					}
					if(film == null)
						throw new FilmNotFoundException();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listOfFilms;
	}
	
	@Override
	public Film addFilm(Film film) throws FilmAlreadyExistingException{
		/*
		List<Film> films = listFilms();
		boolean existing  = false;
		for (int i = 0; i<films.size(); i++)
		{
			if (films.get(i).getTitre().equals(film.getTitre()))
			{
				if(films.get(i).getDateSortie()==film.getDateSortie())
					existing = true;
			}
		}
		try(Connection co = DataSourceProvider.getDataSource().getConnection()) {
			if (existing)
				throw new FilmAlreadyExistingException();
			try (PreparedStatement pStm = co.prepareStatement("INSERT INTO FILM (idFilm, titreFilm, resumeFilm, dateSortie, dureeFilm, realisateur, acteur, imgFilm, urlBA, idGenre, valide) VALUES (?,?,?,?,?,?,?,?,?,?,?);")) {
				pStm.setInt(1, film.getId());
				pStm.setString(2, film.getTitre());
				pStm.setString(3, film.getResume());
				pStm.setTimestamp(4, Timestamp.valueOf(film.getDateSortie().atStartOfDay()));
				pStm.setInt(5, film.getDuree());
				pStm.setString(6, film.getRealisateur());
				pStm.setString(7, film.getActeur());
				pStm.setString(8, film.getImageName());
				pStm.setString(9, film.getUrlBA());//mettre l'image ?
				pStm.setInt(10, film.getGenre());//besoin d'obtenir idgenre à partir de genre
				pStm.setInt(11, film.getValide());
				pStm.executeUpdate();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}*/
		return film;
	}

	public Film activeFilm(Integer id) throws FilmNotFoundException, FilmAlreadyActiveException {
		Film film = null;
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			film=getFilm(id);
			if (film.getValide()==1)
				throw new FilmAlreadyActiveException();
			String sqlQuery = "UPDATE `film` SET `valide` = '1' WHERE `film`.`idFilm` = ?";
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			statement.setInt(1, id);
			int nb = statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		try{
			film=getFilm(id);
		}catch (FilmNotFoundException e){}
		return film;
	}

	public Film desactiveFilm(Integer id) throws FilmNotFoundException, FilmAlreadyDesactiveException {
		Film film = null;
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			film=getFilm(id);
			if(film.getValide()==0)
				throw new FilmAlreadyDesactiveException();
			String sqlQuery = "UPDATE `film` SET `valide` = '0' WHERE `film`.`idFilm` = ?";
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			statement.setInt(1, id);
			int nb = statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		try{
			film=getFilm(id);
		}catch (FilmNotFoundException e){}
		return film;
	}

	@Override
	public Film deleteFilm(Integer id) throws FilmNotFoundException{
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

}
