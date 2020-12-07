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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UtilisateurService {
	
	static final Logger LOGGER = LogManager.getLogger();
	
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
    	List<Utilisateur> users = utilisateurDao.listUser();
        boolean existing  = false;
        for (int i = 0; i<users.size(); i++)
        {
            if (users.get(i).getEmail().equals(user.getEmail()))
                existing = true;
        }
        if (existing) {
        	throw new UserAlreadyExistingException("Utilisateur déjà existant");
        }
        else {
        	String password=user.getMdpHash();
            String passwordHash= MotDePasseUtils.genererMotDePasse(password);
            user.setMdpHash(passwordHash);
            utilisateurDao.addUser(user);
            return user;
        }
    }

    public Utilisateur modifyUser(Utilisateur user) throws SQLException, UserAlreadyExistingException {
    	try {
    		utilisateurDao.getUser(user.getId());
    	}catch(UserNotFoundException e) {
    		LOGGER.error("error modifing user nb "+user.getId());
    		return null;
    	}
    	List<Utilisateur> users = utilisateurDao.listUser();
        boolean existing  = false;
        for (int i = 0; i<users.size(); i++)
        {
            if (users.get(i).getEmail().equals(user.getEmail())) {
            	if(users.get(i).getId() != user.getId()) {
            		existing = true;
            	}
            }
        }
        if (existing){
            throw new UserAlreadyExistingException("Utilisateur déjà existant");
        }
        else {
        	String password=user.getMdpHash();
            String passwordHash= MotDePasseUtils.genererMotDePasse(password);
            user.setMdpHash(passwordHash);
            user = utilisateurDao.modifyUser(user);
            return user;
        }
    }

    public List<UtilisateurDto>  listUsersDto(){
        List<Utilisateur> utilisateurs = utilisateurDao.listUser();

        List<UtilisateurDto> listUserDto = new ArrayList<UtilisateurDto>();
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
    	try {
    		return utilisateurDao.getUser(id);
    	}
        catch(UserNotFoundException e) {
        	return null;
        }
    }

    public Utilisateur getUserByEmail(String email) throws UserNotFoundException {
        try{
            return utilisateurDao.getUserByEmail(email);
        }catch (UserNotFoundException e){
            return null;
        }
    }

    public Utilisateur deleteUser(Integer id) throws UserNotFoundException, SQLException {
        try {
        	utilisateurDao.getUser(id);
        }
        catch(UserNotFoundException e) {
        	return null;
        }
        return utilisateurDao.deleteUser(id);
    }

    public Utilisateur changeRoleUser(String action,Integer id) throws SQLException, UserAlreadyDownException, UserAlreadyAdminException, UserNotFoundException {
        Utilisateur user= utilisateurDao.getUser(id);
        if(action.contentEquals("up") && user.isAdmin()) {
            throw new UserAlreadyAdminException("Utilisateur actuellement admin");
        } else if(action.contentEquals("down") && !user.isAdmin()){
            throw new UserAlreadyDownException("Utilisateur actuellement non admin");
        }
        user = utilisateurDao.changeRoleUser(action,id);
        return user;
    }
}
