/**
 * @date 2018-03-26 
 * @author Algorithme Solutions inc. ©
 * @email DonavanMartin@AlgorithmeSolutions.com
 */

package Graph;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class HoveredThresholdNode extends StackPane {
    public HoveredThresholdNode(int id, Double x, Double y) {
      setPrefSize(15, 15);

      final Label label = createDataThresholdLabel(id,x,y);

      setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getChildren().setAll(label);
          setCursor(Cursor.NONE);
          toFront();
        }
      });
      setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getChildren().clear();
          setCursor(Cursor.CROSSHAIR);
        }
      });
    }

    private Label createDataThresholdLabel(int id, Double x, Double y) {
      final Label label = new Label("ID:"+id +"\nx:"+x+"\ny:"+y);
      label.getStyleClass().clear();
      label.getStyleClass().add(getClass().getResource("data.css").toExternalForm());
      label.setStyle("-fx-font-size: 10;");

      label.setTextFill(Color.DARKGRAY);

      label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
      return label;
    }
  }