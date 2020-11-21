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

@WebServlet("/connection")
public class ConnectionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String pwd = request.getParameter("mdp");

        Utilisateur utilisateur = null;
        try {
            utilisateur = UtilisateurService.getInstance().getUserByEmail(email);

        if(utilisateur !=null){
            String pwdHache=utilisateur.getMdp();
            if(MotDePasseUtils.validerMotDePasse(pwd, pwdHache)) {
                request.getSession().setAttribute("utilisateurConnecte", utilisateur);
                System.out.println("Vous êtes connecté: "+ utilisateur.getNom());
            } else {
                System.out.println("mauvais login/pwd");
            }

        }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        response.sendRedirect("accueil");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
