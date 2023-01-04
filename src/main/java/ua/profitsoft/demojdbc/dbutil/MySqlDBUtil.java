package ua.profitsoft.demojdbc.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MySqlDBUtil {

	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String user;
	@Value("${spring.datasource.password}")
	private String pwd;


	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, pwd);
	}

}
