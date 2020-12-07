package controller;

import entity.Film;
import exception.FilmAlreadyActiveException;
import exception.FilmAlreadyDesactiveException;
import exception.FilmNotFoundException;
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
import java.util.List;

@WebServlet("/admin/gestionfilm")
public class GestionFilmServlet extends ServletGenerique {
    private static final long serialVersionUID = 1L;
    static final Logger LOGGER = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("Loading gestion film");
        WebContext context = new WebContext(req, resp, req.getServletContext());
        List<Film> listOfFilms = FilmService.getInstance().listFilms("valide");
        context.setVariable("listFilms", listOfFilms);
        TemplateEngine engine = createTemplateEngine(req.getServletContext());
        engine.process("listfilms", context, resp.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Film> listOfFilms = FilmService.getInstance().listFilms("valide");
        if(request.getParameter("supp")!=null) {
            int index = Integer.parseInt(request.getParameter("supp"));
            int id = listOfFilms.get(index).getId();
            LOGGER.info("Trying to delete film: " + (id));
            try {
                FilmService.getInstance().deleteFilm(id);
            } catch (FilmNotFoundException e) {
                LOGGER.error("Could not delete film "+id);
                e.printStackTrace();
            }
        }
        if(request.getParameter("active")!=null){
            int index = Integer.parseInt(request.getParameter("active"));
            int id = listOfFilms.get(index).getId();
            LOGGER.info("Validating film: " + (id));
            try {
                FilmService.getInstance().activeFilm(id);
            } catch (FilmNotFoundException | FilmAlreadyActiveException e) {
                LOGGER.error("Could not activate film "+id);
                e.printStackTrace();
            }
        }
        if(request.getParameter("desactive")!=null){
            int index = Integer.parseInt(request.getParameter("desactive"));
            int id = listOfFilms.get(index).getId();
            LOGGER.info("Deactivating film: " + (id));
            try {
                FilmService.getInstance().desactiveFilm(id);
            } catch (FilmNotFoundException | FilmAlreadyDesactiveException e) {
                LOGGER.error("could not deactivating film: " + (id));
                e.printStackTrace();
            }
        }
        LOGGER.debug("Reloading current page");
        response.sendRedirect("gestionfilm");
    }
}
