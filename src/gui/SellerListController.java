
package gui;

import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.entities.Sellers;
import model.services.SellerServices;


public class SellerListController implements Initializable, DataChangeListener{
    
    //Injetando uma dependência do SellerServices para acessar os métodos dessa classe
    private SellerServices service;
    
    //criando referência aos componentes da View SellerList
    @FXML
    private TableView<Sellers> tableViewSeller;
    
    @FXML
    private TableColumn<Sellers, Integer> tableColumnId;
    
    @FXML
    private TableColumn<Sellers, String> tableColumnNome;
    
    @FXML
    private TableColumn<Sellers, String> tableColumnEmail;
    
    @FXML
    private TableColumn<Sellers, Date> tableColumnBirthDate;
    
    @FXML
    private TableColumn<Sellers, Double> tableColumnBaseSalary;
    
    @FXML
    private TableColumn<Sellers, Sellers> tableColumnEdit;
    
    @FXML
    private TableColumn<Sellers, Sellers> tableColumnRemove;
    
    @FXML
    private Button btNovo;
    
    //OBS: o Observable list serve para carregar listas no JavaFX
    private ObservableList<Sellers> obsList; 
    
    public void setSellerService(SellerServices service){
        this.service = service; 
    }
    //Botão para criar novo Vendedor
    //Vai carregar uma dialogView com método criado mais abaixo (createDialogForm).
    //Para se carregar essa dialogView precisa passar o stage atual, usando o método currentStage da classe Utils.
    //Obs: o event passado de parâmetro servirá para referência no método currentStage
    @FXML
    public void onBtNewAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Sellers obj = new Sellers();
        createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //método padrão do JavaFX pra iniciar comportamento de colunas (tables views)
        initializeNodes();
        
    }
    
    private void initializeNodes(){
        //implementação do método padrão do JavaFX pra iniciar comportamento de colunas (tables views)
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("basesalary"));
        Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
                       
    }
    
    //Método para atualizar a tabela do SellerView
    //Para isso:
    //1 passo: cria uma lista trazendo uma lista do SellerService (método findAll)
    //2 passo: transfere essa lista criada para o ObservableList do JavaFX
    //3 passo: carrega o ObservableList instanciado para o tableView.
    public void updateTableView(){
        if (service == null){
            throw new IllegalStateException("service não encontrado");
        }
        //carrega uma lista trazendo do método findAll do SellerService
        List<Sellers> list = service.findAll();
                    
        //transfere a lista criada acima ao observableList criado.
        //OBS: o Observable list serve para carregar listas no JavaFX
        obsList = FXCollections.observableArrayList(list);
        
        //Transfere o obsList instanciado acima para o tableView
        tableViewSeller.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }
    
    private void createDialogForm(Sellers obj, String caminho, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Pane pane = loader.load();
            
            SellerFormController controller = loader.getController();
            //controller acessa as classes Sellers e SellerService pelas dependências
            controller.setSellers(obj);
            controller.setSellerService (new SellerServices());
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
    
    //essa classe que oferece a inscrição de objetos precisa implementar o método da interface
    //no caso, cada objeto implementado vai efetuar a atualização da tableview.
    @Override
    public void onDataChange() {
        updateTableView(); //-> cada objeto inscrito vai executar a atualização de tabela
    }
    
    private void initEditButtons() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Sellers, Sellers>() {
            private final Button button = new Button("edit");

            @Override
            protected void updateItem(Sellers obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                setGraphic(null);
                return;
            }
                setGraphic(button);
                button.setOnAction(
                event -> createDialogForm(
                        obj, "/gui/SellerForm.fxml",Utils.currentStage(event)));
            }
        });
    }
    
    private void initRemoveButtons() {
        tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param -> new TableCell<Sellers, Sellers>() {
        private final Button button = new Button("remove");
        
        @Override
        protected void updateItem(Sellers obj, boolean empty) {
            super.updateItem(obj, empty);
            
            if (obj == null) {
                setGraphic(null);
                return;
            }
        
            setGraphic(button);
            button.setOnAction(event -> removeEntity(obj));
            }
        });
    }
    
    private void removeEntity(Sellers obj){
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que quer deletar?");
        
        if(result.get() == ButtonType.OK){
            if(service == null){
                throw new IllegalStateException("Serviço nulo");
            }
            try{
                service.remove(obj);
                updateTableView();
            }
            catch(DbIntegrityException e){
                Alerts.showAlert("Erro ao remover objeto", null, e.getMessage(), AlertType.ERROR);
            }
        }
    }
    
}
