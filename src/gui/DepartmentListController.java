
package gui;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentServices;


public class DepartmentListController implements Initializable, DataChangeListener{
    
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
    //Botão para criar novo departamento
    //Vai carregar uma dialogView com método criado mais abaixo (createDialogForm).
    //Para se carregar essa dialogView precisa passar o stage atual, usando o método currentStage da classe Utils.
    //Obs: o event passado de parâmetro servirá para referência no método currentStage
    @FXML
    public void onBtNewAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Department obj = new Department();
        createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
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
    
    private void createDialogForm(Department obj, String caminho, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Pane pane = loader.load();
            
            DepartmentFormController controller = loader.getController();
            //controller acessa as classes Department e DepartmentService pelas dependências
            controller.setDepartment(obj);
            controller.setDepartmentService(new DepartmentServices());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
            
            //Para se criar uma nova view em cima da view que está sendo mostrada
            //é necessário se criar um novo stage.
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Inserir Novo Departamento");
            dialogStage.setScene(new Scene(pane));//-> cria-se uma nova Scene nesse novo stage
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);//-> informa-se o stage pai em que vai surgir o dialogStage view
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.show();
        }
        catch(IOException e){
            Alerts.showAlert("IO Exception", "Error Loading View", e.getMessage(), AlertType.ERROR);
        }
    }

    @Override
    public void onDataChange() {
        updateTableView();
    }
    
}
