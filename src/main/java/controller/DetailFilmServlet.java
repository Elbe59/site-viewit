package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Film;
import entity.Utilisateur;
import exception.FilmNotFoundException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import service.FilmService;

@WebServlet("/film")
public class DetailFilmServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;
       
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idStr = req.getParameter("id");
		int filmId = -1;
		try {
			filmId = Integer.parseInt(idStr);
		} catch (NumberFormatException nfe) {
			System.err.println(String.format("Parametre incorrect: %s", idStr));
		}
		TemplateEngine engine = createTemplateEngine(req.getServletContext());
		FilmService service = FilmService.getInstance();
		WebContext context = new WebContext(req, resp, req.getServletContext());
		try {
			if(filmId != -1 && service.getFilm(filmId) != null) {
				context.setVariable("film", service.getFilm(filmId));
				engine.process("detailFilmws", context, resp.getWriter());
			} else {
				System.out.println("Film non trouve: "+ filmId);
				resp.sendRedirect("/accueil");
			}
		} catch (FilmNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/*protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateurConnecte");
		if(utilisateur != null){
			if(req.getParameter("addfavori")!=null) {
				int index = Integer.parseInt(req.getParameter("addfavori"));
				try {
					FilmService.getInstance().addFavori(index, utilisateur.getId());
				} catch (FilmNotFoundException e) {
					e.printStackTrace();
				}
			}
			if(req.getParameter("suppfavori")!=null) {
				int index = Integer.parseInt(req.getParameter("suppfavori"));
				try {
					FilmService.getInstance().suppFavori(index, utilisateur.getId());
				} catch (FilmNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(req.getParameter("addlike")!=null) {
				int index = Integer.parseInt(req.getParameter("addlike"));
				try {
					FilmService.getInstance().addLike(index, utilisateur.getId());
				} catch (FilmNotFoundException e) {
					e.printStackTrace();
				}
			}
			if(req.getParameter("adddislike")!=null) {
				int index = Integer.parseInt(req.getParameter("adddislike"));
				try {
					FilmService.getInstance().addDislike(index, utilisateur.getId());
				} catch (FilmNotFoundException e) {
					e.printStackTrace();
				}
			}
			if(req.getParameter("remove")!=null) {
				int index = Integer.parseInt(req.getParameter("remove"));
				try {
					FilmService.getInstance().removeAvis(index, utilisateur.getId());
				} catch (FilmNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		doGet(req,resp);
	}
	*/

}
