package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.DAOModel;
import models.Exercise;
import models.User;

public class ViewAccountsController {
	
	@FXML
	private TableView<User> viewAccountsTable;
		    
	@FXML
	private TableColumn<User, Integer> userID;
		    
	@FXML
	private TableColumn<User, String> username;
		    
	@FXML
	private TableColumn<User, String> password;
		    
	private DAOModel daoModel;
	
	private AdminController adminController;
	
	public void initialize() {
	        daoModel = new DAOModel();
	        userID.setCellValueFactory(new PropertyValueFactory<>("userID"));
	        username.setCellValueFactory(new PropertyValueFactory<>("username"));
	        password.setCellValueFactory(new PropertyValueFactory<>("password"));
		        
	        // Fetch data from sv_users table and populate the table
	        ObservableList<User> userList = daoModel.getAllUsers();
	        viewAccountsTable.setItems(userList);
	}

	public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }
	
	@FXML
	private void handleResetPasswordButtonClicked() {
	    // Get the selected user from the table view
	    User selectedUser = viewAccountsTable.getSelectionModel().getSelectedItem();
	    
	    if (selectedUser != null) {
	        // Ask for confirmation
	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("Confirmation");
	        alert.setHeaderText("Reset Password");
	        alert.setContentText("Are you sure you want to reset the password for this user?");
	        
	        Optional<ButtonType> result = alert.showAndWait();
	        
	        if (result.isPresent() && result.get() == ButtonType.OK) {
	            // Reset the password for the selected user
	            boolean success = daoModel.resetPassword(selectedUser);
	            
	            if (success) {
	                // Show a success message
	                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
	                successAlert.setTitle("Success");
	                successAlert.setHeaderText(null);
	                successAlert.setContentText("Password reset successfully.");
	                successAlert.showAndWait();
	                selectedUser.setPassword("");
	                // Refresh the table view to reflect the changes
	                viewAccountsTable.refresh();
	            } else {
	                // Show an error message
	                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	                errorAlert.setTitle("Error");
	                errorAlert.setHeaderText(null);
	                errorAlert.setContentText("Failed to reset password. Please try again.");
	                errorAlert.showAndWait();
	            }
	        }
	    } else {
	        // No user selected, show a warning message
	        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
	        warningAlert.setTitle("Warning");
	        warningAlert.setHeaderText(null);
	        warningAlert.setContentText("Please select a user to reset the password.");
	        warningAlert.showAndWait();
	    }
	}


	@FXML
	private void handleDeleteUserButtonClicked() {
	    // Get the selected user from the table view
	    User selectedUser = viewAccountsTable.getSelectionModel().getSelectedItem();
	    
	    // Check if a user is selected
	    if (selectedUser != null) {
	        // Display a confirmation dialog
	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("Confirmation");
	        alert.setHeaderText("Delete User");
	        alert.setContentText("Are you sure you want to delete the selected user?");
	        
	        // Show the confirmation dialog and wait for user response
	        Optional<ButtonType> result = alert.showAndWait();
	        
	        // If the user confirms deletion
	        if (result.isPresent() && result.get() == ButtonType.OK) {
	            // Perform the deletion operation using DAOModel or any other appropriate method
	            boolean deleted = daoModel.deleteUser(selectedUser);
	            
	            // Check if the deletion was successful
	            if (deleted) {
	                // Remove the selected user from the table view
	                viewAccountsTable.getItems().remove(selectedUser);
	                // Optionally, display a success message
	                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
	                successAlert.setTitle("Success");
	                successAlert.setHeaderText(null);
	                successAlert.setContentText("User deleted successfully.");
	                successAlert.showAndWait();
	            } else {
	                // Display an error message if deletion fails
	                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	                errorAlert.setTitle("Error");
	                errorAlert.setHeaderText(null);
	                errorAlert.setContentText("Failed to delete user.");
	                errorAlert.showAndWait();
	            }
	        }
	    } else {
	        // If no user is selected, display a warning message
	        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
	        warningAlert.setTitle("Warning");
	        warningAlert.setHeaderText(null);
	        warningAlert.setContentText("Please select a user to delete.");
	        warningAlert.showAndWait();
	    }
	}


}
