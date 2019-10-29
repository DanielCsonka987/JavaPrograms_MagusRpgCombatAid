package application;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/*
 * MANAGES THE AID OF DAMAGE STATE-CALCULATION OF MAGUS-RPG
 */
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
	 * STEP 1
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
	
	/*
	 * STEP 2
	 * LOADS THE AREA COLLECTION THAT USER HAS CHOSEN
	 * -> CREATES THE VIEW CONTENT FOR cmbBxAttackAreaGroup
	 * -> FILLS UP THE MAP HERE
	 */
	public static ObservableList<String> getTheAreaCollection_OfChosenWeapon(String chosenWeaponString){
		
		fillUpWeaponTypesInNeed();
		damage_ChosenAreaCollectionTableName = weaponTypes.get(chosenWeaponString);
		chosen_AreaCollectionColumns = MagusDBInterface.getDBConnection()
				.getDoubleColumnContent(queryToGetChosenAreaCollectionColumns, new String[] { damage_ChosenAreaCollectionTableName });
		return getTheMapKeysAsList(chosen_AreaCollectionColumns);
	}
	
	/*
	 * STEP 3
	 * LOADS IN THE ROW OF CHOSEN AREA-GROUP FROM AREA-COLLECTION BY INNER INFO OF STEP 2
	 * -> CREATES THE VIEW CONTETN OF HP-ROW cmbBxAttackStrength
	 * -> FILLS UP THE MAP OF THAT HERE
	 */
	public static ObservableList<String> getTheStrinctAreaGroup_HPRow(){
		
		chosen_AreaCollectionHPRow = MagusDBInterface.getDBConnection()
				.getSingleRowContnet_WithColumnTitle(queryToGetAreaCollectionHPRow_longer, new String[] {
						damage_ChosenAreaCollectionTableName, damage_ChosenAreaGroupName  });
		return getTheMapKeysAsList(chosen_AreaCollectionHPRow);
	}
	
	/*
	 * STEP 4
	 * LOADS IN THE COMMENT OF THE CHOSEN AREA-GROUP BY INNER INFO OF STEP 2
	 * -> CREATES THE VEIW CONTNENT OF lblInfoOfTable
	 */
	public static String getTheStictComment_OfAreaGroup(){
		
		return MagusDBInterface.getDBConnection()
				.getTheValueOfASpecificCell(queryToGetAreaCollectionRowComment, new String[] { damage_ChosenAreaCollectionTableName, damage_ChosenAreaGroupTableName  });
	}
	
	/*
	 * STEP 5
	 * LOADS IN THE AREAS OF THE CHOSEN AREA-GROUP THAT USER CAN (OPTINOAL) CHOOSE
	 * -> CREATES THE VIEW CONTENT OF cmbBxAttackArea
	 * -> FILLS UP THE MAP HERE
	 */
	public static ObservableList<String> getTheStrictAreaRow_OfChosenAreaGroup(String chosenAreaGroupNameToDamage){

		damage_ChosenAreaGroupName = chosenAreaGroupNameToDamage;
		damage_ChosenAreaGroupTableName = chosen_AreaCollectionColumns.get(chosenAreaGroupNameToDamage);

		chosen_AreasAndDicesGoup = MagusDBInterface.getDBConnection()
				.getDoubleColumnContent(queryToGetAreasGroupAreasAndDices, new String[] { damage_ChosenAreaGroupTableName });
		return getTheMapKeysAsList(chosen_AreasAndDicesGoup);
	}
	
	/*
	 * STEP 6
	 * LOADS IN THE CHOSEN CELLS, THAT AREA-GROUP CONTAINS 
	 * AND HAS CHOSEN BY RANDOM GENEAROR OR USER WITH THE ADDITIONAL AREA INFO
	 * -> CRAETES THE VIEW CONTETNT OF txtAreaDescript AND txtAreaEffect 
	 */
	public static String[] getTheCellPari_OfStrictAreaAndHP(String chosenArea, String chosenHPValue){

		String chosenHPHeader = "";
		if(chosenArea.equals("") || chosenArea == null){
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
	
	/*
	 * SPTE 6b - THIS IS FOR TEST PURPOPSES
	 */
	public static String[] getTheCellPari_OfStrictAreaAndHP(Integer dice, String chosenHPValue){
		
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
		//System.out.println("Rolling "+diceRoll);
		Iterator it = chosen_AreasAndDicesGoup.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			String diceValueFromList = pair.getValue().toString();
			//System.out.println("In list " + diceValueFromList);
			if(diceValueFromList.contains("-")){
				String[] temp = diceValueFromList.split("-");
				Integer down = Integer.parseInt(temp[0]);
				Integer up = Integer.parseInt(temp[1].substring(0, 1));
				//System.out.println("Interval: " + down + " " + up);
				if(down <= diceRoll && up >= diceRoll)
					return (String) pair.getKey();
			}
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
	
	/*
	 * STEP 7
	 * LOADS IN THE COMMENT OF THE CHOSEN AREA HAS
	 * -> CREATES THE VIEW CONTENT OF lblInfoOfRow
	 */
	public static String getTheStirctComment_OfArea(){
		
		return MagusDBInterface.getDBConnection()
				.getTheValueOfASpecificCell(queryTOGetChosenAreaComment, new String[] {damage_ChosenAreaGroupTableName, damage_ChosenArea});
	}

	/*
	 * STEP 8
	 * LOADS IN THE AREA INFO, THAT RANDOM OR USE HAS CHOSEN
	 * -> CREATES THE VIEW CONTENT OF lblAreaInfo;
	 */
	public static String getTheChosenArea_OfManagedProcess(){
		
		return damage_ChosenArea;
	}
	

	

}
