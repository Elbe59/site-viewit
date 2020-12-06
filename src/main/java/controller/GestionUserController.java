package controller;


import entity.Utilisateur;
import entity.UtilisateurDto;
import exception.UserAlreadyAdminException;
import exception.UserAlreadyDownException;
import exception.UserAlreadyExistingException;
import exception.UserNotFoundException;
import service.UtilisateurService;
import utils.MotDePasseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/admin/gestionuser")
public class GestionUserController {
    @Context
    ServletContext servletContext;
    static final Logger LOGGER = LogManager.getLogger();

    @GET
    @Path("/listuser")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UtilisateurDto>  listUsers(){
        LOGGER.debug("loading list of users in page gestion user");
        return UtilisateurService.getInstance().listUsersDto();
    }


    @PATCH
    @Path("/{userid}/change")
    public Response listUsers(@PathParam("userid") Integer userId){
        LOGGER.info("Changing role of user "+userId);
        try {
            Utilisateur user = UtilisateurService.getInstance().getUser(userId);
            if(!user.isAdmin()){
                UtilisateurService.getInstance().changeRoleUser("up",userId);
                LOGGER.debug("made user admin");
            }
            else{
                UtilisateurService.getInstance().changeRoleUser("down",userId);
                LOGGER.debug("made user normal");
            }
            return Response.status(201).entity("").build();
        } catch (UserNotFoundException | UserAlreadyDownException | UserAlreadyAdminException | SQLException e) {
            LOGGER.error("Coud not modify role of user "+userId);
            e.printStackTrace();
            return Response.status(409).entity("").build();
        }
    }

    @PATCH
    @Path("/{userid}/modif")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response listUsers(@PathParam("userid") Integer userId,@FormParam("new_email") String email,@FormParam("new_name") String prenom,@FormParam("new_surname") String nom,@FormParam("new_password") String new_password,@FormParam("previous_password") String previous_password){
        LOGGER.info("Modifying info of user "+userId);
        try {
            Utilisateur utilisateur = UtilisateurService.getInstance().getUser(userId);
            if(!MotDePasseUtils.validerMotDePasse(previous_password,utilisateur.getMdpHash())){
                LOGGER.info("password incorrect");
                return Response.status(405).entity("").build();
            }
            else{
                if(new_password.length()>=7){
                    utilisateur.setMdp(new_password);
                    utilisateur.setMdpHash(new_password);
                    LOGGER.debug("changed password");
                }
                else  {
                    utilisateur.setMdp(previous_password);
                    utilisateur.setMdpHash(previous_password);
                    LOGGER.debug("could not change password");
                }
                utilisateur.setEmail(email);
                utilisateur.setNom(nom);
                utilisateur.setPrenom(prenom);
                UtilisateurService.getInstance().modifyUser(utilisateur);
                LOGGER.debug("email name and surname registered");
                return Response.status(201).entity("").build();
            }
        } catch (UserNotFoundException  | SQLException  e) {
            LOGGER.error("could not register infos");
            e.printStackTrace();
            return Response.status(409).entity("").build();
        } catch (UserAlreadyExistingException e) {
            LOGGER.error("could not register infos");
            e.printStackTrace();
        	return Response.status(404).entity("").build();
		} 
    }

    @DELETE
    @Path("/{userId}")
   // @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userId") Integer id){
        LOGGER.info("deleting user "+id);
        try {
            UtilisateurService.getInstance().deleteUser(id);
            LOGGER.debug("account deleted");
            return Response.status(201).entity("").build();
        } catch (UserNotFoundException | SQLException e) {
            LOGGER.error("could not delete this account");
            return Response.status(409).entity("").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response listUsers(@FormParam("email") String email,@FormParam("name") String prenom,@FormParam("surname") String nom,@FormParam("password") String password){
        Utilisateur user=new Utilisateur(1,prenom,nom,email,password,password,false);
        LOGGER.info("Adding new user "+email);
        try {
            UtilisateurService.getInstance().addUser(user);
            LOGGER.debug("user added");
            return Response.status(201).entity("").build();
        } catch (UserAlreadyExistingException e) {
            LOGGER.error("could not create user "+email);
            return Response.status(409).entity("").build();
        }
    }
}

