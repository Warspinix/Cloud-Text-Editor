package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class Editor extends Application {
	private TextArea textArea;
    private TextField fontSizeField;
    private ToggleButton boldButton, italicButton;
    private ComboBox<String> fontComboBox;
    private ToggleButton darkModeButton;
    private boolean isDarkMode = false; 
    
    String fname, lname, em, file, path = "C:\\Users\\Yuvaraj\\Desktop\\My Space\\Java\\Text Editor\\";
	
    public Editor (String fname, String lname, String em) {
   	    this.fname = fname;
   	    this.lname = lname;
   	    this.em = em;
   	    file = "Untitled Document"; 
   	}
   	
   	public Editor (String fname, String lname, String em, String file) {
   		this.fname = fname;
        this.lname = lname;
        this.em = em;
        this.file = file;
   	}
   	
    @Override
    public void start(Stage primaryStage) {
        try {
            
        	path = getFullPath();
        	
            BorderPane root = new BorderPane();

            textArea = new TextArea();
            textArea.setId("text-area");
            textArea.setFont(Font.font("Times New Roman", 12));
            root.setCenter(textArea);

            ToolBar fileToolBar = new ToolBar();

            Button openButton = new Button("Open");
            Button saveButton = new Button("Save");
            Button saveAsButton = new Button("Save As");
            Button renameButton = new Button("Rename");
            Button logoutButton = new Button("Logout");
            
            openButton.getStyleClass().add("fbutton");
            saveButton.getStyleClass().add("fbutton");
            saveAsButton.getStyleClass().add("fbutton");
            renameButton.getStyleClass().add("fbutton");
            logoutButton.getStyleClass().add("fbutton");

            fileToolBar.getItems().addAll(openButton, saveButton, saveAsButton, renameButton, logoutButton);

            darkModeButton = new ToggleButton("Dark Mode");
            darkModeButton.setOnAction(e -> toggleDarkMode());
            darkModeButton.getStyleClass().add("DarkModebutton");
            
            fileToolBar.getItems().add(darkModeButton);

            ToolBar formatToolBar = new ToolBar();

            fontComboBox = new ComboBox<>();
            fontComboBox.getItems().addAll("Times New Roman", "Courier New", "Arial", "Calibri");
            fontComboBox.setValue("Times New Roman");
            fontComboBox.setOnAction(e -> updateTextStyle());
            
            fontSizeField = new TextField("12");
            fontSizeField.setPrefWidth(50);
            fontSizeField.setOnAction(e -> updateTextStyle());

            Button incrementSizeButton = new Button("+");
            incrementSizeButton.setOnAction(e -> adjustFontSize(1));

            Button decrementSizeButton = new Button("-");
            decrementSizeButton.setOnAction(e -> adjustFontSize(-1));

            boldButton = new ToggleButton("B");
            boldButton.setStyle("-fx-font-weight: bold; -fx-font-family: 'Times New Roman';");
            boldButton.setOnAction(e -> updateTextStyle());

            italicButton = new ToggleButton("I");
            italicButton.setStyle("-fx-font-style: italic; -fx-font-family: 'Times New Roman';");
            italicButton.setOnAction(e -> updateTextStyle());
            
            incrementSizeButton.getStyleClass().add("font-size-button");
            decrementSizeButton.getStyleClass().add("font-size-button");
            boldButton.getStyleClass().add("bold-button");
            italicButton.getStyleClass().add("italic-button");
            fontSizeField.getStyleClass().add("font-size-field");

            formatToolBar.getItems().addAll(
                    fontComboBox,
                    decrementSizeButton,
                    fontSizeField,
                    incrementSizeButton,
                    boldButton,                   
                    italicButton
            );
            
            VBox toolbars = new VBox(fileToolBar, formatToolBar);
            root.setTop(toolbars);

            openButton.setOnAction(e -> openFile(primaryStage));
            saveButton.setOnAction(e -> saveFile(primaryStage));
            saveAsButton.setOnAction(e -> saveAsFile(primaryStage));
            renameButton.setOnAction(e -> renameFile(primaryStage));
            logoutButton.setOnAction(e -> logout());
            
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Untitled Document");
            
            if (!file.equals("Untitled Document")) {
            	loadFile();
                primaryStage.setTitle(file);
            }

            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String header, String content, String type) {
    	Alert alert = null;
    	if (type.equals("error")) 
    		alert = new Alert(Alert.AlertType.ERROR);
    	else if (type.equals("info"))
    		alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void loadFile() {
        try {
        	String fn = path + file + ".txt";
            Path filePath = Paths.get(fn);
            String content = Files.readString(filePath); 
            textArea.setText(content); 
            textArea.positionCaret(0); 
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "File Load Error", "Could not load the selected file.", "error");
        }
    }
    
    private String getFullPath () {
    	String p1 = em.substring(0, em.indexOf("@"));
   	    String p2 = em.substring(em.indexOf("@") + 1, em.indexOf(".", em.indexOf("@")));
   	    return path + p1 + p2 + "\\";
    }

    private void adjustFontSize(int delta) {
        try {
            int size = Integer.parseInt(fontSizeField.getText()) + delta;
            size = Math.max(1, size);
            fontSizeField.setText(String.valueOf(size));
            updateTextStyle();
        } catch (NumberFormatException e) {
            fontSizeField.setText("12");
        }
    }

    private void updateTextStyle() {
        try {
            String fontFamily = fontComboBox.getValue();

            int fontSize = Integer.parseInt(fontSizeField.getText());
            fontSize = Math.max(1, fontSize); 

            FontWeight weight = boldButton.isSelected() ? FontWeight.BOLD : FontWeight.NORMAL;
            FontPosture posture = italicButton.isSelected() ? FontPosture.ITALIC : FontPosture.REGULAR;

            Font font = Font.font(fontFamily, weight, posture, fontSize);
            textArea.setFont(font);  
        } catch (NumberFormatException e) {
            fontSizeField.setText("12"); 
        }
    }

    private void toggleDarkMode() {
        Scene currentScene = textArea.getScene();

        if (isDarkMode) {
            currentScene.getRoot().setStyle("-fx-background-color: white;");
            textArea.setStyle("-fx-control-inner-background: white; -fx-background-color: white; -fx-text-fill: black;");
            darkModeButton.setStyle(
                    "-fx-background-color: transparent; " +
                    "-fx-text-fill: #4A90E2; " +
                    "-fx-font-size: 14px; " +
                    "-fx-border-width: 0 1px 0 0; " +
                    "-fx-border-color: #4A90E2; " +
                    "-fx-border-style: solid; " +
                    "-fx-padding: 5px 10px; " +
                    "-fx-border-radius: 2px;"
                );
            isDarkMode = false;
        } else {
            currentScene.getRoot().setStyle("-fx-background-color: #2b2b2b;");
            textArea.setStyle("-fx-control-inner-background: #2b2b2b; -fx-background-color: #2b2b2b; -fx-text-fill: white;");
            darkModeButton.setStyle("-fx-background-color: blue;" + "-fx-text-fill: white");
            isDarkMode = true;
        }
        updateTextStyle();
    }

    private void openFile(Stage stage) {
    	new FileManagement(fname, lname, em).start(new Stage());
    }

    private void saveFile(Stage stage) {
    	if (file.equals("Untitled Document")) {
    		saveAsFile(stage);
    	} else {
    		try {
		        String fp = path + file + ".txt";
		        Files.write(Paths.get(fp), textArea.getText().getBytes(StandardCharsets.UTF_8));
		        showAlert("File Saved", null, "The file has been saved successfully!", "info");
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	        showAlert("Error", "Unable to Save File", "An error occurred while saving the file. Please try again.", "error");
    	    }
    	}
    }

    private void saveAsFile(Stage stage) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Save As");

        VBox content = new VBox(10);
        TextField fileNameField = new TextField();
        fileNameField.setPromptText("Enter filename");

        content.getChildren().addAll(fileNameField);
        dialog.getDialogPane().setContent(content);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String fileName = fileNameField.getText();
                if (!fileName.isEmpty()) {
                	return fileNameField.getText();
                }
            }
            return null;
        });
        
        dialog.showAndWait();
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(fileName -> {
            if (!fileName.isEmpty()) {
                String fp = path + fileName + ".txt";
		        try {
		            Files.write(Paths.get(fp), textArea.getText().getBytes(StandardCharsets.UTF_8));
                    String insert = "INSERT INTO files VALUES (?, ?)";
	            	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/txtedit", "root", "");
	                PreparedStatement st = con.prepareStatement(insert);
	
	                st.setString(1, em); 
	                st.setString(2, fp);
	                st.executeUpdate();
	                
	                showAlert("Save As Successful", null, "File saved successfully!", "info");
	                stage.setTitle(fileName);
		        } catch (SQLException se) {
		        	 if (se.getErrorCode() == 1062) {
		                 showAlert("File Already Exists", null, "File already exists!", "error");
		             } else {
		                 se.printStackTrace();
		                 showAlert("Database Error", null, "Failed to save file details to the database.", "error");
		             }
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("File Save Error", null, "An error occurred while saving the file.", "error");
                }
            } else {
                showAlert("Invalid File Name", null, "The file name cannot be empty.", "error");
            }
        });
    }

    private void renameFile(Stage stage) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Rename File");

        VBox content = new VBox(10);
        TextField fileNameField = new TextField();
        fileNameField.setPromptText("Enter new filename");

        content.getChildren().addAll(fileNameField);
        dialog.getDialogPane().setContent(content);

        ButtonType renameButtonType = new ButtonType("Rename", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(renameButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == renameButtonType) {
                String newf = fileNameField.getText();
                
                if (!newf.isEmpty()) {
                	try {
                        String oldfp = path + file + ".txt"; 
                        File oldFile = new File(oldfp);
                        String newfp = path + newf + ".txt";
                        
                        File newFile = new File(newfp);
                        if (oldFile.exists() && oldFile.renameTo(newFile)) {
                            System.out.println("File renamed to " + newf);
                            
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/txtedit", "root", "");
                        String update = "UPDATE files SET filepath = ? WHERE email = ? AND filepath = ?";
                        PreparedStatement st = con.prepareStatement(update);
                        st.setString(1, newfp); 
                        st.setString(2, em); 
                        st.setString(3, oldfp); 
                        
                        if (st.executeUpdate() > 0) {
                            System.out.println("Database updated with new file path");
                        } else {
                            System.out.println("Database update failed");
                        }
                        stage.setTitle(newf);
                        showAlert("Rename Successful", null, "File renamed successfully!", "info");
                        } else {
                            System.out.println("Rename failed");
                            showAlert("Rename Error", null, "Failed to rename the file on the disk.", "error");
                        }
                    } catch (SQLException se) {
                    	if (se.getErrorCode() == 1062) {
                    		showAlert("File Already Exists", null, "File already exists!", "error");
                    	} else {
                    		se.printStackTrace();
   		                 showAlert("Database Error", null, "Failed to save file details to the database.", "error");
   		             	}
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert("Error", null, "An error occurred while renaming the file.", "error");
                    }
                } else {
                    showAlert("Invalid File Name", null, "The file name cannot be empty.", "error");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
    
    private void logout() {
    	 try {
	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("Confirm Logout");
	        alert.setHeaderText("Are you sure you want to log out?");
	        alert.setContentText("Any unsaved changes will be lost.");
	
	        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
	
	        if (result == ButtonType.OK) {
	            Stage current = (Stage) textArea.getScene().getWindow(); 
	            current.close();
	
	            new Login().start(new Stage());
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
    }

    public static void main(String[] args) {
        launch(args);
    }
}