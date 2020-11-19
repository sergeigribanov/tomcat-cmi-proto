package com.cmi.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import com.cmi.model.EPoint;
import com.cmi.database.EPointDAO;

public class EPointJDBC implements EPointDAO {
    
    private Connection connection;
    
    public EPointJDBC(String url, String user, String password) throws ClassNotFoundException, SQLException {
	//load driver communication of postgresql.
	Class.forName("org.postgresql.Driver");
	//open the connection
	this.connection = DriverManager.getConnection(url, user, password);
    }

    public EPointJDBC(String dbTag, String path) throws ClassNotFoundException, IOException, SQLException {
	//load driver communication of postgresql.
	Class.forName("org.postgresql.Driver");
	//open the connection
	this.connection = JDBCConnectWithJSONFile.getConnection(dbTag, path);
    }
    
    public void addEPoint(EPoint epoint) throws SQLException {
	//query of postgresql
	String sql = "insert into epoints(ptag, etag, ebeam, mfield)"
	    + "values (?,?,?,?)";
	
	PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	
	
	// 1 = first '?' 
	ps.setString(1, epoint.getPointTag());
	// 2 - second '?'
	ps.setString(2, epoint.getExpTag());
	// 3 = third '?'
	ps.setDouble(3, epoint.getBeamEnergy());
	ps.setDouble(4, epoint.getMagneticField());
	
	//use execute update when the database return nothing
	ps.executeUpdate();
	
	ResultSet generatedKeys =  ps.getGeneratedKeys();
	if(generatedKeys.next()) {
	    epoint.setPointTag(generatedKeys.getString(1));
	}	
    }

    public void removeEPoint(EPoint epoint) throws SQLException  {
	String sql = "delete from epoints where ptag = ?";
	PreparedStatement ps = this.connection.prepareStatement(sql);
	ps.setString(1, epoint.getPointTag());
	ps.executeUpdate();
    }

    public EPoint getEPoint(String pointTag) throws SQLException, DataBaseException {
	String sql = "select * from epoints where ptag = ?";
	PreparedStatement ps = this.connection.prepareStatement(sql);
	ps.setString(1, pointTag);
	ResultSet result = ps.executeQuery();
	while(result.next()) {
	    EPoint epoint = new EPoint();
	    epoint.setPointTag(result.getString("ptag"));
	    epoint.setExpTag(result.getString("etag"));
	    epoint.setBeamEnergy(result.getDouble("ebeam"));
	    epoint.setMagneticField(result.getDouble("mfield"));
	    result.close();
	    return epoint;
	}
	throw new DataBaseException(String.format("Energy point tag (%s) not found!", pointTag));
    }

    public ArrayList<EPoint> getListOfEPoints() throws SQLException {
	ArrayList<EPoint> array = new ArrayList<EPoint>();
	ResultSet result = this.connection.prepareStatement("select * from epoints").executeQuery();
	while(result.next()) {
	    EPoint epoint = new EPoint();
	    epoint.setPointTag(result.getString("ptag"));
	    epoint.setExpTag(result.getString("etag"));
	    epoint.setBeamEnergy(result.getDouble("ebeam"));
	    epoint.setMagneticField(result.getDouble("mfield"));
	    array.add(epoint);
	}
	result.close();
	return array;
    }
    public ArrayList<EPoint> getListOfEPoints(String expTag) throws SQLException, DataBaseException {
	ArrayList<EPoint> array = new ArrayList<EPoint>();
	String sql = "select * from epoints where etag = ?";
	PreparedStatement ps = this.connection.prepareStatement(sql);
	ps.setString(1, expTag);
	ResultSet result = ps.executeQuery();
	while(result.next()) {
	    EPoint epoint = new EPoint();
	    epoint.setPointTag(result.getString("ptag"));
	    epoint.setExpTag(result.getString("etag"));
	    epoint.setBeamEnergy(result.getDouble("ebeam"));
	    epoint.setMagneticField(result.getDouble("mfield"));
	    array.add(epoint);
	}
	result.close();
	if (array.size() == 0 && !this.getListOfExpTags().contains(expTag)) {
	    throw new DataBaseException(String.format("Experiment tag (%s) not found!", expTag));
	}
	
	return array;
    }
    public ArrayList<String> getListOfExpTags() throws SQLException {
	ArrayList<String> array = new ArrayList<String>();
	String sql = "select etag from experiments";
	ResultSet result = this.connection.createStatement().executeQuery(sql);
	while (result.next()) {
	    array.add(result.getString("etag"));
	}
	return array;
    }
}
