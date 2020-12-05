package controller;

import java.io.*;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dao.impl.DataSourceProvider;
import entity.Film;
import entity.Utilisateur;
import exception.FileStorageException;
import exception.FilmAlreadyActiveException;
import exception.FilmAlreadyExistingException;
import exception.FilmNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.core.util.IOUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import entity.Genre;
import service.FileStorageService;
import service.FilmService;

@MultipartConfig
@WebServlet("/admin/modifier_film")
public class ModifFilmServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;
	private FilmService filmService = FilmService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WebContext context = new WebContext(req, resp, req.getServletContext());
		int id = Integer.parseInt(req.getParameter("id"));
		context.setVariable("genres", filmService.listGenre());
		context.setVariable("id", id);
		try {
			context.setVariable("film", filmService.getFilm(id));
		} catch (FilmNotFoundException e) {
			e.printStackTrace();
		}
		TemplateEngine engine = createTemplateEngine(req.getServletContext());
		engine.process("modifFilm", context, resp.getWriter());
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer idFilm =Integer.parseInt(req.getParameter("id"));
		Film previousFilm = null;
		try {
			previousFilm = filmService.getFilm(idFilm);
		} catch (FilmNotFoundException e) {
			e.printStackTrace();
		}
		List<Genre> listGenre = FilmService.getInstance().listGenre();
		System.out.println("ok3");
		String titre = req.getParameter("titre");
		String resume = req.getParameter("resume");
		String dateSortieStr = req.getParameter("dateSortie");
		int duree = Integer.parseInt(req.getParameter("duree"));
		String realisateur = req.getParameter("realisateur");
		String acteur = req.getParameter("acteur");
		int genreIndex = Integer.parseInt(req.getParameter("genre"));
		Genre genre1 = listGenre.get(genreIndex);
		Part part = req.getPart("fichier");

		String urlBA = req.getParameter("url");
		//urlBA = urlBA.substring( urlBA.lastIndexOf( '=' ) + 1 );
		String extension = FilenameUtils.getExtension(part.getSubmittedFileName());
		InputStream in = part.getInputStream();
		String fileNameForStorage = "";
		if(part.getSize()==0){
			fileNameForStorage=previousFilm.getImageName();
		}
		else{
			try {
				FileStorageService.deleteFile(previousFilm.getImageName());
				fileNameForStorage = FileStorageService.storeFile(titre, in, extension);
			} catch (FileStorageException e) {
				e.printStackTrace();
			}
		}
		try {
			FilmService.getInstance().updateFilm(idFilm,titre,resume,dateSortieStr,duree,realisateur,acteur,fileNameForStorage,urlBA,genre1);
		} catch (FilmNotFoundException e) {
			e.printStackTrace();
		}

		resp.sendRedirect("../admin/gestionfilm");
	}
}
