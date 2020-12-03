package controller;

import entity.Film;
import entity.Genre;
import entity.GenreDto;
import exception.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import service.FilmService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/gestiongenre")
public class GestionGenreServlet extends ServletGenerique {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());
        List<GenreDto> listOfGenres = FilmService.getInstance().listGenreDto();
        context.setVariable("listGenres", listOfGenres);
        TemplateEngine engine = createTemplateEngine(req.getServletContext());
        engine.process("listgenres", context, resp.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<GenreDto> listOfGenres = FilmService.getInstance().listGenreDto();
        if(request.getParameter("supp")!=null) {
            int index = Integer.parseInt(request.getParameter("supp"));
            int id = listOfGenres.get(index).getId();
            int nbFilmLie = listOfGenres.get(index).getNbFilmLie();
            System.out.println("Supprime genre : " + (id));
            try {
                FilmService.getInstance().deleteGenre(id,nbFilmLie);
            } catch (SQLException | GenreNotFoundException | GenreLinkToFilmException e) {
                e.printStackTrace();
            }
        }
        if(request.getParameter("enregistrer")!=null) {
            String new_genre = request.getParameter("new_genre");

            System.out.println("Ajout genre: " + new_genre);
            try {
                FilmService.getInstance().addGenre(new_genre);
            } catch (GenreAlreadyExistingException | GenreNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
        response.sendRedirect("gestiongenre");
    }
}
