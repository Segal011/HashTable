package performance;

import gui.*;
import java.util.Locale;
import javafx.application.Application;
import javafx.stage.Stage;
public class Execution extends Application {

    public static void main(String [] args)
    {
        Locale.setDefault(Locale.US);
        Window window = new Window();
        window.createAndShowGUI();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    }
}

