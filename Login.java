package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {

        VBox root = new VBox(10);
        root.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label title = new Label("Login");
        title.setStyle("-fx-font-size: 40; -fx-font-weight: bold;");
        
        TextField email = new TextField();
        email.setPromptText("Email");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        
        Button login = new Button("Login");
        Button register = new Button("Go to Register");
        Label error = new Label();
        error.setStyle("-fx-text-fill: red;");

        login.setOnAction(event -> {
            String em = email.getText();
            String pw = password.getText();

            if (em.isEmpty() || pw.isEmpty()) {
            	error.setText("Please fill in all fields.");
            } else {
            	String s = "SELECT * FROM users WHERE email = ? AND pw = ?";
            	try {
            		Class.forName("com.mysql.cj.jdbc.Driver");
                 	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/txtedit", "root", "");
                    PreparedStatement st = conn.prepareStatement(s);
                    st.setString(1, em);
                    st.setString(2, pw);
                    ResultSet r = st.executeQuery();
                    if (r.next()) {
                    	 String fname = r.getString("fname");
                         String lname = r.getString("lname");
                         new FileManagement(fname, lname, em).start((Stage) login.getScene().getWindow());
                    } else {
                    	error.setText("Invalid email or password.");
                    }
            	} catch (SQLException | ClassNotFoundException e) {
            		error.setText("Error loading the next screen.");
                    e.printStackTrace();
                }
            }
        });
        
        register.setOnAction(event -> {
        	new Register().start(new Stage());
            primaryStage.close();  
        });

        root.getChildren().addAll(title, email, password, login, register, error);
        
        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}