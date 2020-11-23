package controller;

import entity.Film;
import entity.Genre;
import exception.FilmAlreadyDesactiveException;
import exception.FilmNotFoundException;
import exception.GenreNotFoundException;
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
        List<Genre> listOfGenres = FilmService.getInstance().listGenre();
        context.setVariable("listGenres", listOfGenres);
        TemplateEngine engine = createTemplateEngine(req.getServletContext());
        engine.process("listgenres", context, resp.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Genre> listOfGenres = FilmService.getInstance().listGenre();
        if(request.getParameter("supp")!=null){
            int index = Integer.parseInt(request.getParameter("supp"));
            int id = listOfGenres.get(index).getId();
            System.out.println("Supprime genre: " + (id));
            try {
                FilmService.getInstance().deleteGenre(id);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (GenreNotFoundException e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect("gestiongenre");
    }
}
