package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Film;
import exception.FilmNotFoundException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import service.FilmService;

@WebServlet("/film")
public class DetailFilmServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;
       
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idStr = req.getParameter("id");
		int filmId = -1;
		try {
			filmId = Integer.parseInt(idStr);
		} catch (NumberFormatException nfe) {
			System.err.println(String.format("Parametre incorrect: %s", idStr));
		}
		TemplateEngine engine = createTemplateEngine(req.getServletContext());
		FilmService service = FilmService.getInstance();
		WebContext context = new WebContext(req, resp, req.getServletContext());
		try {
			if(filmId != -1 && service.getFilm(filmId) != null) {
				context.setVariable("film", service.getFilm(filmId));
				engine.process("film", context, resp.getWriter());
			} else {
				System.out.println("Film non trouve: "+ filmId);
				resp.sendRedirect("/accueil");
			}
		} catch (FilmNotFoundException e) {
			e.printStackTrace();
		}
	}

}
