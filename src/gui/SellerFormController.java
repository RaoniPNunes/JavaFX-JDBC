
package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Sellers;
import model.exceptions.ValidationException;
import model.services.SellerServices;


public class SellerFormController implements Initializable {
    
    private Sellers entity;
    
    private SellerServices service;
    
    //lista para guardar os inscritos para o padrão observer event
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    
    @FXML
    private TextField txtId;
    
    @FXML
    private TextField txtName;
    
    @FXML 
    private Label labelErrorName;
    
    @FXML
    private Button btSave;
    
    @FXML
    private Button btCancel;
    
    //dependência da classe Sellers
    public void setSellers(Sellers entity){
        this.entity = entity;
    }
    
    //dependência da classe SellerService
    public void setSellerService(SellerServices service){
        this.service = service;
    }
    
    //método de inscrição dos objetos listeners (observers)
    public void subscribeDataChangeListener (DataChangeListener listener){
        dataChangeListeners.add(listener);
    }
    
    @FXML
    public void onBTSaveAction(ActionEvent event){
        //cria um objeto Sellers e insere ou faz update através dos serviços
        //do SellersService criado, utilizando a depêndêcia service.
        if(entity == null){
            throw new IllegalStateException("Entity was null");
        }
        if(service == null){
            throw new IllegalStateException("Service was null");
        }
        try{
        entity = getFormData();
        service.saveOrUpdate(entity);
        notifyDataChangeListeners();
        Utils.currentStage(event).close();
        }
        catch(ValidationException e){
            setErrorMessages(e.getErrors());
        }
        catch(DbException e){
            Alerts.showAlert("Erro ao salvar o departamento", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void onBtCancelAction(ActionEvent event){
        Utils.currentStage(event).close();
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }
    
    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }
    
    //método para popular (atribuir valores) ao txtId e o txtName
    public void updateFormData(){
        if(entity == null){
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }

    //método para pegar os dados do formulário (SellersForm) e instanciar e retornar um
    //novo objeto do tipo Sellers.
    //Obs: os valores do formulário foram atribuídos pelo método updateFormData()
    private Sellers getFormData() {
        Sellers obj = new Sellers();
        
        ValidationException exception = new ValidationException("Erro de Validação");
        
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        
        if(txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addErrors("Name", "Campo Name não pode ficar vázio");
        }
        obj.setName(txtName.getText());
        
        if(exception.getErrors().size() > 0){
            throw exception;
        }
        
        return obj;
    }
    
    //os objetos inscritos irão executar um determinado método previsto em uma interface.
    //No presente caso, para cada objeto inscrito (observer) executa o onDataChange.
    //OBS: o onDataChange foi implementado na classe SellerListController e executa o updateTableView
    
    private void notifyDataChangeListeners(){
        for(DataChangeListener listener : dataChangeListeners){
            listener.onDataChange();
        }
    }
    
    private void setErrorMessages(Map<String, String> errors){
        Set<String> campos = errors.keySet();
        
        if(campos.contains("Name")){
            labelErrorName.setText(errors.get("Name"));
        }
    }

       
}
