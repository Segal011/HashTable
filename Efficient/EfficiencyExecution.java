package Efficient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;

import javafx.stage.Stage;

import java.util.ArrayList;

public class EfficiencyExecution extends Application  {

    @Override public void start(Stage stage) {
        Efficient.Efficiency e = new Efficient.Efficiency();
        ArrayList<Long> data = e.times;
        stage.setTitle("Bar Chart Sample");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Hash Maps Efficiency");
        xAxis.setLabel("Amount of research");
        yAxis.setLabel("Speed");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Hash Table (Participant)");
        series1.getData().add(new XYChart.Data("10000",  data.get(0)));
        series1.getData().add(new XYChart.Data("20000",  data.get(1)));
        series1.getData().add(new XYChart.Data("40000",  data.get(2)));
        series1.getData().add(new XYChart.Data("80000",  data.get(3)));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Hash Table With Double Hashing (Participant)");
        series2.getData().add(new XYChart.Data("10000",  data.get(4)));
        series2.getData().add(new XYChart.Data("20000",  data.get(5)));
        series2.getData().add(new XYChart.Data("40000",  data.get(6)));
        series2.getData().add(new XYChart.Data("80000",  data.get(7)));

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Hash Table (String)");
        series3.getData().add(new XYChart.Data("10000",  data.get(8)));
        series3.getData().add(new XYChart.Data("20000",  data.get(9)));
        series3.getData().add(new XYChart.Data("40000",  data.get(10)));
        series3.getData().add(new XYChart.Data("80000",  data.get(11)));

        XYChart.Series series4 = new XYChart.Series();
        series4.setName("Hash Table With Double Hashing (String)");
        series4.getData().add(new XYChart.Data("10000",  data.get(12)));
        series4.getData().add(new XYChart.Data("20000",  data.get(13)));
        series4.getData().add(new XYChart.Data("40000",  data.get(14)));
        series4.getData().add(new XYChart.Data("80000",  data.get(15)));

        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1, series2, series3, series4);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}