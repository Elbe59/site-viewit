package controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.core.util.IOUtils;
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
		
		LocalDate dateSortie = formaterDate(dateSortieStr);
		
		System.out.println("Titre : " + titre);
		System.out.println("resume : " + resume);
		System.out.println("dateSortieStr : " + dateSortieStr);
		System.out.println("dateSortie : " + dateSortie);
		System.out.println("duree : " + duree);
		System.out.println("realisateur : " + realisateur);
		System.out.println("acteur : " + acteur);
		System.out.println("imageName : " + imageName);
		System.out.println("urlBA : " + urlBA);
		System.out.println("genre : " + genre);
		
		//FilmService.getInstance().addFilm();
		
		resp.sendRedirect("ajoutFilm");
	}
	
	private LocalDate formaterDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	

}
