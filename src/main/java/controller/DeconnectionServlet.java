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

@WebServlet("/deconnection")
public class DeconnectionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utilisateur utilisateur = null;
        request.getSession().setAttribute("utilisateurConnecte", utilisateur);
        response.sendRedirect("accueil");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
