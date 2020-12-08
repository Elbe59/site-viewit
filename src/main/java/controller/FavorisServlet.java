package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import exception.UserNotFoundException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import entity.Film;
import entity.Utilisateur;
import exception.FilmNotFoundException;
import service.FilmService;

@WebServlet("/user/favoris")
public class FavorisServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;
	static final Logger LOGGER = LogManager.getLogger();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WebContext context = new WebContext(request, response, request.getServletContext());
		Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("utilisateurConnecte");
		String trier = request.getParameter("trier");
		List<Film> listOfFilms = null;
		if(utilisateur != null){
			LOGGER.debug("loading page favoris of user "+utilisateur.getEmail());
			try {
				listOfFilms = FilmService.getInstance().listFavorisFilm(utilisateur.getId(), trier);
				context.setVariable("listUser", listOfFilms);

			} catch (UserNotFoundException e) {
				LOGGER.error("User not found");
				e.printStackTrace();
			}
		} else {
			LOGGER.error("Vous n'êtes pas connecté");
		}
		TemplateEngine engine = createTemplateEngine(request.getServletContext());
		engine.process("favoris", context, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("utilisateurConnecte");
		String trier = request.getParameter("trier");
		try {
			if(utilisateur != null){
				List<Film> listOfFilms = FilmService.getInstance().listFavorisFilm(utilisateur.getId(), trier);
				if(request.getParameter("suppfavori")!=null) {
					int index = Integer.parseInt(request.getParameter("suppfavori"));
					int idFilm = listOfFilms.get(index).getId();
					FilmService.getInstance().suppFavori(idFilm, utilisateur.getId());
				}
			}
		} catch (UserNotFoundException | FilmNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		doGet(request, response);
	}	
}
