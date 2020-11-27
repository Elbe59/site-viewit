package service;

import dao.FilmDao;
import dao.UtilisateurDao;
import dao.impl.FilmDaoImpl;
import dao.impl.UtilisateurDaoImpl;
import entity.Film;
import entity.Utilisateur;
import exception.UserAlreadyAdminException;
import exception.UserAlreadyDownException;
import exception.UserAlreadyExistingException;
import exception.UserNotFoundException;
import utils.MotDePasseUtils;

import java.sql.SQLException;
import java.util.List;

public class UtilisateurService {
    private static class UtilisateurHolder {
        private final static UtilisateurService instance = new UtilisateurService();
    }

    public static UtilisateurService getInstance() {
        return UtilisateurService.UtilisateurHolder.instance;
    }

    private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();

    private UtilisateurService() {

    }
    public Utilisateur addUser(Utilisateur user) throws UserAlreadyExistingException {
        String password=user.getMdpHash();
        String passwordHash= MotDePasseUtils.genererMotDePasse(password);
        user.setMdpHash(passwordHash);
        try{
            utilisateurDao.addUser(user);
        }catch (UserAlreadyExistingException e){
            user = null;
        }
        return user;
    }


    public List<Utilisateur> listUser() {
        return utilisateurDao.listUser();
    }

    public Utilisateur getUser(int id) throws UserNotFoundException {
        try{
            return utilisateurDao.getUser(id);
        }catch (UserNotFoundException e){
            return null;
        }
    }

    public Utilisateur getUserByEmail(String email) throws UserNotFoundException
    {
        try{
            return utilisateurDao.getUserByEmail(email);
        }catch (UserNotFoundException e){
            return null;
        }
    }

    public Utilisateur deleteUser(Integer id) throws UserNotFoundException, SQLException
    {
        try{
            return utilisateurDao.deleteUser(id);
        }catch (UserNotFoundException e){
            return null;
        }
    }

    public Utilisateur changeRoleUser(String action,Integer id) throws SQLException, UserAlreadyDownException, UserAlreadyAdminException
    {
        try{
            return utilisateurDao.changeRoleUser(action,id);
        }catch(UserAlreadyDownException | UserAlreadyAdminException e){
            return null;
        }
    }
}
