package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.FilmDto;
import entity.Utilisateur;
import service.FilmService;

@WebServlet("/list")
public class ListFilmServlet extends ServletGenerique {
	private static final long serialVersionUID = 1L;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<FilmDto> listFilm = new ArrayList<FilmDto>();
		Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateurConnecte");
		if (utilisateur!=null){
			listFilm = FilmService.getInstance().listFilmsDto(utilisateur.getId());
		} else {
			listFilm = FilmService.getInstance().listFilmsDto(0);
		}
        String filmJson = MAPPER.writeValueAsString(listFilm);
        resp.getWriter().print(filmJson);
    }
	
}