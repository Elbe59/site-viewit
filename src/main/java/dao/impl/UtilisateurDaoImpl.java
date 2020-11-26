package dao.impl;

import dao.UtilisateurDao;
import entity.Utilisateur;
import exception.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDaoImpl implements UtilisateurDao {

    @Override
    public List<Utilisateur> listUser() {
        List<Utilisateur> listUser = new ArrayList<Utilisateur>();

        try(Connection co = DataSourceProvider.getDataSource().getConnection()){
            try(Statement stm = co.createStatement()) {
                try(ResultSet rs = stm.executeQuery("SELECT * FROM UTILISATEUR ORDER BY idUtilisateur;")) {
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
            e.printStackTrace();
        }
        return listUser;
    }

    @Override
    public Utilisateur getUser(Integer id) throws UserNotFoundException{
        Utilisateur user = null;
        try(Connection co = DataSourceProvider.getDataSource().getConnection()){
            try(PreparedStatement pStm = co.prepareStatement("SELECT * FROM UTILISATEUR WHERE idUtilisateur = ?;")) {
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
                        throw new UserNotFoundException();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Utilisateur addUser(Utilisateur user) throws UserAlreadyExistingException {
        List<Utilisateur> users = listUser();
        boolean existing  = false;
        for (int i = 0; i<users.size(); i++)
        {
            if (users.get(i).getEmail().equals(user.getEmail()))
                existing = true;
        }
        try(Connection co = DataSourceProvider.getDataSource().getConnection()) {
            if (existing)
                throw new UserAlreadyExistingException();
            try (PreparedStatement pStm = co.prepareStatement("INSERT INTO UTILISATEUR ( prenomUtilisateur, nomUtilisateur, email, mdp,mdpHash, admin) VALUES (?,?,?,?,?,?);")) {
                pStm.setString(1, user.getPrenom());
                pStm.setString(2, user.getNom());
                pStm.setString(3, user.getEmail());
                pStm.setString(4, user.getMdp());
                pStm.setString(5, user.getMdpHash());
                pStm.setInt(6, user.isAdmin()?1:0);
                pStm.executeUpdate();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Utilisateur deleteUser(Integer id) throws UserNotFoundException, SQLException {
        Utilisateur user = null;
        try{
            user = getUser(id);
            try(Connection co = DataSourceProvider.getDataSource().getConnection()) {
                try (PreparedStatement pStm = co.prepareStatement("DELETE FROM UTILISATEUR WHERE idUtilisateur = ?;")) {
                    pStm.setInt(1, id);
                    pStm.executeUpdate();
                } catch (SQLException e) { }
            }
        }catch(UserNotFoundException e){
        }
        return user;
    }

    public Utilisateur changeRoleUser(String action,Integer id) throws SQLException, UserAlreadyAdminException, UserAlreadyDownException {
        Utilisateur user = null;
        String sqlQuery;
        try{
            user = getUser(id);
            try(Connection co = DataSourceProvider.getDataSource().getConnection()) {

                if(action.contentEquals("up")){
                    sqlQuery="UPDATE `utilisateur` SET `admin` = '1' WHERE `utilisateur`.`idUtilisateur`=?";
                    if (user.isAdmin())
                        throw new UserAlreadyAdminException();
                }
                else {
                    sqlQuery="UPDATE `utilisateur` SET `admin` = '0' WHERE `utilisateur`.`idUtilisateur`=?";
                    if (!user.isAdmin())
                        throw new UserAlreadyDownException();
                }
                try (PreparedStatement pStm = co.prepareStatement(sqlQuery)) {
                    pStm.setInt(1, id);
                    pStm.executeUpdate();
                } catch (SQLException e) { }
            }
            user = getUser(id);
        }catch(UserNotFoundException e){
        }
        return user;
    }

    @Override
    public Utilisateur getUserByEmail(String email) throws UserNotFoundException{
        Utilisateur user = null;
        try(Connection co = DataSourceProvider.getDataSource().getConnection()){
            String sqlQuery="SELECT * FROM UTILISATEUR WHERE email=?";
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
                        throw new UserNotFoundException();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public int getSqlIdUser(Utilisateur user) throws UserNotFoundException {
        Integer id = null;
        try(Connection co = DataSourceProvider.getDataSource().getConnection()){
            try(PreparedStatement pStm = co.prepareStatement("SELECT idUtilisateur FROM UTILISATEUR WHERE nomUtilisateur =? AND prenomUtilisateur =? AND email =? AND mdp = ? ")) {
                pStm.setString(1, user.getNom());
                pStm.setString(2, user.getPrenom());
                pStm.setString(3, user.getEmail());
                pStm.setString(4, user.getMdp());
                try(ResultSet rs = pStm.executeQuery()) {
                    while(rs.next()) {
                        id = rs.getInt("idUtilisateur");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (id == null)
            throw new UserNotFoundException();
        else
            return id;
    }
}
