package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import dao.FilmDao;
import dao.UtilisateurDao;
import dao.impl.FilmDaoImpl;
import dao.impl.UtilisateurDaoImpl;
import entity.Film;
import entity.Genre;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dao.impl.DataSourceProvider;
import dao.impl.FileStorageProvider;
import exception.FileStorageException;
import exception.FilmNotFoundException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import tools.FileManager;

@RunWith(MockitoJUnitRunner.class)
public class FileStorageServiceTestCase {

	@InjectMocks
	FileStorageService fileStorageService;
	@Mock
	FilmDao filmDao=new FilmDaoImpl();
	
	private static final File dataDirectory = new File("data/filmImages");
    private static File tmpDirectory;
	
	@BeforeClass
    public static void createOnceBeforeClass() throws IOException {
    	tmpDirectory = FileManager.createTmpDirectory();
    }
	
	 /*@Before
	 public void createBeforeTestMethod() throws IOException, SQLException {
	    FileManager.duplicateDataToDirectory(dataDirectory, tmpDirectory);
	    if(fileService == null) {
	    	fileService = fileService.getInstance();
	    }
	    try (Connection co = DataSourceProvider.getDataSource().getConnection();
	             Statement stm = co.createStatement()) {
	            stm.executeUpdate("DELETE FROM film");
	            stm.executeUpdate("DELETE FROM genre");
	            stm.executeUpdate("INSERT INTO genre (idGenre, nomGenre) "
	                    + "VALUES (1,'Aventure'), "
	                    +"(2,'Action'),"
	                    +"(3,'Horreur');");
	            stm.executeUpdate(
	                    "INSERT INTO film(idFilm, titreFilm, resumeFilm, dateSortie, dureeFilm, realisateur, acteur, imgFilm, urlBA, idGenre, valide) "
	                            + "VALUES (1, 'titre 1', 'resume 1', '2020-11-11', 123, 'realisateur 1', 'acteur 1', 'image1.png', 'youtu.be/1', 1, 1),"
	                            + "(2, 'titre 2', 'resume 2', '2020-11-12', 123, 'realisateur 2', 'acteur 2', 'image2.test', 'youtu.be/2', 2, 0),"
	                            + "(3, 'titre 3', 'resume 3', '2020-11-12', 123, 'realisateur 3', 'acteur 3', 'notHere.png', 'youtu.be/3', 3, 0);");
	        }
	 }*/

	@Test
	public void shouldStoreImageAndReturnFileName() throws FileStorageException {
		//GIVEN
		String fileName1 = "image1.png";
		String fileName2 = "image2.jpeg";
		String fileName3 = "image3.jpg";
		//WHEN
		String result1 = fileStorageService.storeFile("image1", InputStream.nullInputStream(), "png");
		String result2 = fileStorageService.storeFile("image2", InputStream.nullInputStream(), "jpeg");
		String result3 = fileStorageService.storeFile("image3", InputStream.nullInputStream(), "jpg");
		//THEN
		Assertions.assertThat(result1).isEqualTo(fileName1);
		Assertions.assertThat(result2).isEqualTo(fileName2);
		Assertions.assertThat(result3).isEqualTo(fileName3);
	}
	
	@Test
	public void shouldStoreImageAndReturnFileNameButWrongExtension() throws FileStorageException {
		//GIVEN
		//WHEN
		String result = fileStorageService.storeFile("hulk", InputStream.nullInputStream(), "exe");
		//THEN
		Assertions.assertThat(result).isEqualTo("");
	}
	
	@Test
	public void shouldStoreImageAndReturnFileNameButThrowFileStorageException() throws FileStorageException {
		//GIVEN
		
		//WHEN
		try {
			String result = fileStorageService.storeFile("tho..r", InputStream.nullInputStream(), "jpg");
		}
		//THEN
		catch(FileStorageException e) {
			Assertions.assertThat(e).isExactlyInstanceOf(FileStorageException.class);
		}
	}
	
	@Test
	public void shouldDeleteImageAndReturnFileName() throws FileStorageException {
		//GIVEN
		String fileName = "test1.jpg";	
		//WHEN
		String result = fileStorageService.deleteFile(fileName);
		//THEN
		Assertions.assertThat(result).isEqualTo(fileName);
	}
	
	@Test
	public void shouldDisplayImageAndReturnFileInputStream() throws IOException, FilmNotFoundException {
		int filmId = 3;
		Film film1 = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", new Genre(1,"Aventure"), 1);
		String pathToImage = FileStorageProvider.getUploadDir();
		Mockito.when(filmDao.getFilm(Mockito.anyInt())).thenReturn(film1);
		FileInputStream image = new FileInputStream(pathToImage + "/image1.png");
		//WHEN
		FileInputStream result = fileStorageService.displayImage(filmId);

		//THEN
		Assertions.assertThat(result).hasSameContentAs(image);
		Mockito.verify(filmDao,Mockito.times(1)).getFilm(Mockito.any());
	}

	@Test
	public void shouldDisplayImageButThrowFilmNotFoundException() throws IOException, FilmNotFoundException {
		int filmId = 3;
		Film film1 = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", new Genre(1,"Aventure"), 1);
		String pathToImage = FileStorageProvider.getUploadDir();
		FileInputStream image = new FileInputStream(pathToImage + "/image1.png");
		Mockito.when(filmDao.getFilm(Mockito.anyInt())).thenThrow(new FilmNotFoundException("Ce film n'existe pas"));

		//WHEN
		FileInputStream result = fileStorageService.displayImage(filmId);

		//THEN
		Assertions.assertThat(result).isNull();
		Mockito.verify(filmDao,Mockito.times(1)).getFilm(Mockito.any());
	}

	@Test
	public void shouldDisplayImageAndReturnFileInputStreamButThrowFilmNotFoundException() throws FilmNotFoundException, IOException {
		//GIVEN
		int filmId = 3;
		Film film1 = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image5.png", "youtube.com/1", new Genre(1,"Aventure"), 1);
		String pathToImage = FileStorageProvider.getUploadDir();
		Mockito.when(filmDao.getFilm(Mockito.anyInt())).thenReturn(film1);
		//WHEN
		FileInputStream result = fileStorageService.displayImage(filmId);
		
		//THEN
		Assertions.assertThatExceptionOfType(FileNotFoundException.class);
		Assertions.assertThat(result).isNull();
		Mockito.verify(filmDao,Mockito.times(1)).getFilm(Mockito.any());
	}
	@Test
	public void shouldDisplayUnknownImage() throws FilmNotFoundException, IOException {
		//GIVEN
		int filmId = 3;
		Film film1 = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "imag5", "youtube.com/1", new Genre(1,"Aventure"), 1);
		String pathToImage = FileStorageProvider.getUploadDir();
		FileInputStream image = new FileInputStream(pathToImage + "/filmInconnu.jpg");
		Mockito.when(filmDao.getFilm(Mockito.anyInt())).thenReturn(film1);
		//WHEN
		FileInputStream result = fileStorageService.displayImage(filmId);

		//THEN
		Assertions.assertThat(result).hasSameContentAs(image);
		Mockito.verify(filmDao,Mockito.times(1)).getFilm(Mockito.any());
	}

    //Après
    @After
    public void createAfterTestMethod() {
    	FileManager.removeDataInDirectory(tmpDirectory);
    }
    
    @AfterClass
    public static void createOnceAfterClass() throws IOException {
    	FileManager.removeDirectory(tmpDirectory);
    }
	
}
