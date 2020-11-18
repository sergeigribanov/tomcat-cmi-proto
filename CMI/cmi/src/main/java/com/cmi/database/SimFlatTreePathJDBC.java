package com.cmi.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import com.cmi.model.SimFlatTreePath;
import com.cmi.database.SimFlatTreePathDAO;

public class SimFlatTreePathJDBC implements SimFlatTreePathDAO {
    private Connection connection;
    public SimFlatTreePathJDBC(String url, String user, String password)
	throws ClassNotFoundException, SQLException {
	Class.forName("org.postgresql.Driver");
	this.connection = DriverManager.getConnection(url, user, password);
    }
    public SimFlatTreePathJDBC(String dbTag, String path)
	throws ClassNotFoundException, IOException, SQLException {
	Class.forName("org.postgresql.Driver");
	this.connection = JDBCConnectWithJSONFile.getConnection(dbTag, path);
    }
    public void addSimFlatTreePath(SimFlatTreePath path)
	throws SQLException {
	String sql = "insert into trph_sim_paths (ptag, stag, spath) values (?,?,?)";
	PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	ps.setString(1, path.getPointTag());
	ps.setString(2, path.getSimTag());
	ps.setString(3, path.getPath());
	ps.executeUpdate();
    }
    public void removeSimFlatTreePath(String pointTag, String simTag) throws SQLException {
	String sql = "delete from trph_sim_paths where ptag = ? and stag = ?";
	PreparedStatement ps = this.connection.prepareStatement(sql);
	ps.setString(1, pointTag);
	ps.setString(2, simTag);
	ps.executeUpdate();
    }
    public SimFlatTreePath getSimFlatTreePath(String pointTag, String simTag)
	throws SQLException, DataBaseException {
	String sql = "select * from trph_sim_paths where ptag = ? and stag = ?";
	PreparedStatement ps = this.connection.prepareStatement(sql);
	ps.setString(1, pointTag);
	ps.setString(2, simTag);
	ResultSet result = ps.executeQuery();
	while(result.next()) {
	    SimFlatTreePath path = new SimFlatTreePath();
	    path.setPointTag(result.getString("ptag"));
	    path.setSimTag(result.getString("stag"));
	    path.setPath(result.getString("spath"));
	    result.close();
	    return path;
	}
	final String msg = "Simulation path for tr_ph (ptag=%s, stag=%s) not found!";
	throw new DataBaseException(String.format(msg, pointTag, simTag));
    }
}
