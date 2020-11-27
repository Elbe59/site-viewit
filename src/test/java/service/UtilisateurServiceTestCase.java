package service;

import dao.UtilisateurDao;
import dao.impl.DataSourceProvider;
import dao.impl.UtilisateurDaoImpl;
import entity.Utilisateur;
import exception.UserAlreadyAdminException;
import exception.UserAlreadyDownException;
import exception.UserAlreadyExistingException;
import exception.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import utils.MotDePasseUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(MockitoJUnitRunner.class)
public class UtilisateurServiceTestCase {

    @InjectMocks
    UtilisateurService userService;
    @Mock
    UtilisateurDao userDao = new UtilisateurDaoImpl();

    @Before
    public void initDb() throws Exception {
        try (Connection co = DataSourceProvider.getDataSource().getConnection();
             Statement stm = co.createStatement()) {
            stm.executeUpdate("DELETE FROM commentaire");
            stm.executeUpdate("DELETE FROM preferer");
            stm.executeUpdate("DELETE FROM UTILISATEUR");
            stm.executeUpdate(
                    "INSERT INTO UTILISATEUR ( idUtilisateur, prenomUtilisateur, nomUtilisateur, email, mdp, mdpHash, admin) "
                            + "VALUES (1,'prenom1', 'nom1', 'email1@gmail.com', 'mdp1', 'mdpHash1', 0),"
                            + "(2,'prenom2', 'nom2', 'email2@gmail.com', 'mdp2', 'mdpHash2', 1);");
        }
    }

    @Test
    public void shouldAddUser() throws UserAlreadyExistingException {
        //given
        Utilisateur user = new Utilisateur("prenom3","nom3","email3@gmail.com","mdp3", MotDePasseUtils.genererMotDePasse("mdpHash3"),false);
        //when
        Utilisateur res = userService.getInstance().addUser(user);
        //then
        Assertions.assertThat(res).isEqualToComparingFieldByField(user);
    }

    @Test
    public void shouldAddUserThrowUserAlreadyExistingException() throws UserAlreadyExistingException {
        //given
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1",MotDePasseUtils.genererMotDePasse("mdpHash1"), false);
        //when
        Utilisateur res = userService.getInstance().addUser(user);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldListUser() {
        //WHEN
        List<Utilisateur> user = userService.getInstance().listUser();
        //THEN
        assertThat(user).hasSize(2);
        assertThat(user).extracting(
                Utilisateur::getPrenom,
                Utilisateur::getNom,
                Utilisateur::getEmail,
                Utilisateur::getMdp,
                Utilisateur::getMdpHash,
                Utilisateur::isAdmin).containsOnly(
                tuple("prenom1", "nom1", "email1@gmail.com", "mdp1", "mdpHash1", false),
                tuple("prenom2", "nom2", "email2@gmail.com", "mdp2", "mdpHash2", true));
    }

    @Test
    public void shouldGetUser() throws UserNotFoundException {
        //given
        int id = 1;
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        //when
        Utilisateur res = userService.getInstance().getUser(id);
        //then
        Assertions.assertThat(res).isEqualToComparingFieldByField(user);
    }

    @Test
    public void shouldGetUserThrowUserNotFoundException() throws UserNotFoundException {
        //given
        int id = 3;
        //when
        Utilisateur res = userService.getInstance().getUser(id);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldGetUserByEmail() throws UserNotFoundException {
        //given
        String mail = "email1@gmail.com";
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        //when
        Utilisateur res = userService.getInstance().getUserByEmail(mail);
        //then
        Assertions.assertThat(res).isEqualToComparingFieldByField(user);
    }

    @Test
    public void shouldGetUserByEmailThrowUserNotFoundException() throws UserNotFoundException {
        //given
        String mail = "email3@gmail.com";
        //when
        Utilisateur res = userService.getInstance().getUserByEmail(mail);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldDeleteUser() throws UserNotFoundException, SQLException {
        //given
        int id = 1;
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        //when
        Utilisateur res = userService.getInstance().deleteUser(id);
        //then
        Assertions.assertThat(res).isEqualToComparingFieldByField(user);
    }

    @Test
    public void shouldDeleteUserThrowUserNotFoundException() throws UserNotFoundException, SQLException {
        //given
        int id = 3;
        //when
        Utilisateur res = userService.getInstance().deleteUser(id);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldChangeRoleUser() throws SQLException, UserAlreadyDownException, UserAlreadyAdminException {
        //given
        String up = "up";
        String down = "down";
        //when
        Utilisateur res1 = userService.getInstance().changeRoleUser(up,1);
        Utilisateur res2 = userService.getInstance().changeRoleUser(down, 2);
        //then
        assertThat(res1.isAdmin()).isEqualTo(true);
        assertThat(res2.isAdmin()).isEqualTo(false);
    }

    @Test
    public void shouldChangeRoleUserThrowUserNotFoundException() throws SQLException, UserAlreadyDownException, UserAlreadyAdminException {
        //given
        String down = "down";
        //when
        Utilisateur res = userService.getInstance().changeRoleUser(down, 3);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldChangeRoleUserThrowUserAlreadyAdminException() throws UserAlreadyAdminException, SQLException, UserAlreadyDownException {
        //given
        String up = "up";
        //when
        Utilisateur res = userService.getInstance().changeRoleUser(up,2);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test
    public void shouldChangeRoleUserThrowUserAlreadyDownException() throws UserAlreadyAdminException, SQLException, UserAlreadyDownException {
        //given
        String down = "down";
        //when
        Utilisateur res = userService.getInstance().changeRoleUser(down,1);
        //then
        Assertions.assertThat(res).isNull();
    }
}
