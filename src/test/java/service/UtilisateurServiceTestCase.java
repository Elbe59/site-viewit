package service;

import dao.UtilisateurDao;
import dao.impl.UtilisateurDaoImpl;
import entity.Utilisateur;
import entity.UtilisateurDto;
import exception.UserAlreadyAdminException;
import exception.UserAlreadyDownException;
import exception.UserAlreadyExistingException;
import exception.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import utils.MotDePasseUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(MockitoJUnitRunner.class)
public class UtilisateurServiceTestCase {

    @InjectMocks
    UtilisateurService userService;
    @Mock
    UtilisateurDao userDao = new UtilisateurDaoImpl();

    @Test
    public void shouldAddUser() throws UserAlreadyExistingException {
        //GIVEN
        Utilisateur user = new Utilisateur("prenom3","nom3","email3@gmail.com","mdp3", MotDePasseUtils.genererMotDePasse("mdpHash3"),false);
        Utilisateur user1=new Utilisateur("prenom1", "nom1", "email1@gmail.com", "mdp1", "mdpHash1", false);
        Utilisateur user2=new Utilisateur("prenom2", "nom2", "email2@gmail.com", "mdp2", "mdpHash2", true);
        List<Utilisateur> utilisateurs=new ArrayList<Utilisateur>();
        utilisateurs.add(user1);
        utilisateurs.add(user2);
        Mockito.when(userDao.listUser()).thenReturn(utilisateurs);
        Mockito.when(userDao.addUser(user)).thenReturn(user);
        //WHEN

        Utilisateur res = userService.addUser(user);
        //THEN
        Assertions.assertThat(res).isEqualToComparingFieldByField(user);
    }

    @Test
    public void shouldAddUserThrowUserAlreadyExistingException() throws UserAlreadyExistingException {
        //GIVEN
        Utilisateur user = new Utilisateur("prenom3","nom3","email2@gmail.com","mdp3", "mdpHash3",false);
        Utilisateur user1=new Utilisateur("prenom1", "nom1", "email1@gmail.com", "mdp1", "mdpHash1", false);
        Utilisateur user2=new Utilisateur("prenom2", "nom2", "email2@gmail.com", "mdp2", "mdpHash2", true);
        List<Utilisateur> utilisateurs=new ArrayList<Utilisateur>();
        utilisateurs.add(user1);
        utilisateurs.add(user2);
        Mockito.when(userDao.listUser()).thenReturn(utilisateurs);
        //WHEN
        try{
            userService.addUser(user);
        }
        catch (UserAlreadyExistingException e){
            Assertions.assertThatExceptionOfType(UserAlreadyExistingException.class);
        }
        //THEN
        Mockito.verify(userDao,Mockito.times(1)).listUser();
        Mockito.verify(userDao,Mockito.never()).addUser(Mockito.any());
    }

    @Test
    public void shouldListUser() {
        Utilisateur user1=new Utilisateur("prenom1", "nom1", "email1@gmail.com", "mdp1", "mdpHash1", false);
        Utilisateur user2=new Utilisateur("prenom2", "nom2", "email2@gmail.com", "mdp2", "mdpHash2", false);
        List<Utilisateur> utilisateurs=new ArrayList<Utilisateur>();
        utilisateurs.add(user1);
        utilisateurs.add(user2);
        Mockito.when(userDao.listUser()).thenReturn(utilisateurs);

        //WHEN
        List<Utilisateur> user = userService.listUser();
        //THEN
        Assertions.assertThat(user).containsExactlyInAnyOrderElementsOf(utilisateurs);
    }

    @Test
    public void shouldGetAnExistantUser() throws UserNotFoundException {
        //GIVEN
        int id = 1;
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        Mockito.when(userDao.getUser(id)).thenReturn(user);
        //WHEN
        Utilisateur get_user=userService.getUser(id);
        //THEN
        Assertions.assertThat(get_user).isEqualToComparingFieldByField(user);
    }

    @Test
    public void shouldGetUserAndThrowUserNotFoundException() throws UserNotFoundException {
        int id =1;
        Mockito.when(userDao.getUser(1)).thenThrow(new UserNotFoundException("Utilisateur inexistant"));;
        try{
            userService.getUser(id);
        }
        catch (UserNotFoundException e){
            Assertions.assertThatExceptionOfType(UserNotFoundException.class);
        }
    }

    @Test
    public void shouldGetAnExistantUserByEmail() throws UserNotFoundException {
        //GIVEN
        String email="prenom.nom@gmail.com";
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        Mockito.when(userDao.getUserByEmail(email)).thenReturn(user);
        //WHEN
        Utilisateur get_user=userService.getUserByEmail(email);
        //THEN
        Assertions.assertThat(get_user).isEqualToComparingFieldByField(user);
    }

    @Test
    public void shouldGetUserByEmailAndThrowUserNotFoundException() throws UserNotFoundException {
        String email="prenom.nom@gmail.com";
        Mockito.when(userDao.getUserByEmail(email)).thenThrow(new UserNotFoundException("Utilisateur inexistant"));;
        try{
            userService.getUserByEmail(email);
        }
        catch (UserNotFoundException e){
            Assertions.assertThatExceptionOfType(UserNotFoundException.class);
        }
    }

    @Test
    public void shouldDeleteUser() throws UserNotFoundException, SQLException {
        //given
        int id = 1;
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        Mockito.when(userDao.getUser(id)).thenReturn(user);
        Mockito.when(userDao.deleteUser(id)).thenReturn(user);
        //when
        Utilisateur get_user=userService.deleteUser(id);
        //then
        Assertions.assertThat(get_user).isEqualToComparingFieldByField(user);
    }

    @Test
    public void shouldDeleteInexistantUserAndThrowUserNotFoundException() throws UserNotFoundException, SQLException {
        //GIVEN
        int id = 1;
        Mockito.when(userDao.getUser(id)).thenThrow(new UserNotFoundException("Utilisateur inexistant"));
        try{
            userService.deleteUser(id);
        }
        catch (UserNotFoundException e){
            Assertions.assertThatExceptionOfType(UserNotFoundException.class);
        }
        Mockito.verify(userDao,Mockito.never()).deleteUser(Mockito.any());
    }

    @Test
    public void shouldChangeRoleUserToAdmin() throws SQLException, UserAlreadyDownException, UserAlreadyAdminException, UserNotFoundException {
        //given
        int id=1;
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        Utilisateur userUp = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", true);
        Mockito.when(userDao.getUser(id)).thenReturn(user);
        Mockito.when(userDao.changeRoleUser("up",id)).thenReturn(userUp);
        //when
        Utilisateur utilisateur=userService.changeRoleUser("up",id);
        //then
        Assertions.assertThat(utilisateur.isAdmin()).isNotEqualTo(user.isAdmin());
        Assertions.assertThat(userUp.getId()).isEqualTo(user.getId());
        Mockito.verify(userDao,Mockito.times(1)).changeRoleUser("up",1);
        Mockito.verify(userDao,Mockito.times(1)).getUser(1);
    }

    @Test
    public void shouldChangeRoleUserToUser() throws SQLException, UserAlreadyDownException, UserAlreadyAdminException, UserNotFoundException {
        //given
        int id=1;
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", true);
        Utilisateur userDown = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        Mockito.when(userDao.getUser(id)).thenReturn(user);
        Mockito.when(userDao.changeRoleUser("down",id)).thenReturn(userDown);
        //when
        Utilisateur utilisateur=userService.changeRoleUser("down",id);
        //then
        Assertions.assertThat(utilisateur.isAdmin()).isNotEqualTo(user.isAdmin());
        Assertions.assertThat(userDown.getId()).isEqualTo(user.getId());
        Mockito.verify(userDao,Mockito.times(1)).changeRoleUser("down",1);
        Mockito.verify(userDao,Mockito.times(1)).getUser(1);
    }

    @Test
    public void shouldChangeRoleUserToAdminAndThrowUserAlreadyAdminException() throws UserAlreadyAdminException, SQLException, UserAlreadyDownException, UserNotFoundException {
        //GIVEN
        int id = 1;
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", true);
        Mockito.when(userDao.getUser(id)).thenReturn(user);
        try{
            userService.changeRoleUser("up",id);
        }
        catch (UserAlreadyAdminException e){
            Assertions.assertThatExceptionOfType(UserAlreadyAdminException.class);
        }
        Mockito.verify(userDao,Mockito.times(1)).getUser(1);
    }

    @Test
    public void shouldChangeRoleUserToUserAndThrowUserAlreadyDownException() throws UserAlreadyAdminException, SQLException, UserAlreadyDownException, UserNotFoundException {
        //GIVEN
        int id = 1;
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        Mockito.when(userDao.getUser(id)).thenReturn(user);
        try{
            userService.changeRoleUser("down",id);
        }
        catch (UserAlreadyDownException e){
            Assertions.assertThatExceptionOfType(UserAlreadyDownException.class);
        }
        Mockito.verify(userDao,Mockito.times(1)).getUser(1);
    }
    
    @Test
    public void shouldChangeRoleUserAndThrowUserNotFoundException() throws UserAlreadyAdminException, SQLException, UserAlreadyDownException, UserNotFoundException {
        //GIVEN
        int id = 1;
        Mockito.when(userDao.getUser(id)).thenThrow(new UserNotFoundException("Utilisateur inexistant"));
        try{
            userService.deleteUser(id);
        }
        catch (UserNotFoundException e){
            Assertions.assertThatExceptionOfType(UserNotFoundException.class);
        }
        Mockito.verify(userDao,Mockito.never()).changeRoleUser(Mockito.any(),Mockito.any());
    }
    
    @Test
    public void shouldModifyUser() throws SQLException, UserAlreadyExistingException, UserNotFoundException {
    	//GIVEN
        int id=1;
    	Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        Utilisateur new_user = new Utilisateur(1,"pren", "no", "ema1@gmail.com", "mp1","mdpash1", false);
        Utilisateur user1=new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1", "mdpHash1", false);
        Utilisateur user2=new Utilisateur(2,"prenom2", "nom2", "email2@gmail.com", "mdp2", "mdpHash2", false);
        List<Utilisateur> utilisateurs=new ArrayList<Utilisateur>();
        utilisateurs.add(user1);
        utilisateurs.add(user2);
        Mockito.when(userDao.getUser(id)).thenReturn(user);
        Mockito.when(userDao.listUser()).thenReturn(utilisateurs);
        Mockito.when(userDao.modifyUser(new_user)).thenReturn(new_user);
        //WHEN
    	Utilisateur modify_user= userService.modifyUser(new_user);
    	//THEN
        Mockito.verify(userDao,Mockito.times(1)).listUser();
        Mockito.verify(userDao,Mockito.times(1)).getUser(1);
        Mockito.verify(userDao,Mockito.times(1)).modifyUser(new_user);
    	assertThat(modify_user.getId()).isEqualTo(user.getId());
        assertThat(modify_user.isAdmin()).isEqualTo(user.isAdmin());
    }
    
    @Test
    public void shouldModifyUserButThrowUserNotFoundException() throws SQLException, UserAlreadyExistingException, UserNotFoundException {
        //GIVEN
        int id = 1;
        Utilisateur new_user = new Utilisateur(1,"pren", "no", "ema1@gmail.com", "mp1","mdpash1", false);
        Mockito.when(userDao.getUser(id)).thenThrow(new UserNotFoundException("Utilisateur inexistant"));

        userService.modifyUser(new_user);
        Assertions.assertThatExceptionOfType(UserNotFoundException.class);
        Mockito.verify(userDao,Mockito.never()).listUser();
        Mockito.verify(userDao,Mockito.never()).modifyUser(new_user);
    }
    
    @Test
    public void shouldModifyUserButThrowUserAlreadyExistingException() throws SQLException, UserAlreadyExistingException, UserNotFoundException {
        //GIVEN
        int id = 1;
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        Utilisateur new_user = new Utilisateur(1,"pren", "no", "email2@gmail.com", "mp1","mdpash1", false);
        Utilisateur user1=new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1", "mdpHash1", false);
        Utilisateur user2=new Utilisateur(2,"prenom2", "nom2", "email2@gmail.com", "mdp2", "mdpHash2", false);
        List<Utilisateur> utilisateurs=new ArrayList<Utilisateur>();
        utilisateurs.add(user1);
        utilisateurs.add(user2);
        Mockito.when(userDao.getUser(id)).thenReturn(user);
        Mockito.when(userDao.listUser()).thenReturn(utilisateurs);
        //WHEN
        try{
            userService.modifyUser(new_user);
        }
        catch (UserAlreadyExistingException e){
            Assertions.assertThatExceptionOfType(UserAlreadyExistingException.class);
        }
        Mockito.verify(userDao,Mockito.times(1)).listUser();
        Mockito.verify(userDao,Mockito.times(1)).getUser(1);
        Mockito.verify(userDao,Mockito.never()).modifyUser(new_user);
    }
    
    @Test
    public void shouldListUsersDto() {
        Utilisateur user1=new Utilisateur("prenom1", "nom1", "email1@gmail.com", "mdp1", "mdpHash1", false);
        Utilisateur user2=new Utilisateur("prenom2", "nom2", "email2@gmail.com", "mdp2", "mdpHash2", true);
        List<Utilisateur> utilisateurs=new ArrayList<Utilisateur>();
        utilisateurs.add(user1);
        utilisateurs.add(user2);
        Mockito.when(userDao.listUser()).thenReturn(utilisateurs);
        //WHEN
        List<UtilisateurDto> usersDto = userService.listUsersDto();
        //THEN
        Assertions.assertThat(usersDto).hasSize(2);
        Assertions.assertThat(usersDto).extracting(
                UtilisateurDto::getPrenom,
                UtilisateurDto::getNom,
                UtilisateurDto::getEmail,
                UtilisateurDto::isAdmin).containsOnly(
                tuple("prenom1", "nom1", "email1@gmail.com", false),
                tuple("prenom2", "nom2", "email2@gmail.com", true));
    	
    }
}
