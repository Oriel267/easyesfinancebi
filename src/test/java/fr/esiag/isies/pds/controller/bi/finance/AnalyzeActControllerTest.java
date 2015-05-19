package fr.esiag.isies.pds.controller.bi.finance;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import fr.esiag.isies.pds.dao.bi.finance.FactBilingActDao;
import fr.esiag.isies.pds.model.bi.finance.factBilingAct;

public class AnalyzeActControllerTest {
	
	@Mocked
	FactBilingActDao myFicitiveFactBilingActDao;
	
	public AnalyzeActController analyzeActController;
	public Model model;
	
	
	
		@Before
		public void init() {
			analyzeActController = new AnalyzeActController();
			model = new ExtendedModelMap();
		}
		
		@Test
		public void testViewFictiveDatamart(){
			new NonStrictExpectations() {{
				//mock response of DAO 
				myFicitiveFactBilingActDao.getAll(); result = new ArrayList<factBilingAct>();
			}};
			String url = analyzeActController.viewFictiveDatamart(model);
			assertEquals(url,"/financebi/ShowGenerateDatas");
		}
		
		@Test
		public void testAddDayOnDate() throws ParseException{
			Date currentDate = analyzeActController.stringToDate("2015-01-01");
			Date resultDate = analyzeActController.stringToDate("2015-01-05");
			Date testDate = analyzeActController.addDayOnDate(currentDate, 4);
			assertEquals(testDate,resultDate);
		}
		
		/*@Test
		public void testStringToDate() throws ParseException{
			
			Date testDate = analyzeActController.stringToDate("2015-06-15");
			assertEquals(currentDate , testDate);
		}*/
		@Test
		public void testRandomValue(){
			int[]arrayValues =  {20,30,40,60};
			int indice = 2;
			int a = analyzeActController.randomValue(arrayValues, 2);
			assertEquals(a,30);
		}
		
		

}
