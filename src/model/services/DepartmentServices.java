/*
 Classe responsável por implantar e fornecer serviços/operações 
à classe Department.
 */
package model.services;

import java.util.ArrayList;
import java.util.List;
import model.entities.Department;


public class DepartmentServices {
    
    public List<Department> findAll(){
            List<Department> list = new ArrayList<>();
            list.add(new Department(1, "Computadores"));
            list.add(new Department(2, "Carros"));
            list.add(new Department(3, "Livros"));
            return list;
    }   
}
