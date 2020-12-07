package controller;

import entity.Utilisateur;
import exception.UserNotFoundException;
import service.UtilisateurService;
import utils.MotDePasseUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/deconnection")
public class DeconnectionServlet extends HttpServlet {

    static final Logger LOGGER = LogManager.getLogger();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utilisateur utilisateur = null;
        LOGGER.info("user log out");
        request.getSession().setAttribute("utilisateurConnecte", utilisateur);
        LOGGER.debug("redirecting user to home page");
        response.sendRedirect("accueil");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
