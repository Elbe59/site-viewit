package service;
import dao.FilmDao;
import dao.impl.FileStorageProvider;
import dao.impl.FilmDaoImpl;
import entity.Film;
import exception.FileStorageException;
import exception.FilmNotFoundException;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileStorageService {

    static final Logger LOGGER = LogManager.getLogger();
    private FilmDao filmDao = new FilmDaoImpl();

    private static class FileStorageHolder {
        private final static FileStorageService instance = new FileStorageService();
    }

    public static FileStorageService getInstance() {
        return FileStorageService.FileStorageHolder.instance;
    }

    private FileStorageService() {

    }
	
    public String storeFile(String filmTitle, InputStream file,String extension) throws FileStorageException {
        if(!extension.contentEquals("png")&&!extension.contentEquals("jpeg")&&!extension.contentEquals("jpg")){
            return "";
        }
        else{
            Path storageLocation = Paths.get(FileStorageProvider.getUploadDir()).toAbsolutePath().normalize();
            // Normalisation du nom du fichier
            String fileName = filmTitle.replace(" ","");
            fileName = fileName.replace(":","");
            fileName = fileName.replace("!","");
            fileName = fileName.replace("'","")+"."+extension;
            try {
                // Vérification de caractère invalide dans le nom du fichier
                if(fileName.contains("..")) {
                    throw new FileStorageException("Le nom de ce fichier n'est pas valide " + fileName);
                }
                // Copier le fichier dans la targetLocation (Remplace si un même nom existe déjà)
                Path targetLocation = storageLocation.resolve(fileName);
                Files.copy(file, targetLocation, StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            } catch (IOException | FileStorageException ex) {
                throw new FileStorageException("Impossible de stocker le fichier " + fileName + ". Essayez encore !");
            }
        }

    }

    public String deleteFile(String fileName) throws FileStorageException {
        Path storageLocation = Paths.get(FileStorageProvider.getUploadDir()).toAbsolutePath().normalize();
        Path targetLocation = storageLocation.resolve(fileName);
        File file = new File(String.valueOf(targetLocation));
        file.delete();
        return fileName;
    }

    public FileInputStream displayImage(int filmId){
        String pathToImage = FileStorageProvider.getUploadDir();
        FileInputStream image = null;
        try {
            Film film=filmDao.getFilm(filmId);
            String imageName=  film.getImageName();
            if(!FilenameUtils.getExtension(imageName).contentEquals("png")&&!FilenameUtils.getExtension(imageName).contentEquals("jpeg")&&!FilenameUtils.getExtension(imageName).contentEquals("jpg") ){
                image = new FileInputStream(pathToImage + "/filmInconnu.jpg");
            }
            else{
                image = new FileInputStream(pathToImage + "/" + imageName);
            }
        } catch (FilmNotFoundException | FileNotFoundException e) {
            LOGGER.error("Impossible d'accéder à l'image: "+filmId);
            e.printStackTrace();
        }
        return image;
    }
}
