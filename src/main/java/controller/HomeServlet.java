package controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Utilisateur;
import exception.FilmNotFoundException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import entity.Film;
import service.FilmService;

@WebServlet("/accueil")
public class HomeServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//HttpServletRequest httpRequest = (HttpServletRequest) req;
		
		String trier = req.getParameter("trier");
		List<Film> listOfFilms = FilmService.getInstance().listFilms(trier);
        WebContext context = new WebContext(req, resp, req.getServletContext());

		context.setVariable("listFilms", listOfFilms);
		context.setVariable("trieSelect", trier);
        TemplateEngine engine = createTemplateEngine(req.getServletContext());
        engine.process("accueil", context, resp.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println(request.getParameter("trier"));
		String trie = request.getParameter("trier");
		Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("utilisateurConnecte");
		List<Film> listOfFilms = FilmService.getInstance().listFilms(trie);
		if(utilisateur != null){
			if(request.getParameter("addfavori")!=null) {
				int index = Integer.parseInt(request.getParameter("addfavori"));
				int idFilm = listOfFilms.get(index).getId();
				try {
					FilmService.getInstance().addFavori(idFilm, utilisateur.getId());
				} catch (FilmNotFoundException e) {
					e.printStackTrace();
				}
			}
			if(request.getParameter("suppfavori")!=null) {
				int index = Integer.parseInt(request.getParameter("suppfavori"));
				int idFilm = listOfFilms.get(index).getId();
				try {
					FilmService.getInstance().suppFavori(idFilm, utilisateur.getId());
				} catch (FilmNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		//response.sendRedirect("accueil");
		doGet(request,response);
	}

}
