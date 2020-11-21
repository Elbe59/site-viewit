package service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class StockageImageService {
	
	private static final Path chemin = Paths.get("./HEI/HEI4/ProjetS7/Projet/site-viewit/src/main/webapp/images/film"); //Chemin Kylian
	
	private static class StockageImageHolder {
		private final static StockageImageService instance = new StockageImageService();
	}

	public static StockageImageService getInstance() {
		return StockageImageHolder.instance;
	}
	
	public void addImage(File img) {

		System.out.println(chemin.getFileName());
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(chemin)){
			for (Path entry : stream) {
				System.out.println(entry);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void deleteImage(String nom) {
		
	}
	
}
