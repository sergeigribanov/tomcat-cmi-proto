package com.cmi.database;
    
import java.sql.SQLException;
import java.util.ArrayList;

import com.cmi.model.EPoint;

public interface EPointDAO {
	
	/*
	 * add epoint in database
	 */
	public void addEPoint(EPoint epoint) throws SQLException;
	/*
	 * remove epoint of database
	 */
	public void removeEPoint(EPoint epoint) throws SQLException;
	/*
	 * get one epoint with similar name.
	 */
	public EPoint getEPoint(String name) throws SQLException;
	/*
	 * get all epoints
	 */
	public ArrayList<EPoint> getAllEPoints() throws SQLException;
}
