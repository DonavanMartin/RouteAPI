package Graph;

import API.APIpost;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.json.JSONException;

public class MyGraph {

    private APIpost api = new APIpost();
    private Integer[][] distance = null;
    private static Integer NB_POINTS = 50;
    private static String CAPACITY = "0";
    private final static double XMIN = -1000.0;
    private final static double XMAX = 1000.0;
    private final static double YMIN = -1000.0;
    private final static double YMAX = 1000.0;
    
    private XYChart.Series<Double, Double> comsumers;
    private XYChart.Series<Double, Double> depot;
    private ArrayList<Series<Double, Double>> pathSeries;
    private XYChart<Double, Double> graph;
    private double range;
    
    public MyGraph(final XYChart<Double, Double> graph, final double range) {
        this.graph = graph;
        this.range = range;
    }
    
    public void generate(int nb) {
        NB_POINTS = nb;
        
        clear();
        generateDatas();
        setDepot();
        this.graph.getStylesheets().clear();
        this.graph.getStylesheets().add(getClass().getResource("data.css").toExternalForm());
        
        setTooltip(comsumers,null);
        graph.getData().addAll(comsumers,depot);
        
    }
    
    private void setTooltip(XYChart.Series<Double, Double> s, Integer[] ids){
        int size = s.getData().size();
        for(int i = 0 ; i<size; i++){
            XYChart.Data<Double, Double> node = s.getData().get(i);
            if((i == 0) || (i == size -1)){
                s.getData().get(i).setNode(new HoveredThresholdNode(i,node.getXValue(),node.getYValue()));
            }else{
                if(ids == null){
                    s.getData().get(i).setNode(new HoveredThresholdNode(i,node.getXValue(),node.getYValue()));
                }else{
                    s.getData().get(i).setNode(new HoveredThresholdNode(ids[i-1],node.getXValue(),node.getYValue()));
                }
            }
        }
    }
    public void clear() {
        try{
            graph.getData().clear();
        }catch(Exception e){
            System.err.println("Clear error:"+e.getMessage());
        }
    }
    
    private void setDepot(){
        depot = new XYChart.Series<Double, Double>();
        depot.setName("depot");
        depot.getData().add(new XYChart.Data(0.0, 0.0));
    }
    
    private void generateDatas() {
        comsumers = new XYChart.Series<Double, Double>();
        comsumers.setName("a");
        
        for (int i = 0; i < NB_POINTS; i++) {
            comsumers.getData().add(new XYChart.Data(getRandomX(), getRandomY()));
        }
    }
    
    public void connect(){
        clear();
        this.graph.getStylesheets().clear();
        this.graph.getStylesheets().add(getClass().getResource("path.css").toExternalForm());
        
        pathSeries = new ArrayList<Series<Double, Double>>();
        for(int i = NB_POINTS-2; i < NB_POINTS ; i++){
            // Depot to comsumer
            XYChart.Series<Double, Double> dc = new XYChart.Series<>();
            dc.getData().add(new XYChart.Data(depot.getData().get(0).getXValue(),depot.getData().get(0).getYValue()));
            dc.getData().add(new XYChart.Data(comsumers.getData().get(i).getXValue(),comsumers.getData().get(i).getYValue()));
            
            // Comsumer to Comsumer
            for(int j = i-1; j < NB_POINTS ; j++){
                dc.getData().add(new XYChart.Data(comsumers.getData().get(i).getXValue(),comsumers.getData().get(i).getYValue()));
                dc.getData().add(new XYChart.Data(comsumers.getData().get(j).getXValue(),comsumers.getData().get(j).getYValue()));
            }
            dc.getData().add(new XYChart.Data(depot.getData().get(0).getXValue(),depot.getData().get(0).getYValue()));
            setTooltip(dc,null);
            pathSeries.add(dc);
        }
        
        graph.getData().addAll(pathSeries);
    }
    
    public void connectPath(){
        clear();
        this.graph.getStylesheets().clear();
        this.graph.getStylesheets().add(getClass().getResource("path.css").toExternalForm());
        
        pathSeries = new ArrayList<Series<Double, Double>>();
        for(int i = 0 ; i<api.result.length; i++){
            XYChart.Series<Double, Double> dc = getSeries(api.result[i]);
            setTooltip(dc,null);
            pathSeries.add(dc);
        }
        graph.getData().addAll(pathSeries);
    }
    
    
    public void optimizePath(Integer cap) throws IOException, JSONException{
        CAPACITY = String.valueOf(cap);
        clear();
        this.graph.getStylesheets().clear();
        this.graph.getStylesheets().add(getClass().getResource("path.css").toExternalForm());
        pathSeries = new ArrayList<Series<Double, Double>>();
        calculDistance();
        
        String postResult = api.post("\""+api.key+"\"",
                getDistancePost(),
                getComsumeTimePost(),
                "[]", CAPACITY, "20000");
        
        if(postResult.contains("error") ){
            JOptionPane.showMessageDialog(new JFrame("Request error return"), postResult);
        }else{
            for(int i = 0 ; i<api.result.length; i++){
                XYChart.Series<Double, Double> dc = getSeries(api.result[i]);
                setTooltip(dc,api.result[i]);
                pathSeries.add(dc);
            }
            graph.getData().addAll(pathSeries);
        }
    }
    
    private XYChart.Series<Double, Double> getSeries(Integer[] path){
        int pathL = path.length;
        if(pathL > 0){
            XYChart.Series<Double, Double> dc = new XYChart.Series<>();
            dc.getData().add(new XYChart.Data(depot.getData().get(0).getXValue(),depot.getData().get(0).getYValue()));
            for(int i = 0; i < pathL ; i++){
                int idConsumer = path[i];
                dc.getData().add(new XYChart.Data(comsumers.getData().get(idConsumer-1).getXValue(),comsumers.getData().get(idConsumer-1).getYValue()));
            }
            dc.getData().add(new XYChart.Data(depot.getData().get(0).getXValue(),depot.getData().get(0).getYValue()));
            return dc;
        }else{
            System.err.println("Get Path Series but path = "+pathL);
            return null;
        }
    }
    
    private double getRandomX(){
        Random r = new Random();
        return XMIN + (XMAX - XMIN) * r.nextDouble();
    }
    
    private double getRandomY(){
        Random r = new Random();
        return YMIN + (YMAX - YMIN) * r.nextDouble();
    }
    
    public void calculDistance(){
        int l = comsumers.getData().size();
        distance = new Integer[l+1][l+1];
        
        distance[0][0] = 0;
        // Depot
        for(int j = 0 ; j<l; j++ ){
            Integer v = calculDistance(depot.getData().get(0), comsumers.getData().get(j));
            distance[0][j+1] = v;
            distance[j+1][0] = v;
        }
        
        // Comsumer
        for(int i = 0 ; i<l; i++ ){
            for(int j = i ; j<l; j++ ){
                Integer v = calculDistance(comsumers.getData().get(i), comsumers.getData().get(j));
                if(i==j){
                    distance[i+1][j+1] = v;
                }else{
                    distance[i+1][j+1] = v;
                    distance[j+1][i+1] = v;
                }
            }
        }
    }
    
    public Integer calculDistance(Data p1, Data p2){
        double a = Math.pow(((double)p2.getXValue()-(double)p1.getXValue()),2.0);
        double b = Math.pow(((double)p2.getYValue()-(double)p1.getYValue()),2.0);
        return ((Double)(Math.sqrt(a+b))).intValue();
    }
    
    /// API
    private String getDistancePost(){
        String s = "";
        s += "[";
        s += Arrays.stream(distance).map(Arrays::toString) .collect(Collectors.joining(","));
        s += "]";
        System.out.println("Request:"+s);
        return s;
    }
    
    private String getComsumeTimePost(){
        String s = "";
        s += "[";
        int l = comsumers.getData().size();
        for(int i = 0 ; i<l+1; i++ ){
            s += String.valueOf(0);
            if(i<l){
                s += ",";
            }
        }
        s += "]";
        System.out.println("Request:"+s);
        return s;
    }
}
