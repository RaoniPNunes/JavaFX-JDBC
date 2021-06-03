
package model.dao;

import db.DB;
import java.sql.Connection;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;


public class DaoFactory {
    
    Connection con = DB.getConnection();
    
    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(DB.getConnection());
    }
    
    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }
    
}
