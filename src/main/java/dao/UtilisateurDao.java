package dao;

import entity.Utilisateur;
import exception.UserAlreadyAdminException;
import exception.UserAlreadyDownException;
import exception.UserAlreadyExistingException;
import exception.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface UtilisateurDao {

    public List<Utilisateur> listUser();
    public Utilisateur getUser(Integer id) throws UserNotFoundException;
    public Utilisateur addUser(Utilisateur user) throws UserAlreadyExistingException;
    public Utilisateur deleteUser(Integer id) throws UserNotFoundException, SQLException;

    Utilisateur modifyUser(Utilisateur user) throws SQLException;

    public Utilisateur getUserByEmail(String email) throws UserNotFoundException;
    public Utilisateur changeRoleUser(String action,Integer id) throws SQLException, UserAlreadyAdminException, UserAlreadyDownException;
    public int getSqlIdUser(Utilisateur user) throws UserNotFoundException;

}
