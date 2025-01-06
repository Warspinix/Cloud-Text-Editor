package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.mail.*; 
import javax.mail.internet.*; 
import java.util.Properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Random;

import java.io.File;


public class Register extends Application {
	
	String path = "C:\\Users\\Yuvaraj\\Desktop\\My Space\\Java\\Text Editor\\";
    final String smtpmail = "mityuvaraj079@gmail.com";
    final String smtppw = "mjlr rbxt etho hmat";
    final String smtphost = "smtp.gmail.com";
    final int smtpport = 587;
	
    public void start(Stage primaryStage) {
		
		VBox root = new VBox(10);
        root.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label title = new Label("Register");
        title.setStyle("-fx-font-size: 40; -fx-font-weight: bold;");
        
        TextField fname = new TextField();
        fname.setPromptText("First Name");
        TextField lname = new TextField();
        lname.setPromptText("Last Name");
        TextField email = new TextField();
        email.setPromptText("Email");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        Button register = new Button("Register");
        Button login = new Button("Go to Login");
        Label error = new Label();
        error.setStyle("-fx-text-fill: red;");
        
        register.setOnAction(event -> {
            String fn = fname.getText();
            String ln = lname.getText();
        	String em = email.getText();
            String pw = password.getText();
            
            if (fn.isEmpty() || ln.isEmpty() || em.isEmpty() || pw.isEmpty()) {
            	error.setText("Please fill in all fields.");
            } else {
            	if (emailExists(em)) {
            		error.setText("Email is already registered.");
            	} else {
            		String otp = generateOTP();
            		if (sendOTP(em, otp)) {
                        TextInputDialog otpDialog = new TextInputDialog();
                        otpDialog.setTitle("Enter OTP");
                        otpDialog.setHeaderText("Please enter the OTP sent to your email.");
                        otpDialog.setContentText("OTP:");
                        otpDialog.showAndWait().ifPresent(userOtp -> {
                            if (userOtp.equals(otp)) {
                                String folderName = em.split("@")[0] + em.split("@")[1].split("\\.")[0];
                                File userFolder = new File(path + folderName);
                                if (!userFolder.exists()) {
                                    userFolder.mkdirs();
                                }

                                if (addUser (fn, ln, em, pw)) {
                                    showAlert("Registration Successful", null, "You are successfully registered!", "info");
                                    goToLogin(primaryStage); 
                                }
                            } else {
                                error.setText("Invalid OTP. Please try again.");
                            }
                        });
                    }
            	}
            }
        });
        
        login.setOnAction(event -> goToLogin(primaryStage));
        
        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        root.getChildren().addAll(title, fname, lname, email, password, register, login, error);
		
		primaryStage.setTitle("Register");
        primaryStage.setScene(scene);
        primaryStage.show();
		
	}
	
	private boolean emailExists(String email) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/txtedit", "root", "");
	        String query = "SELECT * FROM users WHERE email = ?";
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, email);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	private String generateOTP() {
	    Random rand = new Random();
	    int otp = 100000 + rand.nextInt(900000);
	    return String.valueOf(otp);
	}

    private boolean sendOTP (String em, String otp) {
        try {
        	Properties properties = System.getProperties();
            properties.put("mail.smtp.host", smtphost);
            properties.put("mail.smtp.port", smtpport);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpmail, smtppw);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(em));
            message.setSubject("OTP for Text Editor - Yuvaraj and Nithish Inc.");
            message.setContent("Your OTP for registration is: " + otp, "text/html");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean addUser(String fname, String lname, String email, String password) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/txtedit", "root", "");
            String query = "INSERT INTO users VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, fname);
            ps.setString(4, lname);
            ps.executeUpdate();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert (String title, String header, String content, String type) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (type.equals("error")) 
    		alert = new Alert(Alert.AlertType.ERROR);
        if (type.equals("info"))
            alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void goToLogin (Stage primaryStage) {
    	new Login().start(new Stage());
        primaryStage.close();  
    }

    public static void main(String[] args) {
        launch(args);
    }
}
