package application;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MagusDamageAidLogic {

	private static MagusDamageAidLogic logic;
	private static Map<String,String> weaponTypes;	//KEY IS THE PRESENTED VALUE OF DIFFERENT WEEAPONS - FROM COLUMNS
	private static Map<String,String> chosen_AreaCollectionColumns;	//KEY IS THE PRESENTED VALUE OF DIFFERENT AREAS	- FROM COLUMNS
	private static Map<String,String> chosen_AreaCollectionHPRow;	//KEY IS THE PRESENTED VALUE OF HP-CHATEGROIES	- FROM A ROW
	private static Map<String,String> chosen_AreasAndDicesGoup;	//KEY IS THE AREAS OF THAT AREA-GROUP
	
	private static String damage_ChosenAreaCollectionTableName;
	private static String damage_ChosenAreaGroupTableName;
	private static String damage_ChosenHPHeader;
	private static String damage_ChosenStrictArea;
	
	private static String changeKeyWordFromHPHeaderToEffectHeader = "affect";
	
	private static String queryToGetWeaponTypes = 
			"SELECT weapon_type, damages_group FROM ?;";
	
	//AREA COLLECTION TABLE
	private static String queryToGetChosenAreaCollectionColumns = 
			"SELECT area_group, tableName  FROM ?;";
	private static String queryToGetAreaCollectionHPRow = 
			"SELECT sp_damage_mild, sp_damage_serious, sp_damage_dangerous, sp_damage_lethal FROM ? WHERE tableName=?;";
	private static String queryToGetAreaCollectionRowComment = 
			"SELECT commenting FROM ? WHERE tableName=?;";
	
	//AREA GROUP TABLE
	private static String queryToGetAreasGroupAreasAndDices = 
			"SELECT area, dice FROM ?;";
	private static String queryToGetChosenAreaCellPair = 
			"SELECT ?, ? FROM ? WHERE area=?;";
	private static String queryTOGetChosenAreaComment = 
			"SELECT commenting FROM ? WHERE area=?;";
	
	
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
					.getDoubleColumnContent(queryToGetWeaponTypes, null);
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
	
	public static ObservableList<String> getTheAreaGroupsOfChosenWeapon(String chosenWeaponString){
		
		fillUpWeaponTypesInNeed();
		damage_ChosenAreaCollectionTableName = weaponTypes.get(chosenWeaponString);
		chosen_AreaCollectionColumns = MagusDBInterface.getDBConnection()
				.getDoubleColumnContent(queryToGetChosenAreaCollectionColumns, new String[] { damage_ChosenAreaCollectionTableName });
		return getTheMapKeysAsList(chosen_AreaCollectionColumns);
	}
	

	public static ObservableList<String> getTheStrictAreaRowFromChosenAreaGroup(String chosenAreaGroupNameToDamage){
		
		damage_ChosenAreaGroupTableName = chosen_AreaCollectionColumns.get(chosenAreaGroupNameToDamage);
		chosen_AreaCollectionHPRow = MagusDBInterface.getDBConnection()
				.getSingleRowContnet_WithColumnTitle(queryToGetAreaCollectionHPRow, new String[] { damage_ChosenAreaCollectionTableName, chosenAreaGroupNameToDamage  });
		return getTheMapKeysAsList(chosen_AreaCollectionHPRow);
	}
	
	
	public static String getTheStictCommentOfAreasOfChosenAreaGroup(){
		
		return MagusDBInterface.getDBConnection()
				.getTheValueOfASpecificCell(queryToGetAreaCollectionRowComment, new String[] { damage_ChosenAreaCollectionTableName, damage_ChosenAreaGroupTableName  });
	}
	
	public static ObservableList<String> getTheStrictAreaGroupPosibilitesThatUserChosen(String chosenHPValue){
	
		damage_ChosenHPHeader = chosen_AreaCollectionHPRow.get(chosenHPValue);
		chosen_AreasAndDicesGoup = MagusDBInterface.getDBConnection()
				.getDoubleColumnContent(queryToGetAreasGroupAreasAndDices, new String[]{  damage_ChosenAreaGroupTableName });
		return getTheMapKeysAsList(chosen_AreasAndDicesGoup);
	}
	
	public static String getTheStrictAreaByItsDice(Integer diceRoll){
		
		damage_ChosenStrictArea = getBackKeyAreaByItsValueDice(diceRoll);
		return damage_ChosenStrictArea;
	}
	
	private static String getBackKeyAreaByItsValueDice(Integer diceRoll){
		
		Iterator it = chosen_AreasAndDicesGoup.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			String diceValueFromList = pair.getValue().toString();
			if(diceValueFromList.contains("-")){
				Integer diceIntervalDown = Integer.parseInt(diceValueFromList.split("-")[0]);
				Integer diceIntervalUp = Integer.parseInt(diceValueFromList.split("-")[1]);
				if(diceIntervalDown <= diceRoll && diceIntervalUp >= diceRoll )
					return (String)pair.getKey();
			} else {
				Integer diceOnlyValue = Integer.parseInt(diceValueFromList);
				if(diceOnlyValue == diceRoll)
					return (String)pair.getKey();
			}
		}
		return null;
	}
	
	
	public static String[] getTheCellPariOfStrictAreaAndHP(String chosenArea){
		
		if(chosenArea != null){
			damage_ChosenStrictArea = chosenArea;
		}
		String chosenAffectHeader = defineTheProperAffectHeader();
		
		return MagusDBInterface.getDBConnection()
				.getTheValuePairOfSpecificCells(queryToGetChosenAreaCellPair, new String[] {
						damage_ChosenHPHeader, chosenAffectHeader, damage_ChosenStrictArea });
	}
	
	
	private static String defineTheProperAffectHeader(){
		
		String dientifPart_AreaGroupHPHeader = damage_ChosenHPHeader.split("-")[1];
		StringWriter strw = new StringWriter();
		strw.write(changeKeyWordFromHPHeaderToEffectHeader);
		strw.write("_");
		strw.write(dientifPart_AreaGroupHPHeader);
		strw.flush();
		return strw.toString();
	}
}