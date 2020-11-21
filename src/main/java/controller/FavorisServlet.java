package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import entity.Film;
import entity.Utilisateur;
import exception.FilmNotFoundException;
import service.FilmService;

@WebServlet("/user/favoris")
public class FavorisServlet extends ServletGenerique {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());
        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateurConnecte");
        List<Film> listOfFilms = null;
		if(utilisateur != null){
			try {
				listOfFilms = FilmService.getInstance().getFilmByUtilisateur(utilisateur.getId());
				context.setVariable("listFilms", listOfFilms);
				TemplateEngine engine = createTemplateEngine(req.getServletContext());
				engine.process("favoris", context, resp.getWriter());
			} catch (FilmNotFoundException e) {
				e.printStackTrace();
			}
		}
		//resp.sendRedirect("accueil");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
}
