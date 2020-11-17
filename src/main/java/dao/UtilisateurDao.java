package dao;

import entity.Utilisateur;
import exception.UserAlreadyExistingException;
import exception.UserNotFoundException;

import java.util.List;

public interface UtilisateurDao {

    public List<Utilisateur> listUser();
    public Utilisateur getUser(Integer id) throws UserNotFoundException;
    public void addUser(Utilisateur user) throws UserAlreadyExistingException;
}
