package controller;

import entity.FilmDto;
import exception.FilmNotFoundException;
import exception.UserNotFoundException;
import service.FilmService;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("{userId}/filmdetail/{filmId}")
public class DetailFilmController {
    @Context
    ServletContext servletContext;

    static final Logger LOGGER = LogManager.getLogger();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response filmDetail(@PathParam("userId") Integer userId, @PathParam("filmId") Integer filmId){
        LOGGER.info("user "+userId+" try to access film "+filmId);
        List<FilmDto> listFilmsDto = FilmService.getInstance().listFilmsDto(userId);
        for (FilmDto film : listFilmsDto) {
            if(film.getId()==filmId){
                LOGGER.debug("Returned film "+film.getTitre());
                return Response.status(201).entity(film).build();
            }
        }
        LOGGER.error("Conflict, film not found");
        return Response.status(409).entity("").build();
    }

    @PATCH
    @Path("/remove/{action}")
    public Response removeAvis(@PathParam("action") String action, @PathParam("filmId") Integer filmId,@PathParam("userId") Integer userId) throws FilmNotFoundException, SQLException, UserNotFoundException {
        LOGGER.info("user "+userId+" use action remove "+action+" on film "+filmId);
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
            LOGGER.error(action+" is not a valid action");
            return Response.status(409).entity("").build();
        }
        return Response.status(201).entity("").build();
    }

    @PATCH
    @Path("/add/{action}")
    public Response addAvis(@PathParam("action") String action, @PathParam("filmId") Integer filmId,@PathParam("userId") Integer userId) throws FilmNotFoundException, UserNotFoundException {
        LOGGER.info("user "+userId+" use action add "+action+" on film "+filmId);
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
            LOGGER.error(action+" is not a valid action");
            return Response.status(409).entity("").build();
        }
        return Response.status(201).entity("").build();
    }
}
