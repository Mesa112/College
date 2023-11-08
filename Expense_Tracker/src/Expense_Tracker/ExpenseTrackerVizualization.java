import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExpenseTrackerApplication extends Application {

    private ExpenseTracker tracker = new ExpenseTracker();
    private boolean initialAmountAdded = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Tracker Application");

        Button btnAddInitialAmount = new Button();
        btnAddInitialAmount.setText("Add Initial Amount");
        btnAddInitialAmount.setOnAction(event -> {
            if (!initialAmountAdded) {
                // Here you would open a dialog to input the initial amount
                // For simplicity, let's say the initial amount is 1000
                double initialAmount = scanner.nextDouble(); // This would come from a dialog
                tracker.addTransaction("Income", initialAmount, "Initial", "N/A");
                initialAmountAdded = true;
                btnAddInitialAmount.setDisable(true); // Disable the button after adding initial amount
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btnAddInitialAmount);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}