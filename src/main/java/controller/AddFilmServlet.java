package controller;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dao.impl.DataSourceProvider;
import entity.Film;
import exception.FilmAlreadyActiveException;
import exception.FilmAlreadyExistingException;
import exception.FilmNotFoundException;
import org.apache.logging.log4j.core.util.IOUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import entity.Genre;
import service.FilmService;

@MultipartConfig
@WebServlet("/user/ajoutfilm")
public class AddFilmServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;
 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("genres", FilmService.getInstance().listGenre());
        System.out.println(context.toString());
        System.out.println("ok1");
        TemplateEngine engine = createTemplateEngine(req.getServletContext());
        engine.process("ajoutFilm", context, resp.getWriter());
        System.out.println("ok2");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Genre> listGenre=FilmService.getInstance().listGenre();
		System.out.println("ok3");
		String titre = req.getParameter("titre");
		String resume = req.getParameter("resume");
		String dateSortieStr = req.getParameter("dateSortie");
		int duree = Integer.parseInt(req.getParameter("duree"));
		String realisateur = req.getParameter("realisateur");
		String acteur = req.getParameter("acteur");
		String imageName = req.getPart("fichier").getName();
		Part part = req.getPart("fichier");
		InputStream in=part.getInputStream();
		String urlBA = req.getParameter("url");
		int genreIndex = Integer.parseInt(req.getParameter("genre"));

		Genre genre1=listGenre.get(genreIndex);
		//System.out.println("urlBA : " + urlBA);
		LocalDate dateSortie = formaterDate(dateSortieStr);
		urlBA = urlBA.substring( urlBA.lastIndexOf( '=' ) + 1 );
		/*System.out.println("Titre : " + titre);
		System.out.println("resume : " + resume);
		System.out.println("dateSortieStr : " + dateSortieStr);
		System.out.println("dateSortie : " + dateSortie);
		System.out.println("duree : " + duree);
		System.out.println("realisateur : " + realisateur);
		System.out.println("acteur : " + acteur);
		System.out.println("imageName : " + imageName);
		System.out.println("urlBA : " + urlBA);
		System.out.println("genre : " + genre);*/
		Film film=new Film(1,titre,resume,dateSortie,duree,realisateur,acteur,imageName,urlBA,genre1,0,"rien");

		try {
			FilmService.getInstance().addFilm(film,in);
		} catch (FilmAlreadyExistingException e) {
			e.printStackTrace();
		}
		resp.sendRedirect("../user/ajoutfilm");
	}
	
	private LocalDate formaterDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

}
