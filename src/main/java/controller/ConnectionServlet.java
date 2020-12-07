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

@WebServlet("/connection")
public class ConnectionServlet extends HttpServlet {

    static final Logger LOGGER = LogManager.getLogger();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String pwd = request.getParameter("mdp");
        LOGGER.info("email "+email+" is trying to log in");
        Utilisateur utilisateur = null;
        try {
            utilisateur = UtilisateurService.getInstance().getUserByEmail(email);

        if(utilisateur !=null){
            String pwdHache=utilisateur.getMdpHash();
            LOGGER.debug("email existing in the db");
            if(MotDePasseUtils.validerMotDePasse(pwd, pwdHache)) {
                request.getSession().setAttribute("utilisateurConnecte", utilisateur);
                LOGGER.info("psw correct, "+email+" logging in");
            } else {
                LOGGER.info("wrong password");
            }

        }
        } catch (UserNotFoundException e) {
            LOGGER.debug("email not found");
            e.printStackTrace();
        }
        LOGGER.debug("redirecting user to home page");
        response.sendRedirect("accueil");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect("accueil");
    }
}
