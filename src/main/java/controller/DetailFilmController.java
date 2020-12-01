package controller;

import entity.FilmDto;
import entity.Utilisateur;
import entity.UtilisateurDto;
import exception.FilmNotFoundException;
import service.FilmService;
import service.UtilisateurService;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("{userId}/filmdetail/{filmId}")
public class DetailFilmController {
    @Context
    ServletContext servletContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response filmDetail(@PathParam("userId") Integer userId, @PathParam("filmId") Integer filmId){
        List<FilmDto> listFilmsDto = FilmService.getInstance().listFilmsDto(userId);
        for (FilmDto film : listFilmsDto) {
            if(film.getId()==filmId){
                return Response.status(201).entity(film).build();
            }
        }
        return Response.status(409).entity("").build();
    }

    @PATCH
    @Path("/remove/{action}")
    public Response removeAvis(@PathParam("action") String action, @PathParam("filmId") Integer filmId,@PathParam("userId") Integer userId) throws FilmNotFoundException, SQLException {
        if(action.contentEquals("favori")){
            FilmService.getInstance().suppFavori(filmId, userId);
        }
        else if(action.contentEquals("like")){
            FilmService.getInstance().removeAvis(filmId,userId);
        }
        else if(action.contentEquals("dislike")){
            FilmService.getInstance().removeAvis(filmId,userId);
        }
        else {
            return Response.status(409).entity("").build();
        }
        return Response.status(201).entity("").build();
    }

    @PATCH
    @Path("/add/{action}")
    public Response addAvis(@PathParam("action") String action, @PathParam("filmId") Integer filmId,@PathParam("userId") Integer userId) throws FilmNotFoundException {
        if(action.contentEquals("favori")){
            FilmService.getInstance().addFavori(filmId, userId);
        }
        else if(action.contentEquals("like")){
            FilmService.getInstance().addLike(filmId,userId);
        }
        else if(action.contentEquals("dislike")){
            FilmService.getInstance().addDislike(filmId,userId);
        }
        else {
            return Response.status(409).entity("").build();
        }
        return Response.status(201).entity("").build();
    }
}
