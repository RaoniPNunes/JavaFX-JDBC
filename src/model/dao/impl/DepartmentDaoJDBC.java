
package model.dao.impl;

import db.DB;
import db.DbException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{
    
    private Connection con = DB.getConnection();
    
    public DepartmentDaoJDBC(Connection con){
        this.con = con;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement ps = null;
        
        try{
            ps = con.prepareStatement("INSERT INTO department "
                                      +"(Id, Name) "
                                      +"VALUES "
                                      +"(?, ?)");
            ps.setInt(1, obj.getId());
            ps.setString(2, obj.getName());
            
            ps.executeUpdate();
                       
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement ps = null;
        
        try{
            ps = con.prepareStatement("UPDATE department "
                                     +"SET Id = ?, Name = ?"
                                     +"WHERE Id = ?");
            
            ps.setInt(1, obj.getId());
            ps.setString(2, obj.getName());
            ps.setInt(3, obj.getId());
            
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;
        
        try{
            ps = con.prepareStatement("DELETE FROM department WHERE Id = ? ");
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            ps = con.prepareStatement("SELECT * FROM department WHERE Id = ?");
            
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if(rs.next()){
               Department dep = new Department(rs.getInt("Id"), rs.getString("Name"));              
               return dep;
            }
            return null;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Department> list = new ArrayList<>();
        
        try{
            ps = con.prepareStatement("select * from department");
            rs = ps.executeQuery();
            Department dep;
                        
            while(rs.next()){
                dep = new Department(rs.getInt("Id"), rs.getString("Name"));
                list.add(dep);
            }
            return list;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
    }
    
}
