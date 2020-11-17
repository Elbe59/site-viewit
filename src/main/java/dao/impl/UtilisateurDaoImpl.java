package dao.impl;

import dao.UtilisateurDao;
import entity.Utilisateur;
import exception.UserAlreadyExistingException;
import exception.UserNotFoundException;

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

    public void addUser(Utilisateur user) throws UserAlreadyExistingException {
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
            try (PreparedStatement pStm = co.prepareStatement("INSERT INTO UTILISATEUR ( prenomUtilisateur, nomUtilisateur, email, mdp, admin) VALUES (?,?,?,?,?);")) {
                pStm.setString(1, user.getPrenom());
                pStm.setString(2, user.getNom());
                pStm.setString(3, user.getEmail());
                pStm.setString(4, user.getMdp());
                pStm.setInt(5, user.isAdmin()?1:0);
                pStm.executeUpdate();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
