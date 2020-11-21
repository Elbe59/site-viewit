package service;

import dao.FilmDao;
import dao.UtilisateurDao;
import dao.impl.FilmDaoImpl;
import dao.impl.UtilisateurDaoImpl;
import entity.Film;
import entity.Utilisateur;
import exception.UserNotFoundException;

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

    public List<Utilisateur> listUser() {
        return utilisateurDao.listUser();
    }

    public Utilisateur getUser(int id) throws UserNotFoundException {
        return utilisateurDao.getUser(id);
    }

    public Utilisateur getUserByEmail(String email) throws UserNotFoundException {return utilisateurDao.getUserByEmail(email);}

    //public void deleteUser(int id){utilisateurDao.deleteUser(id);}

}
