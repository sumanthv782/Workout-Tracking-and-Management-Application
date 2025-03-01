// AddExerciseController.java
package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.DAOModel;
import models.Exercise;

public class AddExerciseController {
    
    @FXML
    private TableView<Exercise> addExerciseTable;
    
    @FXML
    private TableColumn<Exercise, String> colMuscleGroup;
    
    @FXML
    private TableColumn<Exercise, String> colExercise;
    
    @FXML
    private TableColumn<Exercise, String> colDifficulty;
    
    @FXML
    private TableColumn<Exercise, Integer> colReps;
    
    private DAOModel daoModel;
    private UserController clientController;
    
    public AddExerciseController() {
        daoModel = new DAOModel();
    }
    
    @FXML
    private void initialize() {
        // Initialize table columns
    	colMuscleGroup.setCellValueFactory(new PropertyValueFactory<>("muscleGroup"));
        colExercise.setCellValueFactory(new PropertyValueFactory<>("exercise"));
        colDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        colReps.setCellValueFactory(new PropertyValueFactory<>("reps"));
        
        // Fetch exercises from the database and populate the table
        addExerciseTable.setItems(daoModel.getAllExercises());
        addAddToWorkoutsButtonToTable();
    }
    
    private void addAddToWorkoutsButtonToTable() {
        TableColumn<Exercise, Void> colAddToWorkouts = new TableColumn<>("Add to Workouts");
        colAddToWorkouts.setCellFactory(param -> new TableCell<Exercise, Void>() {
            final Button addButton = new Button("Add");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(addButton);
                    addButton.setOnAction(event -> {
                        Exercise exercise = getTableView().getItems().get(getIndex());
                        addToWorkouts(exercise);
                    });
                }
            }
        });
        
        addExerciseTable.getColumns().add(colAddToWorkouts);
    }

    @FXML
    private void addToWorkouts(Exercise selectedExercise) {
        // Get pid for the selected exercise
        int pid = daoModel.getPid(selectedExercise.getMuscleGroup(), selectedExercise.getExercise(), selectedExercise.getDifficulty());
        
        
        if (daoModel.isExerciseAlreadyExists(pid, selectedExercise.getMuscleGroup(), selectedExercise.getExercise(), selectedExercise.getDifficulty())) {
            // If the exercise already exists, show an error message or handle it appropriately
        	 showAlert("Error.","","Exercise already exists.");
            return;
        }
        
     // Add the selected exercise to the user's workouts
        daoModel.addToUserProgress(selectedExercise, pid);
        
        // Add the selected exercise to the addedExercises list in the ClientController
        if (clientController != null) {
            clientController.addExercise(selectedExercise);
        }
    }	
    
    public void setClientController(UserController clientController) {
        this.clientController = clientController;
    }
    
    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
