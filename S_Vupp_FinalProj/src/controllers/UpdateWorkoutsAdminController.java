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
import javafx.scene.control.Alert.AlertType;
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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.util.Optional;

public class UpdateWorkoutsAdminController {
    
	@FXML
    private TableView<Exercise> updateWorkoutsTable;
    
    @FXML
    private TableColumn<Exercise, String> colMuscleGroup;
    
    @FXML
    private TableColumn<Exercise, String> colExercise;
    
    @FXML
    private TableColumn<Exercise, String> colDifficulty;
    
    @FXML
    private TableColumn<Exercise, Integer> colReps;
    
    
    private DAOModel daoModel;
    
    private List<Exercise> addedWorkouts = new ArrayList<>();

	private AdminController adminController;	
	
    public UpdateWorkoutsAdminController() {
        daoModel = new DAOModel();
    }
    
    @FXML
    private void initialize() {
    	
    	
    	 colMuscleGroup.setCellValueFactory(new PropertyValueFactory<>("muscleGroup"));
         colExercise.setCellValueFactory(new PropertyValueFactory<>("exercise"));
         colDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
         colReps.setCellValueFactory(new PropertyValueFactory<>("reps"));
         
      // Fetch exercises from the database and populate the table
         updateWorkoutsTable.setItems(daoModel.getAllExercises());
    }
    
    @FXML
    private void addExerciseDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Exercise");
        dialog.setHeaderText("Add a New Exercise");

        ButtonType addButton = new ButtonType("Add", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        Label muscleGroupLabel = new Label("Muscle Group:");
        TextField muscleGroupField = new TextField();
        Label exerciseLabel = new Label("Exercise:");
        TextField exerciseField = new TextField();
        Label difficultyLabel = new Label("Difficulty:");
        TextField difficultyField = new TextField();
        Label repsLabel = new Label("Reps:");
        TextField repsField = new TextField();

        GridPane grid = new GridPane();
        grid.add(muscleGroupLabel, 0, 0);
        grid.add(muscleGroupField, 1, 0);
        grid.add(exerciseLabel, 0, 1);
        grid.add(exerciseField, 1, 1);
        grid.add(difficultyLabel, 0, 2);
        grid.add(difficultyField, 1, 2);
        grid.add(repsLabel, 0, 3);
        grid.add(repsField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return new Pair<>(muscleGroupField.getText(), exerciseField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            String muscleGroup = pair.getKey();
            String exercise = pair.getValue();
            String difficulty = difficultyField.getText();
            int reps = Integer.parseInt(repsField.getText());

            if (isValidInput(muscleGroup, exercise, difficulty, reps)) {
                daoModel.insertExercise(muscleGroup, exercise, difficulty, reps);
                loadData();
            } else {
                showAlert("Error", "Invalid Input", "Please enter valid values for all fields.");
            }
        });
    }
    
    @FXML
    private void handleUpdateExerciseButtonClicked() {
        // Iterate over the items in the table
    	Exercise exerciseToUpdate1 = updateWorkoutsTable.getSelectionModel().getSelectedItem();
        if (exerciseToUpdate1 != null) {
        	 
        	
        	TextInputDialog dialog = new TextInputDialog("Enter reps required");
            dialog.setTitle("Update Reps required");
            dialog.setHeaderText("Update Reps required");
            dialog.setContentText("Please enter the new value for reps required:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
            	try {
                    // Parse the input string to get the new reps completed value
                    int newRepsRequired = Integer.parseInt(result.get());
                    
                    exerciseToUpdate1.setReps(Integer.parseInt(result.get()));
                    
                    daoModel.updateRepsRequired(exerciseToUpdate1);
                    updateWorkoutsTable.refresh();
                    int repsRequired = exerciseToUpdate1.getReps();
                    System.out.println(repsRequired);
                                        
                } catch (NumberFormatException e) {
                    // Handle invalid input
                    System.out.println("Invalid input for reps required.");
                }
            }
            
        } else {
            // Handle case when no Exercise is selected
        	showAlert("Error", "Select Row", "Please select an exercise to update reps required.");
        }
        
    }
    
    @FXML
    private void handleDeleteExerciseButtonClicked() {
        Exercise exerciseToDelete1 = updateWorkoutsTable.getSelectionModel().getSelectedItem();
        
        if (exerciseToDelete1 != null) {
            daoModel.deleteWorkoutByParams(exerciseToDelete1.getMuscleGroup(), exerciseToDelete1.getExercise(), exerciseToDelete1.getDifficulty());
            updateWorkoutsTable.getItems().remove(exerciseToDelete1);
            updateWorkoutsTable.refresh();
        }

    }

    private boolean isValidInput(String muscleGroup, String exercise, String difficulty, int reps) {
        return !muscleGroup.isEmpty() && !exercise.isEmpty() && !difficulty.isEmpty() && reps > 0;
    }

    private void loadData() {
        // Load data into the table
        updateWorkoutsTable.getItems().clear();
        updateWorkoutsTable.getItems().addAll(daoModel.getUserProgressExercises());
    }
    
    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }
    
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
