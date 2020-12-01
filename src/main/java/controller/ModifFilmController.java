package controller;

import entity.Film;
import exception.FilmNotFoundException;
import service.FilmService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/ws/admin/modification_films")
public class ModifFilmController {

    FilmService filmService = FilmService.getInstance();

    @GET
    @Path("/{title}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Film getValueFilm(@PathParam("title") String title) throws FilmNotFoundException {
        Film film = filmService.getFilmByTitle(title);
        return film;
    }
}
