
package gui;

import Main.Main;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentServices;


public class DepartmentListController implements Initializable{
    
    //Injetando uma dependência do DepartmentServices para acessar os métodos dessa classe
    private DepartmentServices service;
    
    //criando referência aos componentes da View DepartmentList
    @FXML
    private TableView<Department> tableViewDepartment;
    
    @FXML
    private TableColumn<Department, Integer> tableColumnId;
    
    @FXML
    private TableColumn<Department, String> tableColumnNome;
    
    @FXML
    private Button btNovo;
    
    //OBS: o Observable list serve para carregar listas no JavaFX
    
    private ObservableList<Department> obsList; 
    
    public void setDepartmentService(DepartmentServices service){
        this.service = service; 
    }
    
    @FXML
    public void onBtNewAction(){
        System.out.println("onBtNewAction");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //método padrão do JavaFX pra iniciar comportamento de colunas (tables views)
        initializeNodes();
        
    }
    
    private void initializeNodes(){
        //implementação do método padrão do JavaFX pra iniciar comportamento de colunas (tables views)
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("Name"));
                       
    }
    
    //Método para atualizar a tabela do DepartmentView
    //Para isso:
    //1 passo: cria uma lista trazendo uma lista do DepartmentService (método findAll)
    //2 passo: transfere essa lista criada para o ObservableList do JavaFX
    //3 passo: carrega o ObservableList instanciado para o tableView.
    public void updateTableView(){
        if (service == null){
            throw new IllegalStateException("service não encontrado");
        }
        //carrega uma lista trazendo do método findAll do DepartmentService
        List<Department> list = service.findAll();
                    
        //transfere a lista criada acima ao observableList criado.
        //OBS: o Observable list serve para carregar listas no JavaFX
        obsList = FXCollections.observableArrayList(list);
        
        //Transfere o obsList instanciado acima para o tableView
        tableViewDepartment.setItems(obsList);
    }
    
}
