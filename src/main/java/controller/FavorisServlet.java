package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exception.UserNotFoundException;
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
				listOfFilms = FilmService.getInstance().listFavorisFilm(utilisateur.getId());
				context.setVariable("listUser", listOfFilms);

			} catch (FilmNotFoundException | UserNotFoundException e) {
				e.printStackTrace();
			}
		}
		TemplateEngine engine = createTemplateEngine(req.getServletContext());
		engine.process("favoris", context, resp.getWriter());
		//resp.sendRedirect("accueil");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("utilisateurConnecte");
		try {
			List<Film> listOfFilms = FilmService.getInstance().listFavorisFilm(utilisateur.getId());
			if(utilisateur != null){
				if(request.getParameter("suppfavori")!=null) {
					int index = Integer.parseInt(request.getParameter("suppfavori"));
					int idFilm = listOfFilms.get(index).getId();
					FilmService.getInstance().suppFavori(idFilm, utilisateur.getId());
				}
			}
		} catch (UserNotFoundException | FilmNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		response.sendRedirect("favoris");
	}
	
}
