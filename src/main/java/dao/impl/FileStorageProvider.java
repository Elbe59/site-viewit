package dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileStorageProvider {

	private static String uploadDir;
	static final Logger LOGGER = LogManager.getLogger();

	private static Properties loadProperties() {
		LOGGER.debug("Trying to load properties");
		try (InputStream input = FileStorageProvider.class.getClassLoader().getResourceAsStream("images.properties")) {
			if (input == null) {
				throw new IllegalStateException("Properties file not found.");
			}

			Properties configuration = new Properties();
			configuration.load(input);
			LOGGER.info("Properties loaded");
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
