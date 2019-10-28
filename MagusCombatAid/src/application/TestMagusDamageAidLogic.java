package application;

import static org.junit.Assert.*;

import org.junit.Test;

import javafx.collections.ObservableList;

public class TestMagusDamageAidLogic {

	@Test
	public void test1_LoadInWeaponTypes() {
		
		ObservableList<String> log = MagusDamageAidLogic.getDamageAidLogic().getWeaponTypes();
		if(log.size() != 3)
			fail("1-Not enough element: " + log.size());
		if(!log.get(0).equals("Szúrófegyverek" ))
			fail("2-Not the needed element here " + log.get(0));
		if(!log.get(1).equals("Zúzófegyverek" ))
			fail("3-Not the needed element here " + log.get(1));
		if(!log.get(2).equals("Vágófegyverek" ))
			fail("5-Not the needed element here " + log.get(2));
	}

	@Test()
	public void test2_FirstSelection(){
		ObservableList<String> logWeapons = MagusDamageAidLogic.getDamageAidLogic().getWeaponTypes();
		ObservableList<String> logAreaColl = MagusDamageAidLogic.getDamageAidLogic()
		.getTheAreaCollectionOfChosenWeapon(logWeapons.get(1));
		
		if(logAreaColl.size() != 5)
			fail("1-Not enough element: " + logAreaColl.size());
		if(!logAreaColl.get(0).equals("Fej" ))
			fail("2-Not the needed element here " + logAreaColl.get(0));
		if(!logAreaColl.get(1).equals("Törzs előlről" ))
			fail("3-Not the needed element here " + logAreaColl.get(1));
		if(!logAreaColl.get(2).equals("Törzs hátulról" ))
			fail("4-Not the needed element here " + logAreaColl.get(2));
		if(!logAreaColl.get(3).equals("Kar" ))
			fail("5-Not the needed element here " + logAreaColl.get(3));
		if(!logAreaColl.get(4).equals("Láb" ))
			fail("6-Not the needed element here " + logAreaColl.get(4));
	}
	
	@Test()
	public void test3_SecondSelection(){
		ObservableList<String> logWeapons = MagusDamageAidLogic.getDamageAidLogic().getWeaponTypes();
		ObservableList<String> logAreaColl = MagusDamageAidLogic.getDamageAidLogic()
				.getTheAreaCollectionOfChosenWeapon(logWeapons.get(1));
		
		ObservableList<String> logAreaChosen_AreaList =
				MagusDamageAidLogic.getTheStrictAreaRowFromChosenAreaGroup(logAreaColl.get(0));
		
		if(logAreaChosen_AreaList.size() != 5)
			fail("1-Not enough values");
		
		if(!logAreaChosen_AreaList.get(0).equals("Agykoponya" ))
			fail("2-Not the needed element here " + logAreaColl.get(0));
		if(!logAreaChosen_AreaList.get(1).equals("Homlok" ))
			fail("3-Not the needed element here " + logAreaColl.get(1));
		if(!logAreaChosen_AreaList.get(2).equals("Halánték" ))
			fail("4-Not the needed element here " + logAreaColl.get(2));
		if(!logAreaChosen_AreaList.get(3).equals("Arckoponya" ))
			fail("5-Not the needed element here " + logAreaColl.get(3));
		if(!logAreaChosen_AreaList.get(4).equals("Nyak/Tarkó" ))
			fail("5-Not the needed element here " + logAreaColl.get(4));

	}
	
	@Test()
	public void test4_ThirdSelection_OnlySelection(){
		ObservableList<String> logWeapons = MagusDamageAidLogic.getDamageAidLogic().getWeaponTypes();
		ObservableList<String> logAreaColl = MagusDamageAidLogic.getDamageAidLogic()
				.getTheAreaCollectionOfChosenWeapon(logWeapons.get(1));
		ObservableList<String> logAreaChosen_AreaList =
				MagusDamageAidLogic.getTheStrictAreaRowFromChosenAreaGroup(logAreaColl.get(0));
		
		ObservableList<String> logAreaChosen_HPList = MagusDamageAidLogic
				.getDamageAidLogic().getTheStrinctAreaGroupHPRow();
		
		if(logAreaChosen_HPList.size() != 4)
			fail("1-Not eonough values");
		if(!logAreaChosen_HPList.get(0).equals("1-2 Ép" ))
			fail("2-Not the needed element here " + logAreaChosen_HPList.get(0));
		if(!logAreaChosen_HPList.get(1).equals("3-4 Ép" ))
			fail("3-Not the needed element here " + logAreaChosen_HPList.get(1));
		if(!logAreaChosen_HPList.get(2).equals("5-6 Ép" ))
			fail("4-Not the needed element here " + logAreaChosen_HPList.get(2));
		if(!logAreaChosen_HPList.get(3).equals("7+ Ép" ))
			fail("5-Not the needed element here " + logAreaChosen_HPList.get(3));
	}
	
	
	@Test()
	public void test5_ThirdSelectionAndFinish_OnlySelection(){
		ObservableList<String> logWeapons = MagusDamageAidLogic.getDamageAidLogic().getWeaponTypes();
		ObservableList<String> logAreaColl = MagusDamageAidLogic.getDamageAidLogic()
				.getTheAreaCollectionOfChosenWeapon(logWeapons.get(1));			//ZÚZÓFEGYVER
		ObservableList<String> logAreaChosen_AreaList =
				MagusDamageAidLogic.getTheStrictAreaRowFromChosenAreaGroup(logAreaColl.get(0));	//FEJ AREA_GROUP-> AREAS
		ObservableList<String> logAreaChosen_HPList = MagusDamageAidLogic
				.getDamageAidLogic().getTheStrinctAreaGroupHPRow();		//HP LIST BY AREA_GROUP
		
		String[] result = MagusDamageAidLogic.getDamageAidLogic()		//HOMOK + 3-4 Ép
				.getTheCellPariOfStrictAreaAndHP(logAreaChosen_AreaList.get(1), logAreaChosen_HPList.get(1));
		
		if(!result[0].equals("A csapás zúzott sebet ejt a homlokon, de nem szakítja át.") 
				|| !result[1].equals("Szédülés. Gyenge fájdalom."))
			fail("1-Not arrived the wanted result");
		
	}
	
	
	@Test()
	public void test6_ThirdSelectionAndFinish_DirectRNDInjection(){
		ObservableList<String> logWeapons = MagusDamageAidLogic.getDamageAidLogic().getWeaponTypes();
		ObservableList<String> logAreaColl = MagusDamageAidLogic.getDamageAidLogic()
				.getTheAreaCollectionOfChosenWeapon(logWeapons.get(1));		//ZÚZÓFEGYVER
		ObservableList<String> logAreaChosen_AreaList =
				MagusDamageAidLogic.getTheStrictAreaRowFromChosenAreaGroup(logAreaColl.get(0));	//FEJ AREA_GROUP-> AREAS
		ObservableList<String> logAreaChosen_HPList = MagusDamageAidLogic
				.getDamageAidLogic().getTheStrinctAreaGroupHPRow();	//HP LIST BY AREA_GROUP
		
		String[] result = MagusDamageAidLogic.getDamageAidLogic()		//HOMOK RND LIKE (3) + 3-4 Ép
				.getTheCellPariOfStrictAreaAndHP(3, logAreaChosen_HPList.get(1));
		
		if(!result[0].equals("A csapás zúzott sebet ejt a homlokon, de nem szakítja át.") 
				|| !result[1].equals("Szédülés. Gyenge fájdalom."))
			fail("1-Not arrived the wanted result");
		
	}
	
	
	@Test()
	public void test7_ThirdSelectionAndFinish_OnlySelection(){
		ObservableList<String> logWeapons = MagusDamageAidLogic.getDamageAidLogic().getWeaponTypes();
		ObservableList<String> logAreaColl = MagusDamageAidLogic.getDamageAidLogic()
				.getTheAreaCollectionOfChosenWeapon(logWeapons.get(0));			//SZÚRÓFEGYVER
		if(!logWeapons.get(0).equals("Szúrófegyverek"))
			fail("1-Not good order!" + logWeapons.get(0));
		ObservableList<String> logAreaChosen_AreaList =
				MagusDamageAidLogic.getTheStrictAreaRowFromChosenAreaGroup(logAreaColl.get(4));	//LÁB AREA GROUP
		if(!logAreaColl.get(4).equals("Láb"))
			fail("2-Not good order!" + logAreaColl.get(4));
		ObservableList<String> logAreaChosen_HPList = MagusDamageAidLogic
				.getDamageAidLogic().getTheStrinctAreaGroupHPRow();	
		
		String[] result = MagusDamageAidLogic.getDamageAidLogic()		//LÁBFEJ + 5-6 Ép
				.getTheCellPariOfStrictAreaAndHP(logAreaChosen_AreaList.get(4), logAreaChosen_HPList.get(2));
		
		if(!result[0].equals("A fegyver átdöfi a lábfejet, a földhöz szegezi. Csontok törnek, szalagok szakadnak.") 
				|| !result[1].equals("Mérsékelt fájdalom. Jelentős hátrány. Gyenge vérzés. Lábfej Részleges bénulása. Haladási sebesség: 50%"))
			fail("6-Not arrived the wanted result");
		
	}
	
	@Test()
	public void test8_ThirdSelectionAndFinish_CommentsOgAreaGroup(){
		
		ObservableList<String> logWeapons = MagusDamageAidLogic.getDamageAidLogic().getWeaponTypes();
		ObservableList<String> logAreaColl = MagusDamageAidLogic.getDamageAidLogic()
				.getTheAreaCollectionOfChosenWeapon(logWeapons.get(0));			//SZÚRÓFEGYVER
		ObservableList<String> logAreaChosen_AreaList =
				MagusDamageAidLogic.getTheStrictAreaRowFromChosenAreaGroup(logAreaColl.get(2));	//TÖRZS ELÖL AREA GROUP
		
		String commentPierce = MagusDamageAidLogic.getDamageAidLogic()
				.getTheStictCommentOfAreaGroup();
		
		if(!commentPierce.equals("lásd még beslőségek sérülésekor Szívbénulás, Tüdősorvadás, Bélsorvadás BM varázslatok"))
			fail("1-Not the correct Atea_Gorup comment " + commentPierce);

		
	}
	
	@Test()
	public void test9_ThirdSelectionAndFinish_CommentsOgArea(){
		
		ObservableList<String> logWeapons = MagusDamageAidLogic.getDamageAidLogic().getWeaponTypes();
		ObservableList<String> logAreaColl = MagusDamageAidLogic.getDamageAidLogic()
				.getTheAreaCollectionOfChosenWeapon(logWeapons.get(2));			//VÁGÓFEGYVER
		ObservableList<String> logAreaChosen_AreaList =
				MagusDamageAidLogic.getTheStrictAreaRowFromChosenAreaGroup(logAreaColl.get(3));	//KÉZ AREA GROUP
		ObservableList<String> logAreaChosen_HPList = MagusDamageAidLogic
				.getDamageAidLogic().getTheStrinctAreaGroupHPRow();
		
		String[] result = MagusDamageAidLogic.getDamageAidLogic()		//KÖNYÖK + 5-6 Ép
				.getTheCellPariOfStrictAreaAndHP(logAreaChosen_AreaList.get(2), logAreaChosen_HPList.get(2));
		
		if(!result[0].equals("A penge mélyen belevág a könyökízületbe, használhatatlanná téve azt.") 
				|| !result[1].equals("Mérsékelt vérzés. Mérsékelt fájdalom. Könyöktől lefelé a kar Teljes bénulása."))
			fail("1-Not good cell pair errived");
		
		String commentShear	= MagusDamageAidLogic.getDamageAidLogic().getTheStirctCommentOfArea();
		if(!commentShear.equals("Max 5 Ép"))
			fail("2-Not good comment arrived " + commentShear);
		
	}
	
}
