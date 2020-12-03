package service;
import dao.impl.FileStorageProvider;
import exception.FileStorageException;
import exception.FilmNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.thymeleaf.util.StringUtils;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileStorageService {
    public static String storeFile(String filmTitle, InputStream file,String extension) throws FileStorageException {
        if(!extension.contentEquals("png")&&!extension.contentEquals("jpeg")&&!extension.contentEquals("jpg")){
            return "";
        }
        else{
            Path storageLocation = Paths.get(FileStorageProvider.getUploadDir()).toAbsolutePath().normalize();
            // Normalize file name
            String fileName = filmTitle.replace(" ","");
            fileName = fileName.replace(":","");
            fileName = fileName.replace("!","");
            fileName = fileName.replace("'","")+"."+extension;
            try {
                // Check if the file's name contains invalid characters
                if(fileName.contains("..")) {
                    throw new FileStorageException("Le nom de ce fichier n'est pas valide" + fileName);
                }
                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = storageLocation.resolve(fileName);
                Files.copy(file, targetLocation, StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            } catch (IOException | FileStorageException ex) {
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
            }
        }

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
            e.printStackTrace();
        }
        return image;
    }
}
