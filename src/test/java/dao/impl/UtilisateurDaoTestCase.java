package dao.impl;

import dao.FilmDao;
import dao.UtilisateurDao;
import entity.Film;
import entity.Genre;
import entity.Utilisateur;
import exception.*;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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
                    "INSERT INTO UTILISATEUR ( idUtilisateur, prenomUtilisateur, nomUtilisateur, email, mdp, mdpHash, admin) "
                            + "VALUES (1,'prenom1', 'nom1', 'email1@gmail.com', 'mdp1', 'mdpHash1', 0),"
                            + "(2,'prenom2', 'nom2', 'email2@gmail.com', 'mdp2', 'mdpHash2', 1);");
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
                Utilisateur::getMdpHash,
                Utilisateur::isAdmin).containsOnly(
                tuple("prenom1", "nom1", "email1@gmail.com", "mdp1", "mdpHash1", false),
                tuple("prenom2", "nom2", "email2@gmail.com", "mdp2", "mdpHash2", true));
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
        assertThat(user.getMdp()).isEqualTo("mdp1");
        assertThat(user.getMdpHash()).isEqualTo("mdpHash1");
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

    @Test
    public void shouldAddUser() throws UserAlreadyExistingException, UserNotFoundException {
        //given
        Utilisateur verif = null;
        Utilisateur user = new Utilisateur("prenom3","nom3","email3@gmail.com","mdp3","mdpHash3",false);
        //when
        Utilisateur res = userDao.addUser(user);
        //then
        Assertions.assertThat(res).isNotNull();
        try(Connection co = DataSourceProvider.getDataSource().getConnection()){
            try(PreparedStatement pStm = co.prepareStatement("SELECT * FROM UTILISATEUR WHERE idUtilisateur=?;")) {
                pStm.setInt(1, res.getId());
                try(ResultSet rs = pStm.executeQuery()) {
                    while(rs.next()) {

                        verif = new Utilisateur(
                                rs.getInt("idUtilisateur"),
                                rs.getString("prenomUtilisateur"),
                                rs.getString("nomUtilisateur"),
                                rs.getString("email"),
                                rs.getString("mdp"),
                                rs.getString("mdpHash"),
                                rs.getInt("admin")==1?true:false);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); }
        Assertions.assertThat(verif).isNotNull();
        Assertions.assertThat(verif.getPrenom()).isEqualTo(user.getPrenom());
        Assertions.assertThat(verif.getNom()).isEqualTo(user.getNom());
        Assertions.assertThat(verif.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(verif.getMdp()).isEqualTo(user.getMdp());
        Assertions.assertThat(verif.getMdpHash()).isEqualTo(user.getMdpHash());
        Assertions.assertThat(verif.isAdmin()).isEqualTo(user.isAdmin());
    }

    @Test (expected = UserAlreadyExistingException.class)
    public void shouldAddUserThrowUserAlreadyExistingException() throws UserAlreadyExistingException, UserNotFoundException {
        //given
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        //when
        userDao.addUser(user);
        //then
        fail("UserAlreadyExisting not throw as expected");
    }

    @Test
    public void shouldDeleteUser() throws UserNotFoundException, SQLException {
        //given
        Utilisateur user4 = new Utilisateur(4,"prenom4", "nom4", "email4@gmail.com", "mdp4","mdpHash4", false);
        try (Connection co = DataSourceProvider.getDataSource().getConnection();
             Statement stm = co.createStatement()) {
            stm.executeUpdate(
                    "INSERT INTO UTILISATEUR ( idUtilisateur, prenomUtilisateur, nomUtilisateur, email, mdp, mdpHash, admin) "
                            + "VALUES (4,'prenom4', 'nom4', 'email4@gmail.com', 'mdp4','mdpHash4', 0);");
        } catch (SQLException e) { }
        //when
        Utilisateur userDelete = userDao.deleteUser(4);
        //then
        Assertions.assertThat(userDelete.getEmail()).isEqualTo(user4.getEmail());
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM Utilisateur WHERE email = 'email4@gmail.com'")) {
                assertThat(rs.next()).isFalse();
            } catch (SQLException e) {
            }
        }
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

 /*   @Test
    public void shouldGetSqlIdUser() throws IOException, UserNotFoundException {
        //given
        Utilisateur user = new Utilisateur("prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        //when
        int id = userDao.getSqlIdUser(user);
        //then
        Assertions.assertThat(id).isEqualTo(1);
    }*/

 /*   @Test (expected = UserNotFoundException.class)
    public void shouldGetSqlIdFilmThrowFilmNotFoundException() throws IOException, UserNotFoundException{
        //given
        Utilisateur user = new Utilisateur("prenom4", "nom4", "email4@gmail.com", "mdp4","mdpHash4", false);
        //when
        int id = userDao.getSqlIdUser(user);
        //then
        Assertions.fail("User not found not throw as expected");
    }*/

    @Test
    public void shouldChangeRoleUser() throws SQLException, UserAlreadyDownException, UserAlreadyAdminException {
        //given
        String up = "up";
        String down = "down";
        //when
        Utilisateur res1 = userDao.changeRoleUser(up,1);
        Utilisateur res2 = userDao.changeRoleUser(down, 2);
        //then
        assertThat(res1.isAdmin()).isEqualTo(true);
        assertThat(res2.isAdmin()).isEqualTo(false);
    }

    @Test
    public void shouldChangeRoleUserThrowUserNotFoundException() throws SQLException, UserAlreadyDownException, UserAlreadyAdminException {
        //given
        String down = "down";
        //when
        Utilisateur res = userDao.changeRoleUser(down, 3);
        //then
        Assertions.assertThat(res).isNull();
    }

    @Test (expected = UserAlreadyAdminException.class)
    public void shouldChangeRoleUserThrowUserAlreadyAdminException() throws UserAlreadyAdminException, SQLException, UserAlreadyDownException {
        //given
        String up = "up";
        //when
        Utilisateur res = userDao.changeRoleUser(up,2);
        //then
        fail("User already admin not throw as expected");
    }

    @Test (expected = UserAlreadyDownException.class)
    public void shouldChangeRoleUserThrowUserAlreadyDownException() throws UserAlreadyAdminException, SQLException, UserAlreadyDownException {
        //given
        String down = "down";
        //when
        Utilisateur res = userDao.changeRoleUser(down,1);
        //then
        fail("User already down not throw as expected");
    }

    @Test
    public void shouldGetUserByEmail() throws UserNotFoundException {
        //given
        Utilisateur user = new Utilisateur(1,"prenom1", "nom1", "email1@gmail.com", "mdp1","mdpHash1", false);
        //when
        Utilisateur res = userDao.getUserByEmail("email1@gmail.com");
        //then
        Assertions.assertThat(res).isEqualToComparingFieldByField(user);
    }

    @Test (expected = UserNotFoundException.class)
    public void shouldGetUserByEmailThrowUserNotFoundException() throws UserNotFoundException {
        //given
        String email = "email6@gmail.com";
        //when
        Utilisateur res = userDao.getUserByEmail(email);
        //then
        fail("User not found not throw as expected");
    }
}
