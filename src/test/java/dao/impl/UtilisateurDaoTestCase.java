package dao.impl;

import dao.FilmDao;
import dao.UtilisateurDao;
import entity.Film;
import entity.Utilisateur;
import exception.UserAlreadyExistingException;
import exception.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class UtilisateurDaoTestCase {

    private UtilisateurDao userDao = new UtilisateurDaoImpl();

    @Before
    public void initDb() throws Exception {
        try (Connection co = DataSourceProvider.getDataSource().getConnection();
             Statement stm = co.createStatement()) {
            stm.executeUpdate("DELETE FROM commentaire");
            stm.executeUpdate("DELETE FROM preferer");
            stm.executeUpdate("DELETE FROM UTILISATEUR");
            stm.executeUpdate(
                    "INSERT INTO UTILISATEUR ( idUtilisateur, prenomUtilisateur, nomUtilisateur, email, mdp, admin) "
                            + "VALUES (1,'prenom1', 'nom1', 'email1@gmail.com', 'mdp1', 0),"
                            + "(2,'prenom2', 'nom2', 'email2@gmail.com', 'mdp2', 0);");
        }
    }

    @Test
    public void shouldListUser() {
        //WHEN
        List<Utilisateur> user = userDao.listUser();
        //THEN
        assertThat(user).hasSize(2);
        assertThat(user).extracting(
                Utilisateur::getPrenom,
                Utilisateur::getNom,
                Utilisateur::getEmail,
                Utilisateur::getMdp,
                Utilisateur::isAdmin).containsOnly(
                tuple("prenom1", "nom1", "email1@gmail.com", "mdp1", false),
                tuple("prenom2", "nom2", "email2@gmail.com", "mdp2", false));
    }

    @Test
    public void shouldGetUser() throws UserNotFoundException {
        //WHEN
        Utilisateur user = userDao.getUser(1);
        //THEN
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getPrenom()).isEqualTo("prenom1");
        assertThat(user.getNom()).isEqualTo("nom1");
        assertThat(user.getEmail()).isEqualTo("email1@gmail.com");
        assertThat(user.isAdmin()).isEqualTo(false);
    }

    @Test (expected = UserNotFoundException.class)
    public void shouldGetUserNotFoundException() throws UserNotFoundException {
        //given
        int id = 3;
        //When
        userDao.getUser(id);
        //then
        fail("userNotFound not Throw as expected");
    }

    @Test (expected = UserAlreadyExistingException.class)
    public void shouldAddUserThrowUserAlreadyExistingException() throws UserAlreadyExistingException
    {
        //given
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1", false);
        //when
        userDao.addUser(user);
        //then
        fail("UserAlreadyExisting not throw as expected");
    }
}
