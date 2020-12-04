package controller;


import entity.Utilisateur;
import entity.UtilisateurDto;
import exception.UserAlreadyAdminException;
import exception.UserAlreadyDownException;
import exception.UserAlreadyExistingException;
import exception.UserNotFoundException;
import service.UtilisateurService;
import utils.MotDePasseUtils;

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


    @GET
    @Path("/listuser")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UtilisateurDto>  listUsers(){
        return UtilisateurService.getInstance().listUsersDto();
    }


    @PATCH
    @Path("/{userid}/change")
    public Response listUsers(@PathParam("userid") Integer userId){
        try {
            Utilisateur user = UtilisateurService.getInstance().getUser(userId);
            if(!user.isAdmin()){
                UtilisateurService.getInstance().changeRoleUser("up",userId);
            }
            else{
                UtilisateurService.getInstance().changeRoleUser("down",userId);
            }
            return Response.status(201).entity("").build();
        } catch (UserNotFoundException | UserAlreadyDownException | UserAlreadyAdminException | SQLException e) {
            e.printStackTrace();
            return Response.status(409).entity("").build();
        }
    }

    @PATCH
    @Path("/{userid}/modif")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response listUsers(@PathParam("userid") Integer userId,@FormParam("new_email") String email,@FormParam("new_name") String prenom,@FormParam("new_surname") String nom,@FormParam("new_password") String new_password,@FormParam("previous_password") String previous_password){
        System.out.println("cc je suis rentré");
        try {
            Utilisateur utilisateur = UtilisateurService.getInstance().getUser(userId);
            if(!MotDePasseUtils.validerMotDePasse(previous_password,utilisateur.getMdpHash())){
                return Response.status(405).entity("").build();
            }
            else{
                if(new_password.length()>=7){
                    utilisateur.setMdp(new_password);
                    utilisateur.setMdpHash(new_password);
                    System.out.println("Mdp chnge");
                }
                else  {
                    utilisateur.setMdp(previous_password);
                    utilisateur.setMdpHash(previous_password);
                    System.out.println("Mdp not chnge");
                }
                utilisateur.setEmail(email);
                utilisateur.setNom(nom);
                utilisateur.setPrenom(prenom);
                UtilisateurService.getInstance().modifyUser(utilisateur);
                return Response.status(201).entity("").build();
            }
        } catch (UserNotFoundException  | SQLException  e) {
            return Response.status(409).entity("").build();
        } catch (UserAlreadyExistingException e) {
        	return Response.status(404).entity("").build();
		} 
    }

    @DELETE
    @Path("/{userId}")
   // @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userId") Integer id){
        try {
            UtilisateurService.getInstance().deleteUser(id);
            return Response.status(201).entity("").build();
        } catch (UserNotFoundException | SQLException e) {
            return Response.status(409).entity("").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response listUsers(@FormParam("email") String email,@FormParam("name") String prenom,@FormParam("surname") String nom,@FormParam("password") String password){
        Utilisateur user=new Utilisateur(1,prenom,nom,email,password,password,false);
        try {
            UtilisateurService.getInstance().addUser(user);
            return Response.status(201).entity("").build();
        } catch (UserAlreadyExistingException e) {
            return Response.status(409).entity("").build();
        }
    }
}

