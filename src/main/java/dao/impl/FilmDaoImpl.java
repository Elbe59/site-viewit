package dao.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import dao.FilmDao;
import dao.UtilisateurDao;
import entity.Film;
import entity.FilmDto;
import entity.Genre;
import entity.Utilisateur;
import exception.*;

public class FilmDaoImpl implements FilmDao {

	private UtilisateurDao userDao = new UtilisateurDaoImpl();

	@Override
	public List<Film> listFilms() {
		List<Film> listOfFilms = new ArrayList<Film>();
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(Statement stm = co.createStatement()) {
				try(ResultSet rs = stm.executeQuery("SELECT * FROM film JOIN genre ON film.idGenre = genre.idGenre ORDER BY titreFilm;")) {
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
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		
		return listOfFilms;
	}

	@Override
	public List<Film> listFilms(String param) {
		System.out.println(param);
		List<Film> listOfFilms = new ArrayList<Film>();
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			if (param == "Popularite") {
				for (int i=0;i<listFilmsDto(0).size();i++) {
					try {
						listOfFilms.add(getFilm(listFilmsDto(0).get(i).getId()));
						System.out.println(getFilm(listFilmsDto(0).get(i).getId()));
					} catch (FilmNotFoundException e) {
						e.printStackTrace();
					}	
				}
			} else {
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
			try(PreparedStatement pStm = co.prepareStatement("SELECT * FROM film JOIN genre ON film.idGenre = genre.idGenre AND film.idFilm = ?;")) {
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
						throw new FilmNotFoundException("Le film que vous essayez de récupérer n'a pas été trouvé.");
				}
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return film;
	}

	@Override
	public List<Film> listFavorisFilm(Integer idUtilisateur, String trie) throws UserNotFoundException {
		List<Film> listOfFilms = new ArrayList<Film>();
		Film film = null;
		userDao.getUser(idUtilisateur);
		//boolean notFound = true;
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(PreparedStatement pStm = co.prepareStatement("SELECT preferer.idfilm, titreFilm, dateSortie FROM preferer JOIN film ON film.idFilm = preferer.idFilm WHERE idUtilisateur=? AND favoris = 1 ORDER BY ?;")) {
				pStm.setInt(1, idUtilisateur);
				pStm.setString(2, trie);
				try(ResultSet rs = pStm.executeQuery()) {
					while(rs.next()) {
						//notFound = false;
						Integer idFilm = rs.getInt("idFilm");
						rs.getString("titreFilm");
						rs.getDate("dateSortie").toLocalDate();
						film = getFilm(idFilm);
						listOfFilms.add(film);
					}
				}
			}
		} catch (SQLException | FilmNotFoundException e) {
			e.printStackTrace();
		}
		return listOfFilms;
	}
	
	@Override
	public Film addFilm(Film film) throws FilmAlreadyExistingException, FilmNotFoundException {
		List<Film> films = listFilms();
		String sqlQuerry = "INSERT INTO film (titreFilm, resumeFilm, dateSortie, dureeFilm, realisateur, acteur, imgFilm, urlBA, idGenre, valide) VALUES (?,?,?,?,?,?,?,?,?,?);";
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
				throw new FilmAlreadyExistingException("Le film que vous essayez d'ajouter existe déjà.");
			} else {
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
					pStm.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
				throw new FilmAlreadyActiveException("Le film que vous essayez d'activer est déjà activé.");
			String sqlQuery = "UPDATE `film` SET `valide` = '1' WHERE `film`.`idFilm` = ?";
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			statement.setInt(1, id);
			int nb = statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		film=getFilm(id);
		return film;
	}

	public Film desactiveFilm(Integer id) throws FilmNotFoundException, FilmAlreadyDesactiveException {
		Film film = null;
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			film=getFilm(id);
			if(film.getValide()==0)
				throw new FilmAlreadyDesactiveException("Le film que vous essayez de désactiver est déjà désactivé.");
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

	public int getSqlIdFilm(Film film) throws FilmNotFoundException {
		Integer id = null;
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(PreparedStatement pStm = co.prepareStatement("SELECT idFilm FROM film WHERE titreFilm =? AND dateSortie =? AND resumeFilm =? AND acteur = ? AND realisateur = ? AND urlBA = ? ")) {
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
			throw new FilmNotFoundException("Aucun film ne correspond à l'id que vous essayez de récupérer.");
		else
			return id;
	}

	public List<FilmDto> listFilmsDto(Integer idUtilisateur) {
		List<Film> listOfFilms = listFilms();
		List<FilmDto> listOfFilmsCo = new ArrayList<FilmDto>();

		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			String sqlQuery = "SELECT film.idFilm,film.titreFilm,preferer.idUtilisateur,preferer.favoris,preferer.liker FROM film JOIN preferer ON film.idFilm = preferer.idFilm WHERE preferer.idUtilisateur=?;";
			try(PreparedStatement pStm = co.prepareStatement(sqlQuery)) {
				pStm.setInt(1, idUtilisateur);
				for (int i=0;i<listOfFilms.size();i++) {
					Integer pourcentage = getPourcentageFilm(listOfFilms.get(i).getId());
					FilmDto filmDto = new FilmDto(
							listOfFilms.get(i).getId(),
							listOfFilms.get(i).getTitre(),
							"aucun",
							false,
							pourcentage);
					try(ResultSet rs = pStm.executeQuery()) {
						while(rs.next()) {
							if (rs.getInt("idFilm") == listOfFilms.get(i).getId()) {
								if (rs.getInt("favoris") == 1) {
									filmDto.setFavori(true);
								}
								if (rs.getInt("liker") == 1) {
									filmDto.setAvis("like");
								} else if (rs.getInt("liker") == -1) {
									filmDto.setAvis("dislike");
								}
							}
						}
					}
					listOfFilmsCo.add(filmDto);
				}
			} catch (FilmNotFoundException e) {
				e.printStackTrace();
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return trierListFilms(listOfFilmsCo);
	}

	public List<FilmDto> trierListFilms (List<FilmDto> listFilmsDto) {
		List<FilmDto> listFilmsDtoTri = new ArrayList<FilmDto>();
		List<FilmDto> listFilmsDtoRec = new ArrayList<FilmDto>(listFilmsDto);
		while (listFilmsDtoTri.size() != listFilmsDto.size()) {
			FilmDto maximum = listFilmsDtoRec.get(0);
			listFilmsDtoRec.remove(0);
			for (int i=0;i<listFilmsDtoRec.size();i++) {
				if (maximum.getPourcentage() < listFilmsDtoRec.get(i).getPourcentage()) {
					listFilmsDtoRec.add(0,maximum);
					maximum = listFilmsDtoRec.get(i+1);
					listFilmsDtoRec.remove(i+1);
				}
			}
			listFilmsDtoTri.add(maximum);
		}
		return listFilmsDtoTri;
	}

	public Film addFavori (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException {
		Film film = getFilm(idFilm);
		System.out.println("ds add favoris");
		boolean verification = false;
		userDao.getUser(idUtilisateur);
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(PreparedStatement pstm = co.prepareStatement("SELECT * FROM preferer WHERE preferer.idFilm=? AND preferer.idUtilisateur=?;")) {
				pstm.setInt(1, idFilm);
				pstm.setInt(2, idUtilisateur);
				try(ResultSet rs = pstm.executeQuery()) {
					if(rs.next()) {
						verification = true;
						System.out.println("verif = true");
					}
				}
			} 
			co.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if(verification == true) {
			try(Connection co = DataSourceProvider.getDataSource().getConnection()){
				try(PreparedStatement pstm = co.prepareStatement("UPDATE preferer SET favoris=1 WHERE preferer.idFilm=? AND preferer.idUtilisateur=?;")) {
					pstm.setInt(1, idFilm);
					pstm.setInt(2, idUtilisateur);
					pstm.executeUpdate();
				}
				co.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			try(Connection co = DataSourceProvider.getDataSource().getConnection()){
				try(PreparedStatement pstm = co.prepareStatement("INSERT INTO preferer (idFilm,idUtilisateur,liker,favoris) VALUES (?,?,0,1);")) {
					pstm.setInt(1, idFilm);
					pstm.setInt(2, idUtilisateur);
					pstm.executeUpdate();
				}
				co.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return film;
	}

	public Film suppFavori (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException {
		Film film = null;
		userDao.getUser(idUtilisateur);
		try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
			film=getFilm(idFilm);
			String sqlQuery = "UPDATE preferer SET favoris=0 WHERE preferer.idFilm=? AND preferer.idUtilisateur=?";
			try(PreparedStatement pstm = co.prepareStatement(sqlQuery)){
				pstm.setInt(1, idFilm);
				pstm.setInt(2, idUtilisateur);
				pstm.executeUpdate();
				pstm.close();
			}
			co.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}
	
	public Film addLike (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException {
		Film film = getFilm(idFilm);
		System.out.println("ds add like");
		boolean verification = false;
		userDao.getUser(idUtilisateur);
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(PreparedStatement pstm = co.prepareStatement("SELECT * FROM preferer WHERE preferer.idFilm=? AND preferer.idUtilisateur=?;")) {
				pstm.setInt(1, idFilm);
				pstm.setInt(2, idUtilisateur);
				try(ResultSet rs = pstm.executeQuery()) {
					if(rs.next()) {
						verification = true;
						System.out.println("verif ) true");
					}
				}
			} 
			co.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if(verification == true) {
			try(Connection co = DataSourceProvider.getDataSource().getConnection()){
				try(PreparedStatement pstm = co.prepareStatement("UPDATE preferer SET liker=1 WHERE preferer.idFilm=? AND preferer.idUtilisateur=?;")) {
					pstm.setInt(1, idFilm);
					pstm.setInt(2, idUtilisateur);
					pstm.executeUpdate();
				}
				co.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			try(Connection co = DataSourceProvider.getDataSource().getConnection()){
				try(PreparedStatement pstm = co.prepareStatement("INSERT INTO preferer (idFilm,idUtilisateur,liker,favoris) VALUES (?,?,1,0);")) {
					pstm.setInt(1, idFilm);
					pstm.setInt(2, idUtilisateur);
					pstm.executeUpdate();
				}
				co.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return film;
	}
	
	public Film addDislike (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException {
		Film film = getFilm(idFilm);
		System.out.println("ds add like");
		boolean verification = false;
		userDao.getUser(idUtilisateur);
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(PreparedStatement pstm = co.prepareStatement("SELECT * FROM preferer WHERE preferer.idFilm=? AND preferer.idUtilisateur=?;")) {
				pstm.setInt(1, idFilm);
				pstm.setInt(2, idUtilisateur);
				try(ResultSet rs = pstm.executeQuery()) {
					if(rs.next()) {
						verification = true;
						System.out.println("verif ) true");
					}
				}
			} 
			co.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if(verification == true) {
			try(Connection co = DataSourceProvider.getDataSource().getConnection()){
				try(PreparedStatement pstm = co.prepareStatement("UPDATE preferer SET liker=-1 WHERE preferer.idFilm=? AND preferer.idUtilisateur=?;")) {
					pstm.setInt(1, idFilm);
					pstm.setInt(2, idUtilisateur);
					pstm.executeUpdate();
				}
				co.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			try(Connection co = DataSourceProvider.getDataSource().getConnection()){
				try(PreparedStatement pstm = co.prepareStatement("INSERT INTO preferer (idFilm,idUtilisateur,liker,favoris) VALUES (?,?,-1,0);")) {
					pstm.setInt(1, idFilm);
					pstm.setInt(2, idUtilisateur);
					pstm.executeUpdate();
				}
				co.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return film;
	}
	
	public Film removeAvis (Integer idFilm, Integer idUtilisateur) throws FilmNotFoundException, UserNotFoundException {
		Film film = null;
		userDao.getUser(idUtilisateur);
		try (Connection co = DataSourceProvider.getDataSource().getConnection()) {
			film=getFilm(idFilm);
			String sqlQuery = "UPDATE preferer SET liker=0 WHERE preferer.idFilm=? AND preferer.idUtilisateur=?";
			try(PreparedStatement pstm = co.prepareStatement(sqlQuery)){
				pstm.setInt(1, idFilm);
				pstm.setInt(2, idUtilisateur);
				pstm.executeUpdate();
				pstm.close();
			}
			co.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}
	
	public Integer getPourcentageFilm (Integer id) throws FilmNotFoundException {
		Integer pourcentage = 0;
		try(Connection co = DataSourceProvider.getDataSource().getConnection()){
			try(PreparedStatement pStm = co.prepareStatement("SELECT"
					+ "	(SELECT idFilm FROM preferer WHERE idFilm=? GROUP BY idFilm) AS idFilm,"
					+ "	IFNULL((SELECT count(liker) FROM preferer WHERE idFilm=? AND liker=1 GROUP BY idFilm),0) AS likes,"
					+ " IFNULL((SELECT count(liker) FROM preferer WHERE idFilm=? AND liker!=0 GROUP BY idFilm),0) AS total;")) {
				pStm.setInt(1, id);
				pStm.setInt(2, id);
				pStm.setInt(3, id);
				try(ResultSet rs = pStm.executeQuery()) {
					while (rs.next()) {
						if (rs.getInt("total") != 0) {
							pourcentage = (int) ((rs.getFloat("likes")/rs.getFloat("total"))*100);
						} else {
							pourcentage = 0;
						}	
					}	
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pourcentage;
	}
}
