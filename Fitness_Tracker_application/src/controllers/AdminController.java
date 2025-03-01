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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.DAOModel;
import models.Exercise;
import models.UserProgress;

public class AdminController {
    
    @FXML
    private TableView<UserProgress> progressTable;
    
    @FXML
    private TableColumn<UserProgress, Integer> workoutIDColumn;
    
    @FXML
    private TableColumn<UserProgress, Integer> pidColumn;
    
    @FXML
    private TableColumn<UserProgress, String> muscleGroupColumn;
    
    @FXML
    private TableColumn<UserProgress, String> exerciseNameColumn;
    
    @FXML
    private TableColumn<UserProgress, String> difficultyColumn;
    
    @FXML
    private TableColumn<UserProgress, Integer> repsRequiredColumn;
    
    @FXML
    private TableColumn<UserProgress, Integer> repsCompletedColumn;
    
    @FXML
    private TableColumn<UserProgress, Double> progressColumn;

    private DAOModel daoModel;

	private int userId;
    
    public AdminController() {
        daoModel = new DAOModel();
    }

    @FXML
    public void initialize() {
        // Set cell value factories to populate table columns
        workoutIDColumn.setCellValueFactory(new PropertyValueFactory<>("userWorkoutID"));
        pidColumn.setCellValueFactory(new PropertyValueFactory<>("userPID"));
        muscleGroupColumn.setCellValueFactory(new PropertyValueFactory<>("userMuscleGroup"));
        exerciseNameColumn.setCellValueFactory(new PropertyValueFactory<>("userExerciseName"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("userDifficulty"));
        repsRequiredColumn.setCellValueFactory(new PropertyValueFactory<>("userRepsRequired"));
        repsCompletedColumn.setCellValueFactory(new PropertyValueFactory<>("userRepsCompleted"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("userProgress"));

        // Retrieve data from sv_user_progress table and populate the table
        ObservableList<UserProgress> userExercises = daoModel.getAllUserProgressExercises();
        
        progressTable.setItems(userExercises);
        progressTable.refresh();
    }
    
    @FXML
    private void handleUpdateWorkoutButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UpdateWorkoutsAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            UpdateWorkoutsAdminController UpdateWorkoutsAdminController = loader.getController();
            UpdateWorkoutsAdminController.setAdminController(this);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleViewAccountsButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ViewAccounts.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            ViewAccountsController viewAccountsController = loader.getController();
            viewAccountsController.setAdminController(this);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleLogoutButtonClicked() {
        // Close the current stage
        Stage stage = (Stage) progressTable.getScene().getWindow();
        stage.close();
        
        // Open the login view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public void setUserData(int userId) {
		// TODO Auto-generated method stub
		this.userId = userId;
		
	}
}
