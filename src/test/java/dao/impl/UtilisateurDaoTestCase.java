package dao.impl;

import dao.FilmDao;
import dao.UtilisateurDao;
import entity.Film;
import entity.Utilisateur;
import exception.UserAlreadyExistingException;
import exception.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
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
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","$argon2i$v=19$m=65536,t=5,p=1$zFnaINNvYeCrC75OYuuZEl9al5weOMnSXcOUoIWhUdIMRbNvXF1ipU5aMaU0HVXtsotzpepy/LxIHtd7SJMgFpk7T4T6eE24y3CxyiuG1woN5vMrPCnl4ldjtAmWQ/iEsL0JRXuthPrbFO1GkA+k4D2s7E9SNF9JA8sJaSHURU8$U5xfj0Qz7+T3sr05PxuUhEgAKU2+WxhcrFMUUS2yVGi2egf4rSsxZ9FSXYliBnx03aXgNEvtPrZ7zWq2TQdw9LA+gWS4+IOrKk", false);
        //when
        userDao.addUser(user);
        //then
        fail("UserAlreadyExisting not throw as expected");
    }

    @Test
    public void shouldDeleteUser() throws UserNotFoundException, SQLException {
        //given
        Utilisateur user4 = new Utilisateur(4,"prenom4", "nom4", "email4@gmail.com", "mdp4","$argon2i$v=19$m=65536,t=5,p=1$zFnaINNvYeCrC75OYuuZEl9al5weOMnSXcOUoIWhUdIMRbNvXF1ipU5aMaU0HVXtsotzpepy/LxIHtd7SJMgFpk7T4T6eE24y3CxyiuG1woN5vMrPCnl4ldjtAmWQ/iEsL0JRXuthPrbFO1GkA+k4D2s7E9SNF9JA8sJaSHURU8$U5xfj0Qz7+T3sr05PxuUhEgAKU2+WxhcrFMUUS2yVGi2egf4rSsxZ9FSXYliBnx03aXgNEvtPrZ7zWq2TQdw9LA+gWS4+IOrKk", false);
        try (Connection co = DataSourceProvider.getDataSource().getConnection();
             Statement stm = co.createStatement()) {
            stm.executeUpdate(
                    "INSERT INTO UTILISATEUR ( idUtilisateur, prenomUtilisateur, nomUtilisateur, email, mdp, admin) "
                            + "VALUES (4,'prenom4', 'nom4', 'email4@gmail.com', 'mdp4','$argon2i$v=19$m=65536,t=5,p=1$zFnaINNvYeCrC75OYuuZEl9al5weOMnSXcOUoIWhUdIMRbNvXF1ipU5aMaU0HVXtsotzpepy/LxIHtd7SJMgFpk7T4T6eE24y3CxyiuG1woN5vMrPCnl4ldjtAmWQ/iEsL0JRXuthPrbFO1GkA+k4D2s7E9SNF9JA8sJaSHURU8$U5xfj0Qz7+T3sr05PxuUhEgAKU2+WxhcrFMUUS2yVGi2egf4rSsxZ9FSXYliBnx03aXgNEvtPrZ7zWq2TQdw9LA+gWS4+IOrKk', 0);");
        } catch (SQLException e) { }
        //when
        Utilisateur userDelete = userDao.deleteUser(4);
        //then
        Assertions.assertThat(userDelete.getEmail()).isEqualTo(user4.getEmail());
    }

    @Test
    public void shouldDelteUserThrowUserNotFoundException() throws UserNotFoundException, SQLException {
        //given
        int id = 5;
        //when
        Utilisateur res = userDao.deleteUser(id);
        //then
        Assertions.assertThat(res).isNull();
    }
}
