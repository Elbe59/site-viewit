package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exception.FilmNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import service.FilmService;

@WebServlet("/film")
public class DetailFilmServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;
	static final Logger LOGGER = LogManager.getLogger();
    
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idStr = req.getParameter("id");
		int filmId = -1;
		try {
			filmId = Integer.parseInt(idStr);
			LOGGER.debug("loading film nb "+filmId);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Parametre incorrect: "+ idStr);
		}
		TemplateEngine engine = createTemplateEngine(req.getServletContext());
		FilmService service = FilmService.getInstance();
		WebContext context = new WebContext(req, resp, req.getServletContext());
		try {
			if(filmId != -1 && service.getFilm(filmId) != null) {
				LOGGER.debug("film loaded");
				context.setVariable("film", service.getFilm(filmId));
				engine.process("detailFilmws", context, resp.getWriter());
			} else {
				LOGGER.error("Film non trouve: "+ filmId);
				resp.sendRedirect("/accueil");
			}
		} catch (FilmNotFoundException e) {
			e.printStackTrace();
		}
	}
}
