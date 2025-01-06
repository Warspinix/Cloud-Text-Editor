package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManagement extends Application {
	
	String fname, lname, em;
	
	public FileManagement (String fname, String lname, String em) {
		this.fname = fname;
	    this.lname = lname;
	    this.em = em;
	}
	
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label title = new Label("Welcome, " + fname + "!");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");
        
        ListView<String> fileList = new ListView<>();
        fileList.getItems().add("New File");
        
        String s = "SELECT * FROM files WHERE email = ?";
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
         	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/txtedit", "root", "");
            PreparedStatement st = conn.prepareStatement(s);
            st.setString(1, em);
            ResultSet r = st.executeQuery();
            while (r.next()) {
            	String path = r.getString("filepath");
            	Path p = Paths.get(path);
            	String fext = p.getFileName().toString();
                String fn = fext.substring(0, fext.lastIndexOf('.'));
                fileList.getItems().add(fn);
            } 
    	} catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }  
        fileList.setOnMouseClicked(event -> {
            String selectedFile = fileList.getSelectionModel().getSelectedItem();
            if (selectedFile != null) {
                try {
                    if (selectedFile.equals("New File")) {
                        new Editor(fname, lname, em).start(stage);
                    } else {
                        new Editor(fname, lname, em, selectedFile).start(stage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        root.getChildren().addAll(title, fileList);

        stage.setTitle("Files");
        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}