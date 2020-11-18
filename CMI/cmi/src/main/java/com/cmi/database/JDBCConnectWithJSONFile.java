package com.cmi.database;
import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnectWithJSONFile {
    static public Connection getConnection(String dbTag, String path) throws ClassNotFoundException, IOException, SQLException {
	ObjectMapper mapper = new ObjectMapper();
	JsonNode node = mapper.readTree(new File(path)).path(dbTag);
	final String dataBaseType = node.path("type").asText();
	final String ipAddress = node.path("ip").asText();
	final String dataBaseName = node.path("name").asText();
	final String url = String.format("jdbc:%s://%s/%s", dataBaseType,
					 ipAddress, dataBaseName);
	final String user = node.path("user").asText();
	final String password = node.path("password").asText();
	return DriverManager.getConnection(url, user, password);
    }
}
