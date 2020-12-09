package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Utilisateur;
import exception.FilmNotFoundException;
import exception.UserNotFoundException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import entity.Film;
import service.FilmService;

@WebServlet("/accueil")
public class HomeServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;
	static final Logger LOGGER = LogManager.getLogger(HomeServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("Loading home page");
		String trier = request.getParameter("trier");
		List<Film> listOfFilms = FilmService.getInstance().listFilms(trier);
        WebContext context = new WebContext(request, response, request.getServletContext());

		context.setVariable("listFilms", listOfFilms);
		context.setVariable("trieSelect", trier);
        TemplateEngine engine = createTemplateEngine(request.getServletContext());
        engine.process("accueil", context, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String trie = request.getParameter("trier");
		Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("utilisateurConnecte");
		List<Film> listOfFilms = FilmService.getInstance().listFilms(trie);
		if(utilisateur != null){
			if(request.getParameter("addfavori")!=null) {
				int index = Integer.parseInt(request.getParameter("addfavori"));
				int idFilm = listOfFilms.get(index).getId();
				LOGGER.info("adding film "+idFilm+" to favoris of user "+utilisateur.getId());
				try {
					FilmService.getInstance().addFavori(idFilm, utilisateur.getId());
				} catch (FilmNotFoundException | UserNotFoundException e) {
					LOGGER.error("could not add film to favoris");
					e.printStackTrace();
				}
			}
			if(request.getParameter("suppfavori")!=null) {
				int index = Integer.parseInt(request.getParameter("suppfavori"));
				int idFilm = listOfFilms.get(index).getId();
				LOGGER.info("deleting film "+idFilm+" to favoris of user "+utilisateur.getId());
				try {
					FilmService.getInstance().suppFavori(idFilm, utilisateur.getId());
				} catch (FilmNotFoundException | SQLException | UserNotFoundException e) {
					LOGGER.error("could not delete film from favoris");
					e.printStackTrace();
				}
			}
		}
		doGet(request,response);
	}
}
