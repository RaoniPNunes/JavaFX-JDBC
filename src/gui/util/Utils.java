/*
 Classe utilitária para se recuperar o stage de um determinado view
em um dado momento da utilização da API
 */
package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;


public class Utils {
    
    //método utilitário para recuperar o stage atual de uma view
    public static Stage currentStage(ActionEvent event){
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
    
    //método utilitário para transformar um string de uma caixa de texto em inteiro 
    public static Integer tryParseToInt(String str){
        try{
            return Integer.parseInt(str);
        }
        catch(NumberFormatException e){
            return null;
        }
    }
}
