
package model.entities;

import java.io.Serializable;
import java.util.Objects;


public class Department implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Integer Id;
    private String Name;

    public Department(Integer Id, String Name) {
        this.Id = Id;
        this.Name = Name;
    }

    public Department() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.Id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Department other = (Department) obj;
        if (!Objects.equals(this.Id, other.Id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Department{" + "Id=" + Id + ", Name=" + Name + '}';
    }
    
    
    
}
