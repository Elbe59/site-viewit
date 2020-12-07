package dao.impl;

import dao.UtilisateurDao;
import entity.Utilisateur;
import entity.UtilisateurDto;
import exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDaoImpl implements UtilisateurDao {

    static final Logger LOGGER = LogManager.getLogger();

    public List<Utilisateur> listUser() {
        LOGGER.debug("Getting list of users");
        List<Utilisateur> listUser = new ArrayList<Utilisateur>();
        try(Connection co = DataSourceProvider.getDataSource().getConnection()){
            try(Statement stm = co.createStatement()) {
                try(ResultSet rs = stm.executeQuery("SELECT * FROM utilisateur ORDER BY nomUtilisateur;")) {
                    while(rs.next()) {
                        listUser.add(new Utilisateur(
                                rs.getInt("idUtilisateur"),
                                rs.getString("prenomUtilisateur"),
                                rs.getString("nomUtilisateur"),
                                rs.getString("email"),
                                rs.getString("mdp"),
                                rs.getString("mdpHash"),
                                rs.getInt("admin")==1?true:false));
                    }
                }
            }
        } catch (SQLException  e) {
            LOGGER.error("error while getting list of users");
            e.printStackTrace();
        }
        LOGGER.debug("succesfully returned list of users");
        return listUser;
    }

    public Utilisateur getUser(Integer id) throws UserNotFoundException{
        LOGGER.debug("Trying to get user nb "+id);
        Utilisateur user = null;
        try(Connection co = DataSourceProvider.getDataSource().getConnection()){
            try(PreparedStatement pStm = co.prepareStatement("SELECT * FROM utilisateur WHERE idUtilisateur = ?;")) {
                pStm.setInt(1, id);
                try(ResultSet rs = pStm.executeQuery()) {
                    while(rs.next()) {
                        user = new Utilisateur(
                                rs.getInt("idUtilisateur"),
                                rs.getString("prenomUtilisateur"),
                                rs.getString("nomUtilisateur"),
                                rs.getString("email"),
                                rs.getString("mdp"),
                                rs.getString("mdpHash"),
                                rs.getInt("admin")==1?true:false);
                    }
                    if (user==null)
                        throw new UserNotFoundException("Utilisateur non trouvé");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("error while trying to get user nb "+id);
            e.printStackTrace();
        }
        LOGGER.debug("Returned user "+user.getEmail()+", for id="+id);
        return user;
    }

    public Utilisateur addUser(Utilisateur user) {
        LOGGER.debug("Trying to add user "+user.getEmail());
        try(Connection co = DataSourceProvider.getDataSource().getConnection()) {
            try (PreparedStatement pStm = co.prepareStatement("INSERT INTO utilisateur ( prenomUtilisateur, nomUtilisateur, email, mdp,mdpHash, admin) VALUES (?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS)) {
                pStm.setString(1, user.getPrenom());
                pStm.setString(2, user.getNom());
                pStm.setString(3, user.getEmail());
                pStm.setString(4, user.getMdp());
                pStm.setString(5, user.getMdpHash());
                pStm.setInt(6, user.isAdmin()?1:0);
                pStm.executeUpdate();
                ResultSet ids = pStm.getGeneratedKeys();
                if(ids.next()) {
                	user.setId(ids.getInt(1));
                }
            }
        }catch (SQLException e) {
            LOGGER.error("error adding user  "+user.getEmail());
            e.printStackTrace();
        }
        LOGGER.info("Succesfully added user "+user.getEmail());
        return user;
    }

    public Utilisateur deleteUser(Integer id) throws UserNotFoundException, SQLException {
        LOGGER.debug("Trying to delete user nb "+id);
        Utilisateur user = getUser(id);
        try(Connection co = DataSourceProvider.getDataSource().getConnection()) {
        	try (PreparedStatement pStm = co.prepareStatement("DELETE FROM utilisateur WHERE idUtilisateur = ?;")) {
        		pStm.setInt(1, id);
                pStm.executeUpdate();
            } catch (SQLException e) {
                LOGGER.error("error deleting user  "+id);
                e.printStackTrace();
            }
        }
        LOGGER.info("Succesfully deleted user  "+ user.getEmail());
        return user;
    }

    public Utilisateur changeRoleUser(String action,Integer id) throws SQLException, UserAlreadyAdminException, UserAlreadyDownException, UserNotFoundException {
        LOGGER.debug("Trying to change role of user "+id+", action is: "+action);
        Utilisateur user = null;
        String sqlQuery;
        try{
            user = getUser(id);
            try(Connection co = DataSourceProvider.getDataSource().getConnection()) {

                if(action.contentEquals("up")){
                    sqlQuery="UPDATE `utilisateur` SET `admin` = '1' WHERE `utilisateur`.`idUtilisateur`=?";
                    if (user.isAdmin())
                        throw new UserAlreadyAdminException("L'utilisateur est déjà admin.");
                }
                else {
                    sqlQuery="UPDATE `utilisateur` SET `admin` = '0' WHERE `utilisateur`.`idUtilisateur`=?";
                    if (!user.isAdmin())
                        throw new UserAlreadyDownException("Utilisateur est déjà non-admin");
                }
                try (PreparedStatement pStm = co.prepareStatement(sqlQuery)) {
                    pStm.setInt(1, id);
                    pStm.executeUpdate();
                } catch (SQLException e) {
                    LOGGER.error("error changing role of user  "+id);
                    e.printStackTrace();
                }
            }
            user = getUser(id);
        }catch(UserNotFoundException e){
            LOGGER.error("error changing role of user  "+id+", not found");
        }
        LOGGER.info("Changed role of user "+user.getEmail());
        return user;
    }

    public Utilisateur modifyUser(Utilisateur user) throws SQLException, UserAlreadyExistingException {
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
            String sqlQuery = "UPDATE `utilisateur` SET email = ?, prenomUtilisateur = ?, nomUtilisateur = ?, mdpHash = ? WHERE idUtilisateur = ?";
            try (PreparedStatement pStm = connection.prepareStatement(sqlQuery)) {
                pStm.setString(1, user.getEmail());
                pStm.setString(2, user.getPrenom());
                pStm.setString(3, user.getNom());
                pStm.setString(4, user.getMdpHash());
                pStm.setInt(5, user.getId());
                pStm.executeUpdate();
            }
        }catch(SQLException e) {
        	LOGGER.error("Error modify user nb "+user.getId());
        	e.printStackTrace();
        }
        LOGGER.info("Succesfully modified user nb "+user.getId());
        return user;
    }

    public Utilisateur getUserByEmail(String email) throws UserNotFoundException{
        LOGGER.debug("Getting user "+email);
        Utilisateur user = null;
        try(Connection co = DataSourceProvider.getDataSource().getConnection()){
            String sqlQuery="SELECT * FROM utilisateur WHERE email=?";
            try(PreparedStatement pStm = co.prepareStatement(sqlQuery)) {
                pStm.setString(1,email);
                try(ResultSet rs = pStm.executeQuery()) {
                    while(rs.next()) {
                        user = new Utilisateur(
                                rs.getInt("idUtilisateur"),
                                rs.getString("prenomUtilisateur"),
                                rs.getString("nomUtilisateur"),
                                rs.getString("email"),
                                rs.getString("mdp"),
                                rs.getString("mdpHash"),
                                rs.getInt("admin")==1?true:false);
                    }
                    if (user==null)
                        throw new UserNotFoundException("Utilisateur non trouvé d'après le mail " + email);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting user "+email);
            e.printStackTrace();
        }
        LOGGER.debug("Found user "+email+", at id="+user.getId());
        return user;
    }
}
