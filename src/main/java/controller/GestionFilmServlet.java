package controller;

import entity.Film;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import service.FilmService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/gestionfilm")
public class GestionFilmServlet extends ServletGenerique {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());
        List<Film> listOfFilms = FilmService.getInstance().listFilms("valide");
        context.setVariable("listFilms", listOfFilms);
        TemplateEngine engine = createTemplateEngine(req.getServletContext());
        engine.process("listfilms", context, resp.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Film> listOfFilms = FilmService.getInstance().listFilms("valide");
        int index = Integer.parseInt(request.getParameter("supp"));
        int id=listOfFilms.get(index).getId();
        System.out.println("Delete: "+(id+1));
        //int id=3;
        FilmService.getInstance().deleteFilm(id);
        response.sendRedirect("gestionfilm");
    }
}
