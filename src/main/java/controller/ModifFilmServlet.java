package controller;

import java.io.*;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	static final Logger LOGGER = LogManager.getLogger(HomeServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WebContext context = new WebContext(req, resp, req.getServletContext());
		int id = Integer.parseInt(req.getParameter("id"));
		LOGGER.debug("loading page modif film for film "+id);
		Film filmToModif= null;
		try {
			filmToModif = filmService.getFilm(id);
		} catch (FilmNotFoundException e) {
			LOGGER.error("could not load that film");
			e.printStackTrace();
		}
		filmToModif.setUrlBA("https://www.youtube.com/watch?v="+filmToModif.getUrlBA());
		context.setVariable("genres", filmService.listGenre());
		context.setVariable("id", id);
		context.setVariable("film", filmToModif);
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
				LOGGER.error("error loading the image");
				e.printStackTrace();
			}
		}
		try {
			FilmService.getInstance().updateFilm(idFilm,titre,resume,dateSortieStr,duree,realisateur,acteur,fileNameForStorage,urlBA,genre1);
			LOGGER.info("modified film "+idFilm);
		} catch (FilmNotFoundException e) {
			LOGGER.error("could not update that film");
			e.printStackTrace();
		}
		LOGGER.debug("redirecting user to gestion film");
		resp.sendRedirect("../admin/gestionfilm");
	}
}
