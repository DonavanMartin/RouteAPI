package Graph;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextField;
import org.json.JSONException;

public class MainAppController implements Initializable {
    @FXML
    private TextField nb_comsumers;

    @FXML
    private TextField capacity;
    
    @FXML
    private LineChart<Double, Double> lineGraph;
    
    private MyGraph mathsGraph;
    
    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        lineGraph.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        mathsGraph = new MyGraph(lineGraph, 10);
    }
    
    @FXML
    private void TODO(final ActionEvent event) {
        //mathsGraph.generate();
    }
            
    @FXML
    private void connectPath(final ActionEvent event) {
        mathsGraph.connectPath();
    }
    
    @FXML
    private void optimizePath(final ActionEvent event) throws IOException, JSONException {
        int c = 0;
        try{
         c = Integer.valueOf(capacity.getText());
        }catch(Exception e){}
        mathsGraph.optimizePath(c);
    }
    
    @FXML
    private void connect(final ActionEvent event) {
        mathsGraph.connect();
    }
    
    
    @FXML
    private void generate(final ActionEvent event) {
        int nb_comsumer = Integer.valueOf(nb_comsumers.getText());
        mathsGraph.generate(nb_comsumer);
    }
    
    @FXML
    private void clear(final ActionEvent event) {
        mathsGraph.clear();
    }
}
