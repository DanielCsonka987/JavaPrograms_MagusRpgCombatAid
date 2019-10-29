package application;

import java.awt.Desktop.Action;
import java.io.StringWriter;
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

    @FXML
    private Label lblAreaInfo;
    
    private MagusDamageAidLogic service;
	private String chosenWeaponType;
	private String chosenAreaGroup;
	private String chosenHPValue;
	private Integer textBreakLineLength = 40;
	private Integer lblBreakLineLength = 25;
	
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
    	
    	String[] cellPairResult = service.getTheCellPari_OfStrictAreaAndHP("", chosenHPValue);
    	String cellCommentResult = service.getTheStirctComment_OfArea();
    	loadDetailsToAreass(cellPairResult, cellCommentResult);
    }

    @FXML
    void defineWithGivenArea(ActionEvent event) {

    	if(fillingInStatesRevisor(FormCompliteness.CHOSEN_AREA))
    		return;
    	String chosenArea = cmbBxAttackArea.getSelectionModel().getSelectedItem();
    	
    	String[] cellPairResult = service.getTheCellPari_OfStrictAreaAndHP(chosenArea, chosenHPValue);
    	String cellCommentResult = service.getTheStirctComment_OfArea();
    	loadDetailsToAreass(cellPairResult, cellCommentResult);
    }

    @FXML
    void loadInAreaGroupDetails(ActionEvent event) {

    	if(fillingInStatesRevisor(FormCompliteness.CHOSEN_AREAGROUP))
    		return;
    	chosenAreaGroup = cmbBxAttackAreaGroup.getSelectionModel().getSelectedItem();
    	cmbBxAttackArea.itemsProperty().set(service.getTheStrictAreaRow_OfChosenAreaGroup(chosenAreaGroup));
    	cmbBxAttackStrength.itemsProperty()
			.set(service.getTheStrinctAreaGroup_HPRow());
    	String tableComment = service.getTheStictComment_OfAreaGroup();
    	lblInfoOfTable.setText(breakLinesOfString_ToBeDecorative(tableComment, false));
    }

    @FXML
    void loadInTheAreaCollection(ActionEvent event) {

    	if(fillingInStatesRevisor(FormCompliteness.CHOSEN_WEAPON))
    		return;
    	chosenWeaponType = cmbBxWeaponTypes.getSelectionModel().getSelectedItem();
    	cmbBxAttackAreaGroup.itemsProperty()
    		.set(service.getTheAreaCollection_OfChosenWeapon(chosenWeaponType));;
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
    		chosenHPValue = cmbBxAttackStrength.getSelectionModel().getSelectedItem();
    	}
    	if(state == FormCompliteness.CHOSEN_AREA){
    		if(cmbBxAttackArea.getSelectionModel().isEmpty())
    			return true;
    	}
    	return false;
    }
    
    private enum FormCompliteness { CHOSEN_WEAPON, CHOSEN_AREAGROUP, CHOSEN_HPorAREA, CHOSEN_AREA }
    
    private void loadDetailsToAreass(String[] cellPair, String comment){
    	txtAreaDescript.setText(breakLinesOfString_ToBeDecorative(cellPair[0], true));
    	txtAreaEffect.setText(breakLinesOfString_ToBeDecorative(cellPair[1], true));
    	lblInfoOfRow.setText(breakLinesOfString_ToBeDecorative(comment, false));
    	lblAreaInfo.setText(service.getTheChosenArea_OfManagedProcess());
    }
    
    private String breakLinesOfString_ToBeDecorative(String datas, Boolean type){
    	
    	Integer actLenghtPermitted = type?textBreakLineLength:lblBreakLineLength;
    	if(datas == null)
    		return "";
    	if(datas.length() < actLenghtPermitted)
    		return datas;
    	
    	StringWriter strwr = new StringWriter();
    	Integer rowCounter = 1;
    	String[] pieces = datas.split(" ");
    	for(int i = 0; i < pieces.length; i++){
    		strwr.write(pieces[i]);
    		if(strwr.getBuffer().length() >= (actLenghtPermitted * rowCounter)){
    			strwr.write("\n");
    			rowCounter++;
    		} else {
    			strwr.write(" ");
    		}
    	}
    	strwr.flush();
    	return strwr.toString();
    }
    
}
