package controller;

import entity.Utilisateur;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import service.UtilisateurService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/gestionuser")
public class GestionUserServlet extends ServletGenerique {
    static final Logger LOGGER = LogManager.getLogger();
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("loading gestion user page");
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
}
