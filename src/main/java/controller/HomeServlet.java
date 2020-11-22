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
import service.FilmService;

@WebServlet("/accueil")
public class HomeServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) req;

		List<Film> listOfFilms = FilmService.getInstance().listFilms();
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("listFilms", listOfFilms);
        TemplateEngine engine = createTemplateEngine(req.getServletContext());
        engine.process("accueil", context, resp.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
