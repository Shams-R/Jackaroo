package view;

import java.util.ArrayList;
import java.util.Arrays;

import model.card.Card;
import model.card.standard.Standard;
import model.card.standard.Suit;
import model.card.wild.Burner;
import model.card.wild.Saver;
import model.card.wild.Wild;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class demo extends Application {
	public void start (Stage stage) throws Exception {
		
       
		StackPane root = new StackPane();
        Scene scene = new Scene(root, 600, 600);

        // Create 4 home zones
        CellView cell1=new CellView("red");
        CellView cell2=new CellView("red");
        CellView cell3=new CellView("red");
        CellView cell4=new CellView("red");
        CellView cell5=new CellView("red");
        CellView cell6=new CellView("red");
        CellView cell7=new CellView("red");
        CellView cell8=new CellView("red");
        
        
        ArrayList<CellView>cells=new ArrayList<>();
        cells.add(cell1);
        cells.add(cell2);
        cells.add(cell3);
        cells.add(cell4);
        ArrayList<CellView>cells2=new ArrayList<>();
        cells2.add(cell5);
        cells2.add(cell6);
        cells2.add(cell7);
        cells2.add(cell8);
        HomeZoneView h1=new HomeZoneView(cells);
        
        HomeZoneView h2=new HomeZoneView(cells2);
        
       
       
       StackPane.setAlignment(h1,Pos.TOP_LEFT);
       StackPane.setAlignment(h2,Pos.TOP_RIGHT);
      
      // root.setMaxSize(600 * 0.6, 600 * 0.4);
       
       root.getChildren().addAll(h1,h2);
        

       

        stage.setTitle("Home Zones Test");
        stage.setScene(scene);
        stage.show();
    }
		
	
	public static void main(String[]args){
		launch(args);
	}

}
