package application;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MagusDamageAidLogic {

	private static MagusDamageAidLogic logic;
	private static Map<String,String> weaponTypes;	//KEY IS THE PRESENTED VALUE OF DIFFERENT WEEAPONS - FROM COLUMNS
	private static Map<String,String> chosen_AreaCollectionColumns;	//KEY IS THE PRESENTED VALUE OF DIFFERENT AREAS	- FROM COLUMNS
	private static Map<String,String> chosen_AreaCollectionHPRow;	//KEY IS THE PRESENTED VALUE OF HP-CHATEGROIES	- FROM A ROW
	private static Map<String,String> chosen_AreasAndDicesGoup;	//KEY IS THE AREAS OF THAT AREA-GROUP
	
	private static String starterTableOfDB = "type_damages";
	
	private static String damage_ChosenAreaCollectionTableName;	//TABLE NAME OF AREA-COLLECTION
	private static String damage_ChosenAreaGroupTableName;		//TABLE NAME OF AREA-GROUP
	private static String damage_ChosenAreaGroupName;			//STRING OF AREA-GROUP
	private static String damage_ChosenArea;
	
	private static String changeKeyWordFromHPHeaderToEffectHeader = "affect";
	
	private static String queryToGetWeaponTypes = 
			"SELECT weapon_type, damages_group FROM @;";
	
	//AREA COLLECTION TABLE
	private static String queryToGetChosenAreaCollectionColumns = 
			"SELECT area_group, tableName  FROM @;";
	private static String queryToGetAreaCollectionHPRow_longer = 
			"SELECT sp_damage_mild, sp_damage_serious, sp_damage_dangerous, sp_damage_lethal FROM @ WHERE area_group='@';";
	//private static String queryToGetAreaCollectionHPRow_shorter = 
		//	"SELECT sp_damage_mild, sp_damage_serious, sp_damage_dangerous FROM @ WHERE area_group='@';";
	private static String queryToGetAreaCollectionRowComment = 
			"SELECT commenting FROM @ WHERE tableName='@';";
	
	//AREA GROUP TABLE
	private static String queryToGetAreasGroupAreasAndDices = 
			"SELECT area, dice FROM @;";
	private static String queryToGetChosenAreaCellPair = 
			"SELECT @, @ FROM @ WHERE area='@';";
	private static String queryTOGetChosenAreaComment = 
			"SELECT commenting FROM @ WHERE area='@';";
	
	
	public static MagusDamageAidLogic  getDamageAidLogic(){
		
		if(logic == null){
			logic = new MagusDamageAidLogic();
		}
		return logic;
	}
	
	/*
	 * LOADS THE WEAPONS IN TO START THE SCREEN PROCESS
	 * -> CREATES THE VIEW CONTETN TO COMBOBOX OF cmbBxWeaponTypes
	 * -> FILLS UP THE MAP CONTAINER HERE
	 */
	public static ObservableList<String> getWeaponTypes(){
		
		fillUpWeaponTypesInNeed();
		return getTheMapKeysAsList(weaponTypes);
	}
	
	
	private static void fillUpWeaponTypesInNeed(){
		
		if(weaponTypes == null){
			weaponTypes = MagusDBInterface.getDBConnection()
					.getDoubleColumnContent(queryToGetWeaponTypes, new String[] { starterTableOfDB });
		}
	}
	
	private static ObservableList<String> getTheMapKeysAsList(Map<String,String> listToConvert){
		
		ObservableList<String> resToView = FXCollections.observableArrayList();
		Iterator it = listToConvert.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			if((String)pair.getKey() == null)
				continue;
			resToView.add((String)pair.getKey());
		}
		return resToView;
	}
	
	public static ObservableList<String> getTheAreaCollectionOfChosenWeapon(String chosenWeaponString){
		
		fillUpWeaponTypesInNeed();
		damage_ChosenAreaCollectionTableName = weaponTypes.get(chosenWeaponString);
		chosen_AreaCollectionColumns = MagusDBInterface.getDBConnection()
				.getDoubleColumnContent(queryToGetChosenAreaCollectionColumns, new String[] { damage_ChosenAreaCollectionTableName });
		return getTheMapKeysAsList(chosen_AreaCollectionColumns);
	}
	

	public static ObservableList<String> getTheStrictAreaRowFromChosenAreaGroup(String chosenAreaGroupNameToDamage){

		damage_ChosenAreaGroupName = chosenAreaGroupNameToDamage;
		damage_ChosenAreaGroupTableName = chosen_AreaCollectionColumns.get(chosenAreaGroupNameToDamage);

		chosen_AreasAndDicesGoup = MagusDBInterface.getDBConnection()
				.getDoubleColumnContent(queryToGetAreasGroupAreasAndDices, new String[] { damage_ChosenAreaGroupTableName });
		return getTheMapKeysAsList(chosen_AreasAndDicesGoup);
	}
	
	
	public static String getTheStictCommentOfAreaGroup(){
		
		return MagusDBInterface.getDBConnection()
				.getTheValueOfASpecificCell(queryToGetAreaCollectionRowComment, new String[] { damage_ChosenAreaCollectionTableName, damage_ChosenAreaGroupTableName  });
	}
	
	public static String getTheStirctCommentOfArea(){
		
		return MagusDBInterface.getDBConnection()
				.getTheValueOfASpecificCell(queryTOGetChosenAreaComment, new String[] {damage_ChosenAreaGroupTableName, damage_ChosenArea});
	}
	
	public static ObservableList<String> getTheStrinctAreaGroupHPRow(){
		
		chosen_AreaCollectionHPRow = MagusDBInterface.getDBConnection()
				.getSingleRowContnet_WithColumnTitle(queryToGetAreaCollectionHPRow_longer, new String[] {
						damage_ChosenAreaCollectionTableName, damage_ChosenAreaGroupName  });
		return getTheMapKeysAsList(chosen_AreaCollectionHPRow);
	}
	
	public static String[] getTheCellPariOfStrictAreaAndHP(String chosenArea, String chosenHPValue){

		String chosenHPHeader = "";
		if(chosenArea == null){
			Random rnd = new Random();
			damage_ChosenArea = getBackKeyAreaValueByItsValueDice(rnd.nextInt(10));
		} else {
			damage_ChosenArea = chosenArea;
		}
		chosenHPHeader = chosen_AreaCollectionHPRow.get(chosenHPValue);
		chosenHPHeader = chosenHPHeader.substring(3);
		String chosenAffectHeader = defineTheProperAffectHeader(chosenHPHeader);
		
		//System.out.println(chosenHPHeader + " " + chosenAffectHeader +" " + damage_ChosenAreaGroupTableName + " " + damage_ChosenArea );
		return MagusDBInterface.getDBConnection()
				.getTheValuePairOfSpecificCells(queryToGetChosenAreaCellPair, new String[] {
						chosenHPHeader, chosenAffectHeader, damage_ChosenAreaGroupTableName, damage_ChosenArea });
	}

	public static String[] getTheCellPariOfStrictAreaAndHP(Integer dice, String chosenHPValue){
		
		String chosenHPHeader = "";
		damage_ChosenArea = getBackKeyAreaValueByItsValueDice(dice);

		chosenHPHeader = chosen_AreaCollectionHPRow.get(chosenHPValue);
		chosenHPHeader = chosenHPHeader.substring(3);
		String chosenAffectHeader = defineTheProperAffectHeader(chosenHPHeader);
		
		//System.out.println(chosenHPHeader + " " + chosenAffectHeader +" " + damage_ChosenAreaGroupTableName + " " + damage_ChosenArea );
		return MagusDBInterface.getDBConnection()
				.getTheValuePairOfSpecificCells(queryToGetChosenAreaCellPair, new String[] {
						chosenHPHeader, chosenAffectHeader, damage_ChosenAreaGroupTableName, damage_ChosenArea });
	}
	
	private static String getBackKeyAreaValueByItsValueDice(Integer diceRoll) {

		Iterator it = chosen_AreasAndDicesGoup.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			String diceValueFromList = pair.getValue().toString();
			if (diceValueFromList.contains(diceRoll.toString())) {
				return (String) pair.getKey();
			}
		}
		return null;
	}

	
	private static String defineTheProperAffectHeader(String chosenHPHeader) {

		String dientifPart_AreaGroupHPHeader = chosenHPHeader.split("_")[1];
		StringWriter strw = new StringWriter();
		strw.write(changeKeyWordFromHPHeaderToEffectHeader);
		strw.write("_");
		strw.write(dientifPart_AreaGroupHPHeader);
		strw.flush();
		return strw.toString();
	}
}
