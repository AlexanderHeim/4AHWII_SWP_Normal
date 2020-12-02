package FerienStuff;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.stage.Stage;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class DayBarchart extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        HashMap<String, Integer> map = WhenToWork.getFreeDays("BY", 2020);

        //Defining the axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>
                observableArrayList(Arrays.asList("Amount")));
        xAxis.setLabel("category");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("count");
        yAxis.setTickUnit(1);

        //Creating the Bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Comparison between frequency of free days");

        //Prepare XYChart.Series objects by setting data
        XYChart.Series<String, Number> mon = new XYChart.Series<>();
        mon.setName("Monday");
        mon.getData().add(new XYChart.Data<>("Amount", map.get("MONDAY")));

        XYChart.Series<String, Number> tue = new XYChart.Series<>();
        tue.setName("Tuesday");
        tue.getData().add(new XYChart.Data<>("Amount", map.get("TUESDAY")));

        XYChart.Series<String, Number> wed = new XYChart.Series<>();
        wed.setName("Wednesday");
        wed.getData().add(new XYChart.Data<>("Amount", map.get("WEDNESDAY")));

        XYChart.Series<String, Number> thu = new XYChart.Series<>();
        thu.setName("Thursday");
        thu.getData().add(new XYChart.Data<>("Amount", map.get("THURSDAY")));

        XYChart.Series<String, Number> fri = new XYChart.Series<>();
        fri.setName("Friday");
        fri.getData().add(new XYChart.Data<>("Amount", map.get("FRIDAY")));

        //Setting the data to bar chart
        barChart.getData().addAll(mon, tue, wed, thu, fri);

        //Creating a Group object
        Group root = new Group(barChart);

        //Creating a scene object
        Scene scene = new Scene(root, 600, 400);

        //Setting title to the Stage
        stage.setTitle("Holiday days");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
