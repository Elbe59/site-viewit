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

@MultipartConfig
@WebServlet("/user/ajoutfilm")
public class AddFilmServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;
	private FilmService filmService = FilmService.getInstance();
	static final Logger LOGGER = LogManager.getLogger();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("genres", filmService.listGenre());
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
		int genreIndex = Integer.parseInt(req.getParameter("genre"));
		Genre genre1=listGenre.get(genreIndex);
		Utilisateur utilisateur= (Utilisateur) req.getSession().getAttribute("utilisateurConnecte");
		String fileNameForStorage = "filmInconnu.jpg";
		//Files.copy(in,Paths.get(FileStorageProvider.getUploadDir()+part.getSubmittedFileName()));
		if(utilisateur.isAdmin()){
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
			} catch (FilmAlreadyExistingException | FilmNotFoundException e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				FilmService.getInstance().addFilm(titre,resume,dateSortieStr,duree,realisateur,acteur,fileNameForStorage,null,genre1);
			} catch (FilmAlreadyExistingException | FilmNotFoundException e) {
				e.printStackTrace();
			}
		}




		//LocalDate dateSortie = formaterDate(dateSortieStr);
		//urlBA = urlBA.substring( urlBA.lastIndexOf( '=' ) + 1 );
		
		//Film film=new Film(1,titre,resume,dateSortie,duree,realisateur,acteur,imageName,urlBA,genre1,0,"rien");



		//System.out.println("urlBA : " + urlBA);
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

		resp.sendRedirect("../user/ajoutfilm");
	}
}
