package dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariadb.jdbc.MariaDbDataSource;

public class DataSourceProvider {

	static final Logger LOGGER = LogManager.getLogger();
	private static MariaDbDataSource dataSource;

	public static DataSource getDataSource() {

		if (dataSource == null) {
			initDataSource();
		}
		return dataSource;
	}

	private static void initDataSource(){
		Properties jdbcProperties = new Properties();
		try{
			LOGGER.debug("Connexion à la base de donnée");
			jdbcProperties.load(DataSourceProvider.class.getClassLoader().getResourceAsStream("jdbc.properties"));
			dataSource = new MariaDbDataSource();

			dataSource.setServerName(jdbcProperties.getProperty("jdbc.server.host"));
			dataSource.setPort(Integer.parseInt(jdbcProperties.getProperty("jdbc.server.port")));
			dataSource.setDatabaseName(jdbcProperties.getProperty("jdbc.database"));
			dataSource.setUser(jdbcProperties.getProperty("jdbc.user"));
			dataSource.setPassword(jdbcProperties.getProperty("jdbc.password"));
			LOGGER.info("Connexion à mariadb réussie");
		}catch(Exception e){
			LOGGER.error("Erreur lors de la connexion à mariadb");
			e.printStackTrace();
		}
	}
}

