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

	static final Logger LOGGER = LogManager.getLogger();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WebContext context = new WebContext(req, resp, req.getServletContext());
		Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateurConnecte");
		String trier = req.getParameter("trier");
		System.out.println("get : " + trier);
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
		TemplateEngine engine = createTemplateEngine(req.getServletContext());
		engine.process("favoris", context, resp.getWriter());
		//resp.sendRedirect("accueil");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("utilisateurConnecte");
		//String trier = request.getParameter("trier");
		//System.out.println("post : " + trier);
		try {
			List<Film> listOfFilms = FilmService.getInstance().listFavorisFilm(utilisateur.getId(), null);
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
		doGet(request, response);
	}
	
}
