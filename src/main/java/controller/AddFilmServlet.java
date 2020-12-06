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
import exception.FilmNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import service.FileStorageService;
import service.FilmService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@MultipartConfig
@WebServlet("/user/ajoutfilm")
public class AddFilmServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;
	private FilmService filmService = FilmService.getInstance();
	static final Logger LOGGER = LogManager.getLogger();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOGGER.debug("doGet add film, building the web page");
		WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("genres", filmService.listGenre());
        TemplateEngine engine = createTemplateEngine(req.getServletContext());
        engine.process("ajoutFilm", context, resp.getWriter());

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOGGER.debug("Send form to add a film");
		List<Genre> listGenre=FilmService.getInstance().listGenre();
		String titre = req.getParameter("titre");
		LOGGER.debug("title is "+titre);
		String resume = req.getParameter("resume");
		String dateSortieStr = req.getParameter("dateSortie");
		int duree = Integer.parseInt(req.getParameter("duree"));
		String realisateur = req.getParameter("realisateur");
		String acteur = req.getParameter("acteur");
		int genreIndex = Integer.parseInt(req.getParameter("genre"));
		Genre genre1=listGenre.get(genreIndex);
		Utilisateur utilisateur= (Utilisateur) req.getSession().getAttribute("utilisateurConnecte");
		String fileNameForStorage = "filmInconnu.jpg";
		//Files.copy(in,Paths.get(FileStorageProvider.getUploadDir()+part.getSubmittedFileName()));
		if(utilisateur.isAdmin()){
			LOGGER.debug("Film add by admin "+utilisateur.getEmail());
			Part part = req.getPart("fichier");
			String extension= FilenameUtils.getExtension(part.getSubmittedFileName());
			InputStream in=part.getInputStream();
			try {
				fileNameForStorage = FileStorageService.storeFile(titre,in,extension);
			} catch (FileStorageException e) {
				e.printStackTrace();
			}
			String urlBA = req.getParameter("url");
			//urlBA = urlBA.substring( urlBA.lastIndexOf( '=' ) + 1 );
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
				FilmService.getInstance().addFilm(titre,resume,dateSortieStr,duree,realisateur,acteur,fileNameForStorage,null,genre1);
			} catch (FilmAlreadyExistingException e) {
				LOGGER.debug("Could not add film");
				e.printStackTrace();
			}
		}
		LOGGER.debug("redirecting user to home page");
		resp.sendRedirect("../accueil");
	}
}
