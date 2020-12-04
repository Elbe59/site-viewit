package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Film;
import exception.FilmNotFoundException;
import service.FilmService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/ws/admin/modification_films")
public class ModifFilmController {

    FilmService filmService = FilmService.getInstance();

    @GET
    @Path("/{idFilm}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getValueFilm(@PathParam("idFilm") Integer idFilm) throws FilmNotFoundException, JsonProcessingException {
        Film film = filmService.getFilm(idFilm);
        System.out.println(film.getTitre());
        ObjectMapper mapper = new ObjectMapper();
        String jsonFilm = mapper.writeValueAsString(film);
        return jsonFilm;
    }
}
