/*
 Classe responsável por implantar e fornecer serviços/operações 
à classe Sellers.
 */
package model.services;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Sellers;


public class SellerServices {
        
    private SellerDao dao = DaoFactory.createSellerDao();
    
    public List<Sellers> findAll(){
        return dao.findAll();
    }
    
    public void saveOrUpdate(Sellers obj){
        if(obj.getId() == null){
            dao.insert(obj);
        }
        else{
            dao.update(obj);
        }
    }
    
    public void remove(Sellers obj){
        dao.deleteById(obj.getId());
    }
}
