package service;

import dao.UtilisateurDao;
import dao.impl.UtilisateurDaoImpl;
import entity.Utilisateur;
import entity.UtilisateurDto;
import exception.UserAlreadyAdminException;
import exception.UserAlreadyDownException;
import exception.UserAlreadyExistingException;
import exception.UserNotFoundException;
import utils.MotDePasseUtils;

import java.sql.SQLException;
import java.util.ArrayList;
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
        utilisateurDao.addUser(user);
        return user;
    }

    public Utilisateur modifyUser(Utilisateur user) throws SQLException {
        String password=user.getMdpHash();
        String passwordHash= MotDePasseUtils.genererMotDePasse(password);
        user.setMdpHash(passwordHash);
        utilisateurDao.modifyUser(user);
        return user;
    }

    public List<UtilisateurDto>  listUsersDto(){
        List<Utilisateur> utilisateurs=utilisateurDao.listUser();

        List<UtilisateurDto> listUserDto= new ArrayList<>();
        for (Utilisateur utilisateur: utilisateurs) {
            UtilisateurDto userDto=new UtilisateurDto(utilisateur.getId(),utilisateur.getPrenom(),utilisateur.getNom(),utilisateur.getEmail(),utilisateur.isAdmin());
            listUserDto.add(userDto);
        }
        return listUserDto;
    }
    public List<Utilisateur> listUser() {
        return utilisateurDao.listUser();
    }

    public Utilisateur getUser(int id) throws UserNotFoundException {
        return utilisateurDao.getUser(id);
    }

    public Utilisateur getUserByEmail(String email) throws UserNotFoundException {return utilisateurDao.getUserByEmail(email);}

    public Utilisateur deleteUser(Integer id) throws UserNotFoundException, SQLException {return utilisateurDao.deleteUser(id);}
    public Utilisateur changeRoleUser(String action,Integer id) throws SQLException, UserAlreadyDownException, UserAlreadyAdminException {return utilisateurDao.changeRoleUser(action,id);}
}
