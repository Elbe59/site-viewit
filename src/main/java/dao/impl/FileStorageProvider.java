package dao.impl;

import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class FileStorageProvider {

	private static String uploadDir;

	private static Properties loadProperties() {
		try (InputStream input = FileStorageProvider.class.getClassLoader().getResourceAsStream("images.properties")) {
			if (input == null) {
				throw new IllegalStateException("Properties file not found.");
			}

			Properties configuration = new Properties();
			configuration.load(input);
			return configuration;
		} catch (IOException e) {
			throw new RuntimeException("Problem when reading the properties file.", e);
		}
	}

	public static String getUploadDir() {
		Properties configuration = FileStorageProvider.loadProperties();
		uploadDir = configuration.getProperty("file.upload-dir");
		return uploadDir;
	}
}
