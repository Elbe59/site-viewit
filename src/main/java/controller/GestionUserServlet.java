package controller;

import entity.Film;
import entity.Utilisateur;
import exception.*;
import jdk.jshell.execution.Util;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import service.FilmService;
import service.UtilisateurService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/gestionuser")
public class GestionUserServlet extends ServletGenerique {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());
        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateurConnecte");
        List<Utilisateur> listUser = UtilisateurService.getInstance().listUser();
        context.setVariable("listUsers", listUser);
        context.setVariable("utilisateurCo", utilisateur);
        if(utilisateur!=null){
            TemplateEngine engine = createTemplateEngine(req.getServletContext());
            engine.process("listuser", context, resp.getWriter());
        }


    }

    /*protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Utilisateur> listOfUsers = UtilisateurService.getInstance().listUser();
        if(request.getParameter("supp")!=null) {
            int index = Integer.parseInt(request.getParameter("supp"));
            int id = listOfUsers.get(index).getId();
            System.out.println("Delete user: " + (id));
            try {
                UtilisateurService.getInstance().deleteUser(id);
            } catch (UserNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        if(request.getParameter("down")!=null){
            int index = Integer.parseInt(request.getParameter("down"));
            int id = listOfUsers.get(index).getId();
            System.out.println("Retrograde: " + (id));
            try {
                UtilisateurService.getInstance().changeRoleUser("down",id);
            } catch (SQLException | UserAlreadyDownException | UserAlreadyAdminException e) {
                e.printStackTrace();
            }
        }
        if(request.getParameter("up")!=null){
            int index = Integer.parseInt(request.getParameter("up"));
            int id = listOfUsers.get(index).getId();
            System.out.println("Promouvoir: " + (id));
            try {
                UtilisateurService.getInstance().changeRoleUser("up",id);
            } catch (SQLException | UserAlreadyDownException | UserAlreadyAdminException e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect("gestionuser");
    }*/
}
