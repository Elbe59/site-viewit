package service;
import dao.impl.FileStorageProvider;
import exception.FileStorageException;
import exception.FilmNotFoundException;

import org.apache.commons.io.FilenameUtils;
import org.thymeleaf.util.StringUtils;


import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileStorageService {
	
	private static class FileStorageHolder {
		private final static FileStorageService instance = new FileStorageService();
	}
	 
	public static FileStorageService getInstance() {
		return FileStorageHolder.instance;
	}
	
    public static String storeFile(String filmTitle, InputStream file,String extension) throws FileStorageException {
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

    public static String deleteFile(String fileName) throws FileStorageException {
        Path storageLocation = Paths.get(FileStorageProvider.getUploadDir()).toAbsolutePath().normalize();
        Path targetLocation = storageLocation.resolve(fileName);
        File file = new File(String.valueOf(targetLocation));
        file.delete();
        System.out.println("Suppression du précédent fichier");
        return fileName;
    }

    public static FileInputStream displayImage(int filmId){
        String pathToImage = FileStorageProvider.getUploadDir();
        FileInputStream image = null;
        try {
            String imageName= FilmService.getInstance().getFilm(filmId).getImageName();
            if(!FilenameUtils.getExtension(imageName).contentEquals("png")&&!FilenameUtils.getExtension(imageName).contentEquals("jpeg")&&!FilenameUtils.getExtension(imageName).contentEquals("jpg") ){
                image = new FileInputStream(pathToImage + "/filmInconnu.jpg");
            }
            else{
                image = new FileInputStream(pathToImage + "/" + imageName);
                System.out.println(pathToImage + "/" + FilmService.getInstance().getFilm(filmId).getImageName());
            }
        } catch (FilmNotFoundException | FileNotFoundException e) {
            return null;
        }
        return image;
    }
}
