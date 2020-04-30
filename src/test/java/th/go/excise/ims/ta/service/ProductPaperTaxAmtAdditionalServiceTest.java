package th.go.excise.ims.ta.service;

import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

public class ProductPaperTaxAmtAdditionalServiceTest {

	private static final String PATH = "/tmp/";
	private static final String TYPE = ".xlsx";

	private static final String PRODUCT_PAPER_TAX_AMT_ADDITIONAL = "product_paper_tax_amt_additional";

	ProductPaperTaxAmtAdditionalService productPaperTaxAmtAdditionalService = new ProductPaperTaxAmtAdditionalService();

	@Test
	public void test_exportProductPaperTaxAmtAdditional() {
		// set output
		try (FileOutputStream Output = new FileOutputStream(PATH + PRODUCT_PAPER_TAX_AMT_ADDITIONAL + TYPE)) {
			byte[] outArray = productPaperTaxAmtAdditionalService.exportProductPaperTaxAmtAdditional();
			Output.write(outArray);
			System.out.println("Creating excel" + "\n" + PRODUCT_PAPER_TAX_AMT_ADDITIONAL + "\n" + "Done" + "\n");
		} catch (IOException e) {
			System.out.println(PRODUCT_PAPER_TAX_AMT_ADDITIONAL + "\n" + "false" + "\n" + e.getMessage() + "\n" + e + "\n");
		}

	}

}
