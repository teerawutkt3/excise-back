package th.go.excise.ims.ta.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import org.junit.Test;

public class ServicePaperPricePerUnitServiceTest {
	 private static final String PATH = "/tmp/";
	 private static final String TYPE = ".xlsx";
	 private static final String MATERIAL_RECEIVE = "service";
	 @Test
	 public void test_GetPriceServiceVo() throws IOException {

	  String fileName = URLEncoder.encode(MATERIAL_RECEIVE, "UTF-8");

	  ServicePaperPricePerUnitService servicePaperPricePerUnit = new ServicePaperPricePerUnitService();

	  try {
	   byte[] outArray = servicePaperPricePerUnit.exportFilePriceServiceVo();
	   FileOutputStream Output = new FileOutputStream(PATH + fileName + TYPE);
	   Output.write(outArray);
	   Output.close();
	  } catch (FileNotFoundException e) {
	   e.printStackTrace();
	  } catch (IOException e) {
	   e.printStackTrace();
	  }

	  System.out.println("Done");

	 }
}
