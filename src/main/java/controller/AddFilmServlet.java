package controller;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import entity.Genre;
import service.FilmService;

@WebServlet("/user/ajoutfilm")
public class AddFilmServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;
 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("genres", FilmService.getInstance().listGenre());
        System.out.println("ok1");
        TemplateEngine engine = createTemplateEngine(req.getServletContext());
        engine.process("ajoutFilm", context, resp.getWriter());
        System.out.println("ok2");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("ok3");
		String titre = req.getParameter("titre");
		String resume = req.getParameter("resume");
		String dateSortieStr = req.getParameter("dateSortie");
		int duree = Integer.parseInt(req.getParameter("duree"));
		String realisateur = req.getParameter("realisateur");
		String acteur = req.getParameter("acteur");
		String imageName = req.getParameter("image");
		String urlBA = req.getParameter("url");
		String genre = req.getParameter("genre");
		
		System.out.println("Titre : " + titre);
		System.out.println("resume : " + resume);
		System.out.println("dateSortie : " + dateSortieStr);
		System.out.println("duree : " + duree);
		System.out.println("realisateur : " + realisateur);
		System.out.println("acteur : " + acteur);
		System.out.println("imageName : " + imageName);
		System.out.println("urlBA : " + urlBA);
		System.out.println("genre : " + genre);
		
		resp.sendRedirect("ajoutFilm");
	}

}
