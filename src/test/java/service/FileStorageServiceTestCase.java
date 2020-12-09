package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

import dao.FilmDao;
import dao.impl.FilmDaoImpl;
import entity.Film;
import entity.Genre;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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

	/*@Test
	public void shouldStoreImageAndReturnFileName() throws FileStorageException {
		//GIVEN
		String fileName1 = "image1.png";
		//WHEN
		String result1 = fileStorageService.storeFile("image1", InputStream.nullInputStream(), "png");
		//THEN
		Assertions.assertThat(result1).isEqualTo(fileName1);
	}*/
	
	@Test
	public void shouldStoreImageAndReturnFileNameButWrongExtension() throws FileStorageException {
		//GIVEN
		//WHEN
		String result = fileStorageService.storeFile("hulk", InputStream.nullInputStream(), "exe");
		//THEN
		Assertions.assertThat(result).isEqualTo("");
	}
	
	@Test
	public void shouldStoreImageAndReturnFileNameButThrowFileStorageException() {
		//GIVEN
		
		//WHEN
		try {
			fileStorageService.storeFile("tho..r", InputStream.nullInputStream(), "jpg");
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
		@SuppressWarnings("static-access")
		String result = fileStorageService.getInstance().deleteFile(fileName);
		//THEN
		Assertions.assertThat(result).isEqualTo(fileName);
	}
	
	/*@Test
	public void shouldDisplayImageAndReturnFileInputStream() throws IOException, FilmNotFoundException {
		int filmId = 3;
		Film film1 = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image1.png", "youtube.com/1", new Genre(1,"Aventure"), 1);
		String pathToImage = FileStorageProvider.getUploadDir();
		Mockito.when(filmDao.getFilm(Mockito.anyInt())).thenReturn(film1);
		FileInputStream image = new FileInputStream("src/test/resources/dataTest" + "/image1.png");
		//WHEN
		FileInputStream result = fileStorageService.displayImage(filmId);

		//THEN
		Assertions.assertThat(result).hasSameContentAs(image);
		Mockito.verify(filmDao,Mockito.times(1)).getFilm(Mockito.any());
	}*/

	@Test
	public void shouldDisplayImageButThrowFilmNotFoundException() throws FilmNotFoundException {
		int filmId = 3;
		Mockito.when(filmDao.getFilm(Mockito.anyInt())).thenThrow(new FilmNotFoundException("Ce film n'existe pas"));
		FileInputStream result=null;
		//WHEN
		try{
			result = fileStorageService.displayImage(filmId);
		} catch (Exception e) {
			Assertions.assertThatExceptionOfType(FilmNotFoundException.class);
		}

		//THEN
		Assertions.assertThat(result).isNull();
		Mockito.verify(filmDao,Mockito.times(1)).getFilm(Mockito.any());
	}

	@Test
	public void shouldDisplayImageAndReturnFileInputStreamButThrowFilmNotFoundException() throws FilmNotFoundException, IOException {
		//GIVEN
		int filmId = 3;
		Film film1 = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "image5.png", "youtube.com/1", new Genre(1,"Aventure"), 1);
		Mockito.when(filmDao.getFilm(Mockito.anyInt())).thenReturn(film1);
		FileInputStream result=null;
		//WHEN
		try {
			result = fileStorageService.displayImage(filmId);
		} catch (Exception e) {
			Assertions.assertThatExceptionOfType(FileNotFoundException.class);
		}
		//THEN
		Assertions.assertThat(result).isNull();
		Mockito.verify(filmDao,Mockito.times(1)).getFilm(Mockito.any());
	}
	/*@Test
	public void shouldDisplayUnknownImage() throws FilmNotFoundException, IOException {
		//GIVEN
		int filmId = 3;
		Film film1 = new Film(1, "titre 1", "resume 1", LocalDate.of(2020, 11, 11), 123, "realisateur 1", "acteur 1", "imag5", "youtube.com/1", new Genre(1,"Aventure"), 1);
		String pathToImage = FileStorageProvider.getUploadDir();
		FileInputStream image = new FileInputStream("src/test/resources/dataTest/" + "/filmInconnu.jpg");
		Mockito.when(filmDao.getFilm(Mockito.anyInt())).thenReturn(film1);
		//WHEN
		FileInputStream result = fileStorageService.displayImage(filmId);

		//THEN
		Assertions.assertThat(result).hasSameContentAs(image);
		Mockito.verify(filmDao,Mockito.times(1)).getFilm(Mockito.any());
	}*/

    //Après
    @After
    public void createAfterTestMethod() {
    	FileManager.removeDataInDirectory(tmpDirectory);
    }
    
    @AfterClass
    public static void createOnceAfterClass() throws IOException {
    	FileManager.removeDirectory(tmpDirectory);
    }

	public static File getDatadirectory() {
		return dataDirectory;
	}
	
}
