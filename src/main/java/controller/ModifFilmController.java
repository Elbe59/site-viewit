package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import entity.Film;
import exception.FilmNotFoundException;
import service.FilmService;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/admin/modificationfilms")
public class ModifFilmController {
    @Context
    ServletContext servletContext;

    @GET
    @Path("/{idFilm}")
    @Produces(MediaType.APPLICATION_JSON)
    public Film getValueFilm(@PathParam("idFilm") Integer idFilm) throws FilmNotFoundException, JsonProcessingException {
        Film film = FilmService.getInstance().getFilm(idFilm);
        System.out.println(film.getTitre());
        return film;
    }
}
