package controller;

import entity.Film;
import entity.Utilisateur;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import service.FilmService;
import service.UtilisateurService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/gestionuser")
public class GestionUserServlet extends ServletGenerique {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());
        List<Utilisateur> listUser = UtilisateurService.getInstance().listUser();
        context.setVariable("listUsers", listUser);
        TemplateEngine engine = createTemplateEngine(req.getServletContext());
        engine.process("listuser", context, resp.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("gestionuser");
    }
}
