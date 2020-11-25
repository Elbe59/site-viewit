package dao.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
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
						Blob blob = rs.getBlob("image");
						String base64Image=transformBlobToBase64Image(blob);
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
								rs.getInt("valide"),
								base64Image));;
					}
				}
			}
		} catch (SQLException | IOException e) {
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
						Blob blob = rs.getBlob("image");
						String base64Image=transformBlobToBase64Image(blob);

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
								rs.getInt("valide"),
								base64Image));
					}
				}
			}
		} catch (SQLException | IOException e) {
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
						Blob blob = rs.getBlob("image");
						String base64Image=transformBlobToBase64Image(blob);

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
								rs.getInt("valide"),
								base64Image);
					}
					if(film == null)
						throw new FilmNotFoundException();
				}
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return film;
	}

	@Override
	public List<Film> getFilmByUtilisateur(Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException {
		List<Film> listOfFilms = new ArrayList<Film>();
		Film film = null;
		boolean notFound = true;
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(PreparedStatement pStm = co.prepareStatement("SELECT idfilm FROM PREFERER WHERE idUtilisateur=? AND favoris = 1;")) {
				pStm.setInt(1, idUtilisateur);
				try(ResultSet rs = pStm.executeQuery()) {
					while(rs.next()) {
						notFound = false;
						Integer idFilm = rs.getInt("idFilm");
						film = getFilm(idFilm);
						listOfFilms.add(film);
					}
					if (notFound)
						throw new UserNotFoundException();
				}
			}
		} catch ( SQLException e) {
			e.printStackTrace();
		}
		return listOfFilms;
	}
	
	@Override
	public Film addFilm(Film film,InputStream image) throws FilmAlreadyExistingException, FilmNotFoundException {
		List<Film> films = listFilms();
		String sqlQuerry = "INSERT INTO FILM (titreFilm, resumeFilm, dateSortie, dureeFilm, realisateur, acteur, imgFilm, urlBA, idGenre, valide, image) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
		boolean existing  = false;
		for (int i = 0; i<films.size(); i++)
		{
			//System.out.println("Compare "+films.get(i).getTitre()+" avec "+film.getTitre());
			if (films.get(i).getTitre().equals(film.getTitre()))
			{
				//System.out.println("Titres égales");
				//System.out.println("compare "+films.get(i).getDateSortie()+" avec "+film.getDateSortie());
				if(films.get(i).getDateSortie().equals(film.getDateSortie()))
					//System.out.println("dates égales");
					existing = true;
			}
		}
		try(Connection co = DataSourceProvider.getDataSource().getConnection()) {
			if (existing) {
				throw new FilmAlreadyExistingException();
			} else {
				if(image == null)
					sqlQuerry = "INSERT INTO FILM (titreFilm, resumeFilm, dateSortie, dureeFilm, realisateur, acteur, imgFilm, urlBA, idGenre, valide, image) VALUES (?,?,?,?,?,?,?,?,?,?,'');";
				try (PreparedStatement pStm = co.prepareStatement(sqlQuerry)) {
					pStm.setString(1, film.getTitre());
					pStm.setString(2, film.getResume());
					pStm.setTimestamp(3, Timestamp.valueOf(film.getDateSortie().atStartOfDay()));
					pStm.setInt(4, film.getDuree());
					pStm.setString(5, film.getRealisateur());
					pStm.setString(6, film.getActeur());
					pStm.setString(7, film.getImageName());
					pStm.setString(8, film.getUrlBA());
					pStm.setInt(9, film.getGenre().getId());
					pStm.setInt(10, film.getValide());
					if (image != null)
						pStm.setBlob(11, image);
					pStm.executeUpdate();
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		film.setId(getSqlIdFilm(film));
		//System.out.println("id: "+film.getId());
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

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	public String transformBlobToBase64Image(Blob blob) throws IOException, SQLException {
		InputStream inputStream = blob.getBinaryStream();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		byte[] imageBytes = outputStream.toByteArray();
		String base64Image = Base64.getEncoder().encodeToString(imageBytes);
		inputStream.close();
		outputStream.close();
		return base64Image;
	}

	public int getSqlIdFilm(Film film) throws FilmNotFoundException {
		Integer id = null;
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(PreparedStatement pStm = co.prepareStatement("SELECT idFilm FROM FILM WHERE titreFilm =? AND dateSortie =? AND resumeFilm =? AND acteur = ? AND realisateur = ? AND urlBA = ? ")) {
				pStm.setString(1, film.getTitre());
				pStm.setTimestamp(2, Timestamp.valueOf(film.getDateSortie().atStartOfDay()));
				pStm.setString(3, film.getResume());
				pStm.setString(4, film.getActeur());
				pStm.setString(5, film.getRealisateur());
				pStm.setString(6, film.getUrlBA());
				try(ResultSet rs = pStm.executeQuery()) {
					while(rs.next()) {
						id = rs.getInt("idFilm");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (id == null)
			throw new FilmNotFoundException();
		else
			return id;
	}
}
