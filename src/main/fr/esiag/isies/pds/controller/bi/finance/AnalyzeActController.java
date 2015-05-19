package fr.esiag.isies.pds.controller.bi.finance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.esiag.isies.pds.dao.bi.finance.FactBilingActDao;
import fr.esiag.isies.pds.dao.bi.finance.staging.StagingHealthPathDao;
import fr.esiag.isies.pds.dao.bi.finance.staging.StagingHealthPathStepDao;
import fr.esiag.isies.pds.dao.bi.finance.staging.StagingMedicalActDao;
import fr.esiag.isies.pds.dao.bi.finance.staging.StagingMedicalRecordDao;
import fr.esiag.isies.pds.dao.bi.finance.staging.StagingOrganizationDao;
import fr.esiag.isies.pds.dao.bi.finance.staging.StagingServiceTypeDao;
import fr.esiag.isies.pds.dao.hospital.medical.MedicalActDao;
import fr.esiag.isies.pds.dao.referential.organization.OrganizationDao;
import fr.esiag.isies.pds.model.bi.finance.DimDate;
import fr.esiag.isies.pds.model.bi.finance.DimMedicalAct;
import fr.esiag.isies.pds.model.bi.finance.DimOrganization;
import fr.esiag.isies.pds.model.bi.finance.DimServiceType;
import fr.esiag.isies.pds.model.bi.finance.factBilingAct;
import fr.esiag.isies.pds.model.bi.finance.staging.StagingAssociationActSpéService;
import fr.esiag.isies.pds.model.bi.finance.staging.StagingHealthPath;
import fr.esiag.isies.pds.model.bi.finance.staging.StagingMedicalAct;
import fr.esiag.isies.pds.model.bi.finance.staging.StagingMedicalStep;
import fr.esiag.isies.pds.model.bi.finance.staging.StagingOrganization;
import fr.esiag.isies.pds.model.hospital.medical.TypeAct;
import fr.esiag.isies.pds.model.referential.organization.OrgaType;


/**
 * Controller of AnalyzeAct
 * @author OSA
 *
 */
@Controller
@RequestMapping("/financebi")

public class AnalyzeActController {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AnalyzeActController.class);
	private FactBilingActDao  		 myFicitiveFactBilingActDao;
	// List of object to generate datas for mock of fictiv datamart
	List<DimDate>  		 		    allFictivDate;
	List<DimMedicalAct>   			allFictivMedicalAct;
	List<DimOrganization> 			allFictivOrga;
	List<OrgaType> 		  			allOrgaType;
	List<DimServiceType>            allFictivServicetype;
	List <TypeAct> 		            allFictivTypeAct;
	// DAO of acces object from PaaS
	private MedicalActDao 		    myProdMedicalActDao;
	private OrganizationDao 	    myProdOrgaDao;
	private List<factBilingAct>     myListOfFactBilingDao;
	//Staging DAOs
	private StagingOrganizationDao   mrdao;
	private StagingMedicalActDao     mySMA;
	private StagingHealthPathStepDao shpsdao;
	private StagingHealthPathDao     shsd;
	private StagingMedicalRecordDao  smr;
	private StagingServiceTypeDao    sst;

	// List wich init by IDs which we can make a medical act
	private ArrayList <StagingAssociationActSpéService> ListSpéService = new ArrayList<StagingAssociationActSpéService>();
	/**
	 * Contructor of Analyze Act Controller
	 */
	public AnalyzeActController(){
		//init Daos 
		myFicitiveFactBilingActDao 	  = new FactBilingActDao();
		
		myProdMedicalActDao 		  = new MedicalActDao();
		myProdOrgaDao 				  = new OrganizationDao();
		
		mrdao 						  = new StagingOrganizationDao();
		mySMA						  = new StagingMedicalActDao();
		shpsdao 					  = new StagingHealthPathStepDao();
		shsd 						  = new StagingHealthPathDao();
		smr 						  = new StagingMedicalRecordDao();
		sst							  = new StagingServiceTypeDao();
		
	}
	/**
	 * Injector on fictive datamart
	 * @param model
	 * @return display with Content of Datamart
	 */
	@RequestMapping("generatebidata")
	public String generateData(Model model) {
			// Truncate all informations on fact table
			myFicitiveFactBilingActDao.truncateTable();
			//Init random refunds
			int[] myRefundsArray = {20,30,40,60};
			//init random Service Type
			int[] myServiceTypes = {1,2,3,4,5,6,7,8};
			//init incremential values
			int date 			 = 0;
			int nbOrga 			 = 0;
			int nbAct 			 = 0;
			while (date<allFictivDate.size()){
				while (nbOrga<50){
					while(nbAct<55){
					factBilingAct actualFactBilingAct = new factBilingAct();
					// set actual date
					actualFactBilingAct.setDate(allFictivDate.get(date));
					//get a random indice of act
					int indiceAct = (int)(Math.random()*(1557-1))+1;
					//Set it
					actualFactBilingAct.setCcam(allFictivMedicalAct.get(indiceAct));
					//Set an Orga
					actualFactBilingAct.setOrga(allFictivOrga.get(nbOrga));
					//Set Orga type
					actualFactBilingAct.setOrgatype(allOrgaType.get(myProdOrgaDao.getById(nbOrga+1).getOrgaType().getId()-1));
					//Set ServiceType
					actualFactBilingAct.setServiceType(allFictivServicetype.get(randomValue(myServiceTypes, 7)));
					String labelCodeStagingMedicalAct = allFictivMedicalAct.get(indiceAct).getCodeAct();
					//Set Type Act
					actualFactBilingAct.setTypeAct(allFictivTypeAct.get(myProdMedicalActDao.getByCode(labelCodeStagingMedicalAct).get(0).getIdtypeAct().getId()-1));
					//init random refund which in in array "myrefundsArray
					actualFactBilingAct.setFactActRefund(randomValue(myRefundsArray, 4));
					// set Random id Act
					int randomCaAct=(int)(Math.random()*(30000-250))+250;
					//Set Random Act
					actualFactBilingAct.setCaAct(randomCaAct);
					//Calculate Nb Act
					actualFactBilingAct.setNbAct((int) Math.round(randomCaAct/allFictivMedicalAct.get(date).getActPrice()));
					//Add to datamart ficive BDD
					myFicitiveFactBilingActDao.create(actualFactBilingAct);
					//increment Act
					nbAct++;
					}
					//Increment Nb Orga
				 nbOrga++;
				 //Set Nb Act at 0
				 nbAct=0;
				}
				//Set nbOrga at 0
				nbOrga = 0;
				//increment date
				date++;
			}
			//recovery all entries in fact table
			myListOfFactBilingDao = myFicitiveFactBilingActDao.getAll();
			// Add List To model
			model.addAttribute("listfact",myListOfFactBilingDao);
		return "/financebi/ShowGenerateDatas";
	}
	/**
	 * Init A lot of Health Path 
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("recoveryprodbdd")
	public String recoveryProdBdd(Model model) throws ParseException {
		initSpéService();
		//Set Start Date of health path
		Date injectorincrementDate = stringToDate("2014-01-01");
		//Set End Date of health path
		Date injectorEndDate 	   = stringToDate("2015-04-01");
		//init increment
		int healthPathPerDate = 1;
		while(injectorincrementDate.before(injectorEndDate)){
				while (healthPathPerDate<558){
				//Set random int of indice orga
				int indiceOrga = (int)(Math.random()*(557-1))+1;
				StagingOrganization currentOrga = mrdao.getById(indiceOrga);
				if (currentOrga.getOrgaType().getId() == 1 || currentOrga.getOrgaType().getId() == 2 || currentOrga.getOrgaType().getId() == 3){
				StagingHealthPath myHealthPath = new StagingHealthPath();
				myHealthPath.setCreateDate(injectorincrementDate);
				myHealthPath.setStartDate(injectorincrementDate);
				myHealthPath.setMyOrganization(currentOrga);
				myHealthPath.setCurrentState("Completed");
				myHealthPath.setEndDate(addDayOnDate(injectorincrementDate, 4));
				int indiceMedicalRecord = (int)(Math.random()*(1000-1))+1;
				myHealthPath.setMedicalRecord(smr.getById(indiceMedicalRecord));
				StagingMedicalStep sms = new StagingMedicalStep();
				Date injectorIncrementDateofHealthPath = injectorincrementDate;
				Date endDateofHealthPath = addDayOnDate(injectorincrementDate, 4);
				shsd.create(myHealthPath);
					while(injectorIncrementDateofHealthPath.before(endDateofHealthPath)){
						sms.setCreateDate(injectorincrementDate);
						sms.setStartDate(injectorIncrementDateofHealthPath);
						sms.setEndDate(injectorIncrementDateofHealthPath);
						int indiceAct = (int)(Math.random()*(1557-1))+1;
						StagingMedicalAct currentMedicalAct = mySMA.getById(indiceAct);
						sms.setMedicalAct(currentMedicalAct);
						sms.setStatus("Completed");
						int NuméroAct = currentMedicalAct.getIdtypeAct().getId();
						System.out.println(NuméroAct);
						StagingAssociationActSpéService cli = ListSpéService.get(NuméroAct-1);
						int size = cli.getListServiceFor().size();
						int random = (int) (Math.random()*(size-1)+1);
						sms.setServiceType(sst.getById(random));
						myHealthPath.getSteps().add(sms);
						sms.setHealthPath(myHealthPath);
						shpsdao.create(sms);
						injectorIncrementDateofHealthPath = addDayOnDate(injectorIncrementDateofHealthPath, 1);
					}
					injectorIncrementDateofHealthPath = injectorincrementDate;
				  }
				if (currentOrga.getOrgaType().getId() == 4){
					StagingHealthPath myHealthPath = new StagingHealthPath();
					myHealthPath.setCreateDate(injectorincrementDate);
					myHealthPath.setStartDate(injectorincrementDate);
					myHealthPath.setMyOrganization(currentOrga);
					myHealthPath.setCurrentState("Completed");
					myHealthPath.setEndDate(addDayOnDate(injectorincrementDate, 4));
					int indiceMedicalRecord = (int)(Math.random()*(1000-1))+1;
					myHealthPath.setMedicalRecord(smr.getById(indiceMedicalRecord));
					StagingMedicalStep sms = new StagingMedicalStep();
					Date injectorIncrementDateofHealthPath = injectorincrementDate;
					Date endDateofHealthPath = addDayOnDate(injectorincrementDate, 1);
					shsd.create(myHealthPath);
						while(injectorIncrementDateofHealthPath.before(endDateofHealthPath)){
							sms.setCreateDate(injectorincrementDate);
							sms.setStartDate(injectorIncrementDateofHealthPath);
							sms.setEndDate(injectorIncrementDateofHealthPath);
							List<StagingMedicalAct> currentMedicalAct = mySMA.getByTypeAct(13);
							int size = currentMedicalAct.size();
							int random = (int) (Math.random()*(size-1)+1);
							sms.setMedicalAct(currentMedicalAct.get(random));
							sms.setStatus("Completed");
							myHealthPath.getSteps().add(sms);
							sms.setHealthPath(myHealthPath);
							sms.setServiceType(sst.getById(8));
							shpsdao.create(sms);
							injectorIncrementDateofHealthPath = addDayOnDate(injectorIncrementDateofHealthPath, 1);
						}
						injectorIncrementDateofHealthPath = injectorincrementDate;
					  }
				healthPathPerDate++;
				}
				healthPathPerDate = 1;
			injectorincrementDate = addDayOnDate(injectorincrementDate, 4);		
			}
		
		
		return null;
	}
	
	@RequestMapping("viewFictiveDatamart")
	public String viewFictiveDatamart(Model model) {
		myListOfFactBilingDao = myFicitiveFactBilingActDao.getAll();
		model.addAttribute("listfact",myListOfFactBilingDao);
		return "/financebi/ShowGenerateDatas";
		
	}
	
	@RequestMapping("recoverytopass")
	public String recoverytopass(Model model) {
		
		return null;
		
	}
	public static int randomValue(int[]arrayValues , int indices) {
		int myRandom=new Random().nextInt(indices);
		return arrayValues[myRandom];	
	}
	
	public static Date addDayOnDate(Date date, int nbjours) {
		Calendar c = Calendar.getInstance();
		c.setTime(date); 
		c.add(Calendar.DATE, nbjours); 
		return c.getTime();
		}
	
	public static Date stringToDate(String stringDate) throws ParseException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = simpleDateFormat.parse(stringDate);
		return date;
	}
	
	public void initSpéService(){
		
		StagingAssociationActSpéService anestRea = new StagingAssociationActSpéService();
		ArrayList<Integer> anestReaService = new ArrayList<Integer>();
		anestReaService.add(1);
		anestReaService.add(2);
		anestReaService.add(3);
		anestReaService.add(4);
		anestReaService.add(5);
		anestReaService.add(6);
		anestRea.setListServiceFor(anestReaService);
		ListSpéService.add(anestRea);
		
		StagingAssociationActSpéService endicro = new StagingAssociationActSpéService();
		ArrayList<Integer> endicroService = new ArrayList<Integer>();
		endicroService.add(2);
		endicro.setListServiceFor(endicroService);
		ListSpéService.add(endicro);
		
		StagingAssociationActSpéService pneumo = new StagingAssociationActSpéService();
		ArrayList<Integer> pneumoService = new ArrayList<Integer>();
		pneumoService.add(2);
		pneumoService.add(3);
		pneumo.setListServiceFor(pneumoService);
		ListSpéService.add(pneumo);
		
		StagingAssociationActSpéService ophtalmo = new StagingAssociationActSpéService();
		ArrayList<Integer> ophtalmoService = new ArrayList<Integer>();
		ophtalmoService.add(2);
		ophtalmoService.add(3);
		ophtalmo.setListServiceFor(ophtalmoService);
		ListSpéService.add(ophtalmo);
		
		StagingAssociationActSpéService chirurRecon = new StagingAssociationActSpéService();
		ArrayList<Integer> chirurReconService = new ArrayList<Integer>();
		chirurReconService.add(1);
		chirurReconService.add(5);
		chirurReconService.add(6);
		chirurRecon.setListServiceFor(chirurReconService);
		ListSpéService.add(chirurRecon);
		
		
		StagingAssociationActSpéService medGen = new StagingAssociationActSpéService();
		ArrayList<Integer> MedGenService = new ArrayList<Integer>();
		MedGenService.add(2);
		medGen.setListServiceFor(MedGenService);
		ListSpéService.add(medGen);
		
		StagingAssociationActSpéService psy = new StagingAssociationActSpéService();
		ArrayList<Integer> psyService = new ArrayList<Integer>();
		psyService.add(1);
		psyService.add(2);
		psyService.add(3);
		psy.setListServiceFor(psyService);
		ListSpéService.add(psy);
		
		StagingAssociationActSpéService medInt = new StagingAssociationActSpéService();
		ArrayList<Integer> medIntService = new ArrayList<Integer>();
		medIntService.add(1);
		medInt.setListServiceFor(medIntService);
		ListSpéService.add(medInt);
		
		StagingAssociationActSpéService chirDiges = new StagingAssociationActSpéService();
		ArrayList<Integer> chirDigesService = new ArrayList<Integer>();
		chirDigesService.add(1);
		chirDigesService.add(2);
		chirDigesService.add(5);
		chirDigesService.add(6);
		chirDiges.setListServiceFor(chirDigesService);
		ListSpéService.add(chirDiges);
		
		StagingAssociationActSpéService ginecMed = new StagingAssociationActSpéService();
		ArrayList<Integer> ginecMedService = new ArrayList<Integer>();
		ginecMedService.add(1);
		ginecMedService.add(2);
		ginecMed.setListServiceFor(ginecMedService);
		ListSpéService.add(ginecMed);
		
		StagingAssociationActSpéService ginecObs = new StagingAssociationActSpéService();
		ArrayList<Integer> ginecObsService = new ArrayList<Integer>();
		ginecObsService.add(1);
		ginecObsService.add(2);
		ginecObs.setListServiceFor(ginecObsService);
		ListSpéService.add(ginecObs);
		
		StagingAssociationActSpéService ChirGen = new StagingAssociationActSpéService();
		ArrayList<Integer> ChirGenService = new ArrayList<Integer>();
		ChirGenService.add(1);
		ChirGenService.add(3);
		ChirGenService.add(4);
		ChirGenService.add(5);
		ChirGenService.add(6);
		ChirGen.setListServiceFor(ChirGenService);
		ListSpéService.add(ChirGen);
		
		StagingAssociationActSpéService Bio = new StagingAssociationActSpéService();
		ArrayList<Integer> BioService = new ArrayList<Integer>();
		BioService.add(1);
		Bio.setListServiceFor(BioService);
		ListSpéService.add(Bio);
		
		StagingAssociationActSpéService gastro = new StagingAssociationActSpéService();
		ArrayList<Integer> gastroService = new ArrayList<Integer>();
		gastroService.add(7);
		gastro.setListServiceFor(gastroService);
		ListSpéService.add(gastro);
	}
}
