package controllers;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;
import models.DAOModel;

public class LoginController {
    
    @FXML
    private TextField txtUsername;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Label lblError;
    
    private DAOModel daoModel;
    
    public LoginController() {
        daoModel = new DAOModel();
    }
    
    @FXML
    private void login() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        
        // Authenticate user
        Pair<Boolean, Pair<Integer, String>> authResult = daoModel.authenticateUser(username, password);
        
        if (authResult.getKey()) {
        	lblError.setText("Login Successful!");
        	 Pair<Integer, String> userData = authResult.getValue();
        	    int userId = userData.getKey();
        	    String role = userData.getValue();
        	    String message = username + " with role " + role + " has logged in successfully.";
                JOptionPane.showMessageDialog(null, message, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            if ("user".equals(role)) {
                // Open ClientView
            	System.out.println(userId);
            	openClientView(userId);
            } else if ("admin".equals(role)) {
                // Open AdminView
            	System.out.println(userId);
                openAdminView(userId);
            }
        } else {
            // Display error message
            lblError.setText("Invalid username or password.");
        }
    }
    
    private void openClientView(int userId) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClientView.fxml"));
            Parent root = loader.load();
            
            UserController controller = loader.getController();
            controller.setUserData(userId);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Client View");
            stage.show();

            // Close the login window
            Stage loginStage = (Stage) lblError.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void openAdminView(int userId) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminView.fxml"));
            Parent root = loader.load();
            
            AdminController controller = loader.getController();
            controller.setUserData(userId);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin View");
            stage.show();

            // Close the login window
            Stage loginStage = (Stage) lblError.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
