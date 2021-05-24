
package gui;

import Main.Main;
import gui.util.Alerts;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentServices;

/**
 * FXML Controller class
*/
public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemVendedor;
    
    @FXML
    private MenuItem menuItemDepartamento;
    
    @FXML
    private MenuItem menuItemAbout;
    
    @FXML
    public void onMenuItemVendedorAction(){
        System.out.println("onMenuItemVendedorAction");
    }
    
    @FXML
    public void onMenuItemDepartamentoAction(){
        loadView("/gui/DepartmentList.fxml", (DepartmentListController controle) -> {
                                                controle.setDepartmentService(new DepartmentServices());
                                                controle.updateTableView();});
    }
    
    @FXML
    public void onMenuItemAboutAction(){
        loadView("/gui/About.fxml", x -> {}); //carregar o método loadView (carregador de views fxml)e faz referência ao caminho da view About
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    private synchronized <T> void loadView(String caminho, Consumer <T> initializingAction){ //Método para gerar nova janela do tipo VBox
        
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
        VBox newVBox = loader.load();
        
        //Reorganização do menu principal:
        //1 passo: cria-se referências ao mainScene e ao MainVbox da tela principal (MainView).
        //2 passo: cria-se referência ao mainMenu (menus registro e about) inseridos no MainVBox da tela principal
        //3 passo: limpa todos os filhos da mainVBox pra poder reorganizar depois (próximos passos).
        //4 passo: readiciona o mainMenu ao mainVBox
        //5 passo: adiciona o novo VBox do menu about (recém criado) ao mainVBox.
        
        Scene mainScene = Main.getMainScene(); //(1) referência ao mainScene da classe Main
        VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); //(1) referência ao VBox da MainView (tela principal)
              
        Node mainMenu = mainVBox.getChildren().get(0); //(2) referência ao mainMenu (primeiro filho do mainVBox - MenuBar).
        mainVBox.getChildren().clear(); //(3)limpa todos os filhos do menu principal.
        mainVBox.getChildren().add(mainMenu); //(4) adiciona novamente o mainMenu (menu principal) ao container principal mainVBox.
        mainVBox.getChildren().addAll(newVBox.getChildren()); //(5) adiciona também o VBox e seus filhos no mainVBox.
        
        T controle = loader.getController();
        initializingAction.accept(controle);
               
        }
        catch (IOException e){
            Alerts.showAlert("IOExcpetion", "Erro ao carregar a View", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
   /* private synchronized void loadView2(String caminho){ //Método para gerar nova janela do tipo VBox
        
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
        VBox newVBox = loader.load();
                        
        Scene mainScene = Main.getMainScene(); 
        VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); 
              
        Node mainMenu = mainVBox.getChildren().get(0); 
        mainVBox.getChildren().clear(); 
        mainVBox.getChildren().add(mainMenu); 
        mainVBox.getChildren().addAll(newVBox.getChildren());
        
        //cria-se um controller do DepartmentList que tem os métodos
        //esse controller vai injetar a dependência (2) do serviço e chamar o método (3) que precisa dessa dependencia
        
        DepartmentListController controle = loader.getController();// instancia um controlador da DepartmentList e carrega
        controle.setDepartmentService(new DepartmentServices());// (2) instancia a dependencia do DepartmentService
        controle.updateTableView();// (3) chama o método para mostrar a lista na tabela
               
        }
        catch (IOException e){
            Alerts.showAlert("IOExcpetion", "Erro ao carregar a View", e.getMessage(), Alert.AlertType.ERROR);
        }
    }*/
    
}
