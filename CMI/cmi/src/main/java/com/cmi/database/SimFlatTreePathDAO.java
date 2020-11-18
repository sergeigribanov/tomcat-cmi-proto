package com.cmi.database;
    
import java.sql.SQLException;
import java.util.ArrayList;

import com.cmi.model.SimFlatTreePath;

public interface SimFlatTreePathDAO {
    public void addSimFlatTreePath(SimFlatTreePath path) throws SQLException;
    public void removeSimFlatTreePath(String pointTag, String simTag) throws SQLException;
    public SimFlatTreePath getSimFlatTreePath(String pointTag, String simTag)
	throws SQLException, DataBaseException;
}
