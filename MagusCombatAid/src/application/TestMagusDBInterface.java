package application;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class TestMagusDBInterface {

	@Test
	public void test1() {
		Map<String, String> res = MagusDBInterface.getDBConnection()
			.getDoubleColumnContent("SELECT weapon_type, damages_group FROM @;"
					, new String[] { "type_damages" });
		if(res.size() != 3)
			fail("1-Not proper values arrived!");
	}

	@Test
	public void test2() {
		Map<String, String> res = MagusDBInterface.getDBConnection()
			.getSingleRowContnet_WithColumnTitle("SELECT sp_damage_mild, sp_damage_serious, sp_damage_dangerous"
					+ " FROM @ WHERE tableName='@';"
					, new String[] { "pierce_damages_group", "pierce_arm" });
		if(res.size() != 3)
			fail("1-Not proper values arrived!");
	}
	
	@Test
	public void test3() {
		String res = MagusDBInterface.getDBConnection()
			.getTheValueOfASpecificCell("SELECT commenting FROM @ WHERE tableName='@';"
					, new String[] { "bruise_damages_group", "bruise_front" });
		if(!res.equals("* lásd még ET Betegségek leírás; tüdősérülés az Állóképességet felezi"))
			fail("1-Not proper values arrived!");
	}
	
	
	@Test
	public void test4() {
		String[] res = MagusDBInterface.getDBConnection()
			.getTheValuePairOfSpecificCells("SELECT @, @ FROM @ WHERE area='@';;"
					, new String[] { "damage_mild", "affect_mild", "shear_front" , "Jobb kulcscsont"  });
		if(res.length != 2 || res[0] == null || res[1] == null )
			fail("1-Not proper value amounts arrived!");
		if(!res[0].equals("A vágá a jobb kulcscsont felett az izmokba hatol.") 
				|| !res[1].equals("+k6 Fp."))
			fail("1-Not proper value amounts arrived!");
	}
}
