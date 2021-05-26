/*
 Classe responsável por fornecer a conexão 
com o banco de dados MySQL
 */

package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DB {
    
private static Connection con = null;	

public static Connection getConnection(){
    if(con == null){
    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost/coursejdbc", "developer", "1234567");
    }
    catch(SQLException e){
        e.getMessage();
        return null;
    }
    catch(ClassNotFoundException e){
        e.getMessage();
        return null;
    }
    }
    return con;
    }

public static void closeConnection() {
    if (con != null) {
    try {
	con.close();
    } 
    catch (SQLException e) {
	throw new DbException(e.getMessage());
    }
    }
}

public static void closeStatement(Statement st) {
    if (st != null) {
    try {
	st.close();
    } 
    catch (SQLException e) {
	throw new DbException(e.getMessage());
    }
    }
}

public static void closeResultSet(ResultSet rs) {
    if (rs != null) {
    try {
	rs.close();
    } 
    catch (SQLException e) {
	throw new DbException(e.getMessage());
    }
    }
}
	
}
	

