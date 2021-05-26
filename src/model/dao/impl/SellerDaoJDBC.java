package model.dao.impl;

import db.DbException;
import java.util.List;
import model.dao.SellerDao;
import model.entities.Sellers;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.entities.Department;

public class SellerDaoJDBC implements SellerDao {

    private Connection con;

    public SellerDaoJDBC(Connection con) {
        this.con = con;
    }

    @Override
    public void insert(Sellers obj) {
        PreparedStatement st = null;
        
        try{
            st = con.prepareStatement("INSERT INTO sellers "
                                     +"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                                     +"VALUES "
                                     +"(?, ?, ?, ?, ?) ");
            
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthdate().getTime()));
            st.setDouble(4, obj.getBasesalary());
            st.setInt(5, obj.getDepartment().getId());
            
            int linhasAfetadas = st.executeUpdate();
            
            if (linhasAfetadas >0){
                ResultSet rs = st.getGeneratedKeys();
                
                if (rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
            }
            else{
                throw new DbException("Erro inesperado. Nenhuma linha afetada");
            }
                        
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Sellers obj) {
        PreparedStatement st = null;
        
        try{
            st = con.prepareStatement("UPDATE sellers "
                                     +"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                                     +"WHERE Id = ? ");
            
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthdate().getTime()));
            st.setDouble(4, obj.getBasesalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());
            
            st.executeUpdate();
                       
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;
        
        try{
            ps = con.prepareStatement("DELETE FROM sellers "
                                     +"WHERE Id = ?");
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
    }
    }

    @Override
    public Sellers findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT seller.*, department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                Sellers obj = instantiateSeller(rs, dep);

                return obj;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Sellers> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT seller.*, department.Name as DepName "
                                      + "FROM seller INNER JOIN department "
                                      + "ON seller.DepartmentId = department.Id "
                                      + "ORDER BY Name ");
                     
            rs = ps.executeQuery();
            
            List<Sellers> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                
                Department dep = map.get(rs.getInt("DepartmentId"));
                
                if(dep == null){
                
                dep = instantiateDepartment(rs);
                map.put(rs.getInt("DepartmentId"), dep);
                }
                
                Sellers obj = instantiateSeller(rs, dep);
                list.add(obj);  
            }
            
            return list;
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException{
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    private Sellers instantiateSeller(ResultSet rs, Department dep) throws SQLException{
        Sellers obj = new Sellers();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBasesalary(rs.getDouble("BaseSalary"));
        obj.setBirthdate(rs.getDate("BirthDate"));
        obj.setDepartment(dep);
        return obj;
    }

    @Override
    public List<Sellers> findByDepartment(Department department) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT seller.*, department.Name as DepName "
                                      + "FROM seller INNER JOIN department "
                                      + "ON seller.DepartmentId = department.Id "
                                      + "WHERE DepartmentId = ? "
                                      + "ORDER BY Name ");
            
            ps.setInt(1, department.getId());
            rs = ps.executeQuery();
            
            List<Sellers> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                
                Department dep = map.get(rs.getInt("DepartmentId"));
                
                if(dep == null){
                
                dep = instantiateDepartment(rs);
                map.put(rs.getInt("DepartmentId"), dep);
                }
                
                Sellers obj = instantiateSeller(rs, dep);
                list.add(obj);  
            }
            
            return list;
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

}
