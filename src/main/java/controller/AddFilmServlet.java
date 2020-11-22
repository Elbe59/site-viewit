package controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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

		Part part = req.getPart("fichier");

		String nomFichier=getNomFichier((part));
		if ( nomFichier != null && !nomFichier.isEmpty() ) {
			String nomChamp = part.getName();
            String path = System.getProperty("user.dir");
            System.out.println(path);
            //String chemin
            Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
            Path filePath = Paths.get(root.toString(),"src", "main", "webapp","images","film");
            System.out.println(filePath);
            System.out.println(System.getenv().get("PWD"));


            String envRootDir = System.getProperty("user.dir");
            Path rootDir = Paths.get(".").normalize().toAbsolutePath();
            if ( rootDir.startsWith(envRootDir) ) {
                System.out.println(rootDir);
            } else {
                throw new RuntimeException("Root dir not found in user directory.");
            }

            System.out.println(System.getProperties());

            nomFichier = nomFichier.substring( nomFichier.lastIndexOf( '/' ) + 1 ).substring( nomFichier.lastIndexOf( '\\' ) + 1 );
            part.write(path+nomFichier);
            //fichierForm.write("C:\\temp\\fichier.jpg");
			req.setAttribute( nomChamp, nomFichier );
		}
		/*if(nomFichier !=null && !nomFichier.isEmpty()){
			String nomChamp = part.getName();
			req.setAttribute(nomChamp,nomFichier);
		}*/
        //java.io.tmpdir=C:\Program Files\apache-tomcat-9.0.38\temp
		resp.sendRedirect("../user/ajoutfilm");
	}
	
	private LocalDate formaterDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	private static String getNomFichier( Part part ) {
		/* Boucle sur chacun des paramètres de l'en-tête "content-disposition". */
		for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
			/* Recherche de l'éventuelle présence du paramètre "filename". */
			if ( contentDisposition.trim().startsWith( "filename" ) ) {
				/*
				 * Si "filename" est présent, alors renvoi de sa valeur,
				 * c'est-à-dire du nom de fichier sans guillemets.
				 */
				return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
			}
		}
		/* Et pour terminer, si rien n'a été trouvé... */
		return null;
	}

}
