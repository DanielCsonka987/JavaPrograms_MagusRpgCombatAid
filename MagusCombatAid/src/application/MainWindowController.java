package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MainWindowController implements Initializable{

    @FXML
    private Tab damageAid;

    @FXML
    private Pane paneControlArea;

    @FXML
    private ComboBox<String> cmbBxWeaponTypes;

    @FXML
    private ComboBox<String> cmbBxAttackAreaGroup;

    @FXML
    private ComboBox<String> cmbBxAttackStrength;

    @FXML
    private ComboBox<String> cmbBxAttackArea;

    @FXML
    private Label lblInfoOfTable;
    
    @FXML
    private Button btnRollD10Alone;

    @FXML
    private AnchorPane paneInfoArea;

    @FXML
    private TextArea txtAreaDescript;

    @FXML
    private TextArea txtAreaEffect;

    @FXML
    private Label lblInfoOfRow;

    @FXML
    void defineTheStrictArea(ActionEvent event) {

    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}

}
