package application;

import static org.junit.Assert.*;

import org.junit.Test;

import javafx.collections.ObservableList;

public class TestMagusDamageAidLogic {

	@Test
	public void test() {
		
		ObservableList<String> log = MagusDamageAidLogic.getDamageAidLogic().getWeaponTypes();
		if(log.size() != 3)
			fail("1-Not enough element: " + log.size());
	}

	@Test()
	public void test2(){
		
	}
	
	
}