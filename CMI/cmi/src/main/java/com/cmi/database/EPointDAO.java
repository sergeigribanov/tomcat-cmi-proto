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
    public EPoint getEPoint(String pointTag) throws SQLException, DataBaseException;
    /*
     * get all epoints
     */
    public ArrayList<EPoint> getListOfEPoints() throws SQLException;
    /*
     * get all epoints in a certain experiment
     */
    public ArrayList<EPoint> getListOfEPoints(String expTag) throws SQLException, DataBaseException;

    public ArrayList<String> getListOfExpTags() throws SQLException;
}
