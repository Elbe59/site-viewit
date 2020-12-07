package controller;

import entity.GenreDto;
import exception.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import service.FilmService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    static final Logger LOGGER = LogManager.getLogger();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("Loading gestion genres");
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
            LOGGER.info("Trying to delete genre: " + (id));
            try {
                FilmService.getInstance().deleteGenre(id,nbFilmLie);
            } catch (SQLException | GenreNotFoundException | GenreLinkToFilmException e) {
                LOGGER.error("error while deleting genre "+id);
                e.printStackTrace();
            }
        }
        if(request.getParameter("enregistrer")!=null) {
            String new_genre = request.getParameter("new_genre");

            LOGGER.info("Adding new genre "+new_genre);
            try {
                FilmService.getInstance().addGenre(new_genre);
            } catch (GenreAlreadyExistingException throwables) {
                LOGGER.error("error while adding genre "+new_genre);
                throwables.printStackTrace();
            }
        }
        LOGGER.debug("Reloading current page");
        response.sendRedirect("gestiongenre");
    }
}
