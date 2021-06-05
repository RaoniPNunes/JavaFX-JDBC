
package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import java.net.URL;
import java.time.*;
import java.time.temporal.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Sellers;
import model.exceptions.ValidationException;
import model.services.DepartmentServices;
import model.services.SellerServices;


public class SellerFormController implements Initializable {
    
    private Sellers entity;
    
    private SellerServices service;
    
    private DepartmentServices departmentService;
    
    //lista para guardar os inscritos para o padrão observer event
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    
    @FXML
    private TextField txtId;
    
    @FXML
    private TextField txtName;
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private DatePicker dpBirthDate;
    
    @FXML
    private TextField txtBaseSalary;
    
    @FXML
    private ComboBox<Department> comboBoxDepartment;
    
    @FXML 
    private Label labelErrorName;
    
    @FXML 
    private Label labelErrorEmail;
    
    @FXML 
    private Label labelErrorBirthDate;
    
    @FXML 
    private Label labelErrorBaseSalary;
    
    @FXML
    private Button btSave;
    
    @FXML
    private Button btCancel;
    
    @FXML
    private ObservableList<Department> obsList;
    
    //dependência da classe Sellers
    public void setSellers(Sellers entity){
        this.entity = entity;
    }
    
    //dependência da classe SellerService
    public void setServices(SellerServices service, DepartmentServices departmentService){
        this.service = service;
        this.departmentService = departmentService;
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
            Alerts.showAlert("Confirmação de Salvamento", null, "Novo Vendedor Salvo com Sucesso", Alert.AlertType.CONFIRMATION);
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
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
        
        initializeComboBoxDepartment(); 
    }
    
    //método para popular (atribuir valores) ao txtId e o txtName
    public void updateFormData(){
        if(entity == null){
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f", entity.getBasesalary()));
        if(entity.getBirthdate() != null){
        dpBirthDate.setValue(LocalDateTime.ofInstant(entity.getBirthdate().toInstant(), ZoneId.systemDefault()).toLocalDate());
        }
        if(entity.getDepartment() == null){
            comboBoxDepartment.getSelectionModel().selectFirst();
        }
        else{
        comboBoxDepartment.setValue(entity.getDepartment());
        }
    }
    
    public void loadAssociatedObjects(){
        List<Department> list = departmentService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(obsList);
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
        
        if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")){
            exception.addErrors("Email", "Campo Email não pode ficar vázio");
        }
        obj.setEmail(txtEmail.getText());
        
        if(dpBirthDate == null){
            exception.addErrors("BirthDate", "Campo BirthDate não pode ficar vázio");
        }
        else{
        DatePicker dp = new DatePicker(LocalDate.now());
        LocalDate ld = dp.getValue();
        Instant instant = Instant.from(ld.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        obj.setBirthdate(date);
        }
        
        if(txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")){
            exception.addErrors("Salário Base", "Campo Salário Base não pode ficar vázio");
        }
        obj.setBasesalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
        
        obj.setDepartment(comboBoxDepartment.getValue());
        
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
        else{
            labelErrorName.setText("");
        }
        
        if(campos.contains("Email")){
            labelErrorEmail.setText(errors.get("Email"));
        }
        else{
            labelErrorEmail.setText("");
        }
        
        if(campos.contains("Salário Base")){
            labelErrorBaseSalary.setText(errors.get("Salário Base"));
        }
        else{
            labelErrorBaseSalary.setText("");
        }
        
        if(campos.contains("BirthDate")){
            labelErrorBirthDate.setText(errors.get(""));
        }
        else{
            labelErrorBirthDate.setText("");
        }
    }
    
    private void initializeComboBoxDepartment() {
    Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }

       
}
