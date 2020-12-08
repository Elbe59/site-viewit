package controller;

import java.io.*;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import entity.Genre;
import entity.Utilisateur;
import exception.FileStorageException;
import exception.FilmAlreadyExistingException;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import service.FileStorageService;
import service.FilmService;

@MultipartConfig
@WebServlet("/user/ajoutfilm")
public class AddFilmServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;
	private FilmService filmService = FilmService.getInstance();
	static final Logger LOGGER = LogManager.getLogger();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet add film, building the web page");
		WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable("genres", filmService.listGenre());
        TemplateEngine engine = createTemplateEngine(request.getServletContext());
        engine.process("ajoutFilm", context, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("Send form to add a film");
		List<Genre> listGenre=FilmService.getInstance().listGenre();
		String titre = request.getParameter("titre");
		LOGGER.debug("title is "+titre);
		String resume = request.getParameter("resume");
		String dateSortieStr = request.getParameter("dateSortie");
		int duree = Integer.parseInt(request.getParameter("duree"));
		String realisateur = request.getParameter("realisateur");
		String acteur = request.getParameter("acteur");
		int genreIndex = Integer.parseInt(request.getParameter("genre"));
		Genre genre1=listGenre.get(genreIndex);
		Utilisateur utilisateur= (Utilisateur) request.getSession().getAttribute("utilisateurConnecte");
		String fileNameForStorage = "filmInconnu.jpg";
		if(utilisateur.isAdmin()){
			LOGGER.debug("Film add by admin "+utilisateur.getEmail());
			Part part = request.getPart("fichier");
			String extension= FilenameUtils.getExtension(part.getSubmittedFileName());
			InputStream in=part.getInputStream();
			try {
				fileNameForStorage = FileStorageService.getInstance().storeFile(titre,in,extension);
			} catch (FileStorageException e) {
				e.printStackTrace();
			}
			String urlBA = request.getParameter("url");
			try {
				FilmService.getInstance().addFilm(titre,resume,dateSortieStr,duree,realisateur,acteur,fileNameForStorage,urlBA,genre1);
			} catch (FilmAlreadyExistingException e) {
				LOGGER.error("Could not add film");
				e.printStackTrace();
			}
		}
		else {
			LOGGER.debug("Film add by normal user "+utilisateur.getEmail());
			try {
				FilmService.getInstance().addFilm(titre,resume,dateSortieStr,duree,realisateur,acteur,fileNameForStorage,"",genre1);
			} catch (FilmAlreadyExistingException e) {
				LOGGER.debug("Could not add film");
				e.printStackTrace();
			}
		}
		LOGGER.debug("redirecting user to home page");
		response.sendRedirect("../accueil");
	}
}
