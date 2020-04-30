package th.go.excise.ims.ta.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import th.co.baiwa.buckwaframework.common.constant.CommonConstants.PROFILE;
import th.co.baiwa.buckwaframework.common.constant.ReportConstants.FILE_EXTENSION;
import th.co.baiwa.buckwaframework.common.constant.ReportConstants.PATH;
import th.co.baiwa.buckwaframework.common.constant.ReportConstants.REPORT_NAME;
import th.go.excise.ims.Application;
import th.go.excise.ims.ta.vo.TaFormTS0112Vo;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//@WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
//@ActiveProfiles(value = PROFILE.UNITTEST)
public class TaFormTS0112ServiceTest {

	private static final String REPORT_FILE = PATH.TEST_PATH + "%s" + "." + FILE_EXTENSION.PDF;

	@Autowired
	private TaFormTS0112Service taFormTS0112Service;

	@Test
	public void test_generateReport() throws Exception {
		TaFormTS0112Service taFormTS0112Service = new TaFormTS0112Service();

		TaFormTS0112Vo formTS0112Vo = new TaFormTS0112Vo();
		formTS0112Vo.setFormTsNumber("000000-2562-000001");
		formTS0112Vo.setDocPlace("กรมสรรพสามิต");
		formTS0112Vo.setDocDate(new Date());
		formTS0112Vo.setHeadOfficerFullName("ธีรวุฒิ กุลฤทธิชัย");
		formTS0112Vo.setHeadOfficerPosition("เจ้าหน้าที่ตรวจสอบภาษี");
		formTS0112Vo.setHeadOfficerOfficeName("สังกัด");
		formTS0112Vo.setOfficerFullName1("สมพงษ์ คงมี");
		formTS0112Vo.setOfficerPosition1("ตรวจ");
		formTS0112Vo.setOfficerFullName2("จรัญ จำรูญ");
		formTS0112Vo.setOfficerPosition2("ตรวจ");
		formTS0112Vo.setOfficerFullName3("");
		formTS0112Vo.setOfficerPosition3("");
		formTS0112Vo.setOfficerFullName4("");
		formTS0112Vo.setOfficerPosition4("");
		formTS0112Vo.setOfficerFullName5("");
		formTS0112Vo.setOfficerPosition5("");
		formTS0112Vo.setFactoryName("บริษัท เชลล์แห่งประเทศไทย จำกัด ");
		formTS0112Vo.setNewRegId("01005150424621001");
		formTS0112Vo.setFacAddrNo("789");
		formTS0112Vo.setFacSoiName("");
		formTS0112Vo.setFacThnName("เพลินจิต");
		formTS0112Vo.setFacTambolName("คลองเตย");
		formTS0112Vo.setFacAmphurName("คลองเตย");
		formTS0112Vo.setFacProvinceName("กรุงเทพมหานคร");
		formTS0112Vo.setFacZipCode("10110");
		formTS0112Vo.setOwnerFullName1("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setOwnerPosition("1");
		formTS0112Vo.setOwnerOther("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setLawGroup("1");
		formTS0112Vo.setSeizeDesc("รายละเอียด");
		formTS0112Vo.setContactDesc("รายละเอียด");
		formTS0112Vo.setOwnerFullName2("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setOwnerPosition2("เจ้าหน้าที่ตรวจสอบภาษี");
		formTS0112Vo.setOwnerOther2("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setSignAuthFullName("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setSignInspectorFullName("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setSignWitnessFullName1("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setSignWitnessFullName2("ผู้ดูแลระบบ000000 นามสกุล");

		byte[] reportFile = taFormTS0112Service.generateReport(formTS0112Vo);
		IOUtils.write(reportFile, new FileOutputStream(new File(String.format(REPORT_FILE, REPORT_NAME.TA_FORM_TS01_12))));
	}
	
	@Test
	public void test_generateReport_Blank() throws Exception {
		TaFormTS0112Service taFormTS0112Service = new TaFormTS0112Service();
		
		TaFormTS0112Vo formTS0112Vo = new TaFormTS0112Vo();
		
		byte[] reportFile = taFormTS0112Service.generateReport(formTS0112Vo);
		IOUtils.write(reportFile, new FileOutputStream(new File(String.format(REPORT_FILE, REPORT_NAME.TA_FORM_TS01_12 + "_blank"))));
	}
	
//	@Test
	public void test_saveFormTS() throws Exception {
		TaFormTS0112Vo formTS0112Vo = new TaFormTS0112Vo();
		formTS0112Vo.setFormTsNumber("");
		formTS0112Vo.setDocPlace("กรมสรรพสามิต");
		formTS0112Vo.setDocDate(new Date());
		formTS0112Vo.setHeadOfficerFullName("ธีรวุฒิ กุลฤทธิชัย");
		formTS0112Vo.setHeadOfficerPosition("เจ้าหน้าที่ตรวจสอบภาษี");
		formTS0112Vo.setHeadOfficerOfficeName("สังกัด");
		formTS0112Vo.setOfficerFullName1("สมพงษ์ คงมี");
		formTS0112Vo.setOfficerPosition1("ตรวจ");
		formTS0112Vo.setOfficerFullName2("จรัญ จำรูญ");
		formTS0112Vo.setOfficerPosition2("ตรวจ");
		formTS0112Vo.setOfficerFullName3("");
		formTS0112Vo.setOfficerPosition3("");
		formTS0112Vo.setOfficerFullName4("");
		formTS0112Vo.setOfficerPosition4("");
		formTS0112Vo.setOfficerFullName5("");
		formTS0112Vo.setOfficerPosition5("");
		formTS0112Vo.setFactoryName("บริษัท เชลล์แห่งประเทศไทย จำกัด ");
		formTS0112Vo.setNewRegId("01005150424621001");
		formTS0112Vo.setFacAddrNo("789");
		formTS0112Vo.setFacSoiName("");
		formTS0112Vo.setFacThnName("เพลินจิต");
		formTS0112Vo.setFacTambolName("คลองเตย");
		formTS0112Vo.setFacAmphurName("คลองเตย");
		formTS0112Vo.setFacProvinceName("กรุงเทพมหานคร");
		formTS0112Vo.setFacZipCode("10110");
		formTS0112Vo.setOwnerFullName1("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setOwnerPosition("1");
		formTS0112Vo.setOwnerOther("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setLawGroup("1");
		formTS0112Vo.setSeizeDesc("รายละเอียด");
		formTS0112Vo.setContactDesc("รายละเอียด");
		formTS0112Vo.setOwnerFullName2("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setOwnerPosition2("เจ้าหน้าที่ตรวจสอบภาษี");
		formTS0112Vo.setOwnerOther2("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setSignAuthFullName("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setSignInspectorFullName("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setSignWitnessFullName1("ผู้ดูแลระบบ000000 นามสกุล");
		formTS0112Vo.setSignWitnessFullName2("ผู้ดูแลระบบ000000 นามสกุล");
		
		taFormTS0112Service.saveFormTS(formTS0112Vo);
	}
	
//	@Test
	public void test_getFormTS() {
		TaFormTS0112Vo formTs0112Vo = taFormTS0112Service.getFormTS("000000-2562-000247");
		System.out.println(ToStringBuilder.reflectionToString(formTs0112Vo, ToStringStyle.JSON_STYLE));
	}
	
//	@Test
	public void test_getFormTsNumberList() {
		taFormTS0112Service.getFormTsNumberList().forEach(e -> System.out.println(e));
	}
	
}
