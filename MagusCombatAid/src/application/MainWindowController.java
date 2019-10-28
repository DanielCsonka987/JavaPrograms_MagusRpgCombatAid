package application;

import java.awt.Desktop.Action;
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
    private Button btnRollD10Alone;

    @FXML
    private ComboBox<String> cmbBxAttackStrength;

    @FXML
    private ComboBox<String> cmbBxAttackArea;

    @FXML
    private Label lblInfoOfTable;

    @FXML
    private Button btnAreaDefined;

    @FXML
    private Button btnClear;

    @FXML
    private AnchorPane paneInfoArea;

    @FXML
    private TextArea txtAreaDescript;

    @FXML
    private TextArea txtAreaEffect;

    @FXML
    private Label lblInfoOfRow;


    private MagusDamageAidLogic service;
	private String chosenWeaponType;
	private String chosenAreaGroup;
	private String chosenHPValue;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		service = new MagusDamageAidLogic();
		cmbBxWeaponTypes.setItems(service.getWeaponTypes());
	}

	
	
    @FXML
    void clearAreas(ActionEvent event) {

    	txtAreaDescript.setText("");
    	txtAreaEffect.setText("");
    	lblInfoOfRow.setText("");
    	if(!cmbBxAttackArea.getSelectionModel().isEmpty())
    		cmbBxAttackArea.getSelectionModel().clearSelection();
    }

    @FXML
    void defineTheStrictArea(ActionEvent event) {

    	if(fillingInStatesRevisor(FormCompliteness.CHOSEN_HPorAREA))
    		return;
    	
    	String[] cellPairResult = service.getTheCellPariOfStrictAreaAndHP("", chosenHPValue);
    	String cellCommentResult = service.getTheStirctCommentOfArea();
    	loadDetailsToFields(cellPairResult, cellCommentResult);
    }

    @FXML
    void defineWithGivenArea(ActionEvent event) {

    	if(fillingInStatesRevisor(FormCompliteness.CHOSEN_AREA))
    		return;
    	String chosenArea = cmbBxAttackArea.getSelectionModel().getSelectedItem();
    	
    	String[] cellPairResult = service.getTheCellPariOfStrictAreaAndHP(chosenArea, chosenHPValue);
    	String cellCommentResult = service.getTheStirctCommentOfArea();
    	loadDetailsToFields(cellPairResult, cellCommentResult);
    }

    @FXML
    void loadInAreaGroupDetails(ActionEvent event) {

    	if(fillingInStatesRevisor(FormCompliteness.CHOSEN_AREAGROUP))
    		return;
    	chosenAreaGroup = cmbBxAttackAreaGroup.getSelectionModel().getSelectedItem();
    	cmbBxAttackStrength.itemsProperty()
			.set(service.getTheStrinctAreaGroupHPRow());
    	cmbBxAttackArea.itemsProperty().set(service.getTheStrictAreaRowFromChosenAreaGroup(chosenAreaGroup));
    }

    @FXML
    void loadInTheAreaCollection(ActionEvent event) {

    	if(fillingInStatesRevisor(FormCompliteness.CHOSEN_WEAPON))
    		return;
    	chosenWeaponType = cmbBxWeaponTypes.getSelectionModel().getSelectedItem();
    	cmbBxAttackAreaGroup.itemsProperty()
    		.set(service.getTheAreaCollectionOfChosenWeapon(chosenWeaponType));;
    	String tableComment = service.getTheStictCommentOfAreaGroup();
    	lblInfoOfTable.setText(tableComment);
    }

    private Boolean fillingInStatesRevisor(FormCompliteness state){
    	
    	if(state == FormCompliteness.CHOSEN_WEAPON){
        	if(cmbBxWeaponTypes.getSelectionModel().isEmpty())
        		return true;
    	}
    	if(state == FormCompliteness.CHOSEN_AREAGROUP){
        	if(cmbBxAttackAreaGroup.getSelectionModel().isEmpty())
        		return true;
    	}
    	if(state == FormCompliteness.CHOSEN_HPorAREA){
    		if(cmbBxAttackStrength.getSelectionModel().isEmpty())
    			return true;
    	}
    	if(state == FormCompliteness.CHOSEN_AREA){
    		if(cmbBxAttackArea.getSelectionModel().isEmpty())
    			return true;
    	}
    	return false;
    }
    
    private enum FormCompliteness { CHOSEN_WEAPON, CHOSEN_AREAGROUP, CHOSEN_HPorAREA, CHOSEN_AREA }
    
    private void loadDetailsToFields(String[] cellPair, String comment){
    	txtAreaDescript.setText(cellPair[0]);
    	txtAreaEffect.setText(cellPair[1]);
    	lblInfoOfRow.setText(comment);
    }
    
}
