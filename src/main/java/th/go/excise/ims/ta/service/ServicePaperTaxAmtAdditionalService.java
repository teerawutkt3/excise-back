package th.go.excise.ims.ta.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import th.co.baiwa.buckwaframework.common.bean.DataTableAjax;
import th.go.excise.ims.common.util.ExcelUtils;
import th.go.excise.ims.ta.vo.CreatePaperFormVo;
import th.go.excise.ims.ta.vo.ServicePaperQtyVo;
import th.go.excise.ims.ta.vo.ServicePaperTaxAmtAdditionalVo;

@Service
public class ServicePaperTaxAmtAdditionalService {
private static final Logger logger = LoggerFactory.getLogger(ServicePaperTaxAmtAdditionalService.class);
	
	public DataTableAjax<ServicePaperTaxAmtAdditionalVo> GetlistServicePaperTaxAmtAdditionalVo(CreatePaperFormVo request) {
		int total = 35;
		DataTableAjax<ServicePaperTaxAmtAdditionalVo> dataTableAjax = new DataTableAjax<ServicePaperTaxAmtAdditionalVo>();
		dataTableAjax.setDraw(request.getDraw() + 1);
		dataTableAjax.setData(listServicePaperTaxAmtAdditionalVo(request.getStart(),request.getLength(),total));
		dataTableAjax.setRecordsTotal(total);
		dataTableAjax.setRecordsFiltered(total);
		return dataTableAjax;
	}

	public List<ServicePaperTaxAmtAdditionalVo> listServicePaperTaxAmtAdditionalVo(int start,int length,int total) {
		String excis = "กรมสรรพสามิตภาคที่";
		List<ServicePaperTaxAmtAdditionalVo> datalist = new ArrayList<ServicePaperTaxAmtAdditionalVo>();
		ServicePaperTaxAmtAdditionalVo data = null;
		for(int i = start; i <(start+length);i++){
			if(i >= total){
				break;
			}
			data = new ServicePaperTaxAmtAdditionalVo();
			data.setGoodsDesc(excis+i);
			data.setTaxQty("100.00");
			data.setInformPrice("10000.00");
			data.setTaxValue("2000.00");
			data.setTaxRateByValue("40000.00");
			data.setTaxRateByQty("6000.00");
			data.setTaxAdditional("50000.00");
			data.setPenaltyAmt("5000.00");
			data.setSurchargeAmt("9000.00");
			data.setMoiTaxAmt("32000.00");
			data.setNetTaxAmt("5000.00");
			datalist.add(data);
			}
		

		return datalist;
	}
	public byte[] exportFinishedGoodsReceive() {

		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("ตารางการคำนวณภาษีที่ต้องชำระเพิ่ม");
		int rowNum = 0;
		int cellNum = 0;
		Row row = sheet.createRow(rowNum);
		Cell cell = row.createCell(cellNum);

		/* call style from utils */
		CellStyle thStyle = ExcelUtils.createThCellStyle(workbook);
		CellStyle cellCenter = ExcelUtils.createCenterCellStyle(workbook);
		CellStyle cellLeft = ExcelUtils.createLeftCellStyle(workbook);
		CellStyle cellRight = ExcelUtils.createRightCellStyle(workbook);
		;

		/* tbTH1 */
		String[] tbTH1 = { "ลำดับ", "รายการ", "ปริมาณ", "ราคาขายปลีก", "มูลค่า","อัตราภาษี","","ภาษีที่ต้องชำระเพิ่มเติม","เบี้ยปรับ","เงินเพิ่ม","ภาษีเพื่อราชการส่วนท้องถิ่น", "รวม" };
		for (int i = 0; i < tbTH1.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(tbTH1[i]);
			cell.setCellStyle(thStyle);
		}

		/* tbTH2 */
		String[] tbTH2 = { "", "", "","", "",  "ตามมูลค่า", "ตามปริมาณ" };
		rowNum++;
		row = sheet.createRow(rowNum);
		for (int i = 0; i < tbTH2.length; i++) {
			if (i > 2) {
				cell = row.createCell(i);
				cell.setCellValue(tbTH2[i]);
				cell.setCellStyle(thStyle);
			}
		}

		/* width */
		int colIndex = 0;
		sheet.setColumnWidth(colIndex++, 10 * 256);
		sheet.setColumnWidth(colIndex++, 38 * 256);
		sheet.setColumnWidth(colIndex++, 25 * 256);
		sheet.setColumnWidth(colIndex++, 25 * 256);
		sheet.setColumnWidth(colIndex++, 23 * 256);
		sheet.setColumnWidth(colIndex++,25 * 256);
		sheet.setColumnWidth(colIndex++, 25 * 256);
		sheet.setColumnWidth(colIndex++, 25 * 256);
		sheet.setColumnWidth(colIndex++, 25 * 256);
		sheet.setColumnWidth(colIndex++,25 * 256);
		sheet.setColumnWidth(colIndex++, 32 * 256);
		sheet.setColumnWidth(colIndex++, 23 * 256);
		/* merge(firstRow, lastRow, firstCol, lastCol) */
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
//
		for (int i = 0; i < 12; i++) {
			if (i != 5 && i != 6) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
				cell = row.createCell(i);
				cell.setCellStyle(thStyle);
			}
		}

		/* set data */
		rowNum = 2;
		cellNum = 0;
		int no = 1;
		List<ServicePaperTaxAmtAdditionalVo> dataList = listServicePaperTaxAmtAdditionalVo(0, 35, 35);
		for (ServicePaperTaxAmtAdditionalVo data : dataList) {
			row = sheet.createRow(rowNum);

			cell = row.createCell(cellNum);
			cell.setCellValue(no);
			cell.setCellStyle(cellCenter);
			cellNum++;

			cell = row.createCell(cellNum);
			cell.setCellValue(data.getGoodsDesc());
			cell.setCellStyle(cellLeft);
			cellNum++;

			cell = row.createCell(cellNum);
			cell.setCellValue(data.getTaxQty());
			cell.setCellStyle(cellCenter);
			cellNum++;

			cell = row.createCell(cellNum);
			cell.setCellValue(data.getInformPrice());
			cell.setCellStyle(cellRight);
			cellNum++;

			cell = row.createCell(cellNum);
			cell.setCellValue(data.getTaxValue());
			cell.setCellStyle(cellRight);
			cellNum++;

			cell = row.createCell(cellNum);
			cell.setCellValue(data.getTaxRateByValue());
			cell.setCellStyle(cellRight);
			cellNum++;

			cell = row.createCell(cellNum);
			cell.setCellValue(data.getTaxRateByQty());
			cell.setCellStyle(cellRight);
			cellNum++;

			cell = row.createCell(cellNum);
			cell.setCellValue(data.getTaxAdditional());
			cell.setCellStyle(cellRight);
			cellNum++;
			
			cell = row.createCell(cellNum);
			cell.setCellValue(data.getPenaltyAmt());
			cell.setCellStyle(cellRight);
			cellNum++;
			
			cell = row.createCell(cellNum);
			cell.setCellValue(data.getSurchargeAmt());
			cell.setCellStyle(cellRight);
			cellNum++;
			
			cell = row.createCell(cellNum);
			cell.setCellValue(data.getMoiTaxAmt());
			cell.setCellStyle(cellRight);
			cellNum++;
			
			cell = row.createCell(cellNum);
			cell.setCellValue(data.getNetTaxAmt());
			cell.setCellStyle(cellRight);
			cellNum++;
			
			no++;
			rowNum++;
			cellNum = 0;
		}

		// set output
		byte[] content = null;
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			workbook.write(outputStream);
			content = outputStream.toByteArray();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return content;
	}
	 public List<ServicePaperTaxAmtAdditionalVo> readFileServicePaperTaxAmtAdditionalVo(ServicePaperTaxAmtAdditionalVo request) {
		  logger.info("readFileQuantityServiceVo");
		  logger.info("fileName "+request.getFile().getOriginalFilename());
		  logger.info("type "+request.getFile().getContentType());
		  List<ServicePaperTaxAmtAdditionalVo> dataList = new ArrayList<>();
		  
		  try(Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(request.getFile().getBytes()));){
				Sheet sheet = workbook.getSheetAt(0);
				
				   for (Row row : sheet) {
					   ServicePaperTaxAmtAdditionalVo pushdata = new ServicePaperTaxAmtAdditionalVo();
					    // Skip on first row
					    if (row.getRowNum() == 0) {
					     continue;
					    } 
					    for (Cell cell : row) {
					     if (cell.getColumnIndex() == 0) {
					      // Column No.
					    	 continue;
					     } else if (cell.getColumnIndex() == 1) {
					    	 pushdata.setGoodsDesc(ExcelUtils.getCellValueAsString(cell));
					     } else if (cell.getColumnIndex()== 2){
					    	 pushdata.setGoodsDesc(ExcelUtils.getCellValueAsString(cell));
					     } else if (cell.getColumnIndex()== 3){
					    	 pushdata.setTaxQty(ExcelUtils.getCellValueAsString(cell));
					     } else if (cell.getColumnIndex() == 4 ){
					    	 pushdata.setInformPrice(ExcelUtils.getCellValueAsString(cell));
					     } else if (cell.getColumnIndex() == 5){
					    	 pushdata.setTaxValue(ExcelUtils.getCellValueAsString(cell));
					     } else if (cell.getColumnIndex() == 6){
					    	 pushdata.setTaxRateByValue(ExcelUtils.getCellValueAsString(cell));
					     }else if (cell.getColumnIndex() == 7){
					    	 pushdata.setTaxRateByQty(ExcelUtils.getCellValueAsString(cell));
					     }else if (cell.getColumnIndex() == 8){
					    	 pushdata.setTaxAdditional(ExcelUtils.getCellValueAsString(cell));
					     }else if (cell.getColumnIndex() == 9){
					    	 pushdata.setPenaltyAmt(ExcelUtils.getCellValueAsString(cell));
					     }else if (cell.getColumnIndex() == 10){
					    	 pushdata.setSurchargeAmt(ExcelUtils.getCellValueAsString(cell));
					     }else if (cell.getColumnIndex() == 11){
					    	 pushdata.setMoiTaxAmt(ExcelUtils.getCellValueAsString(cell));
					     }else if (cell.getColumnIndex() == 12){
					    	 pushdata.setNetTaxAmt(ExcelUtils.getCellValueAsString(cell));
					     }
					     
					    }
						   dataList.add(pushdata);
					   }
			
				 
		  }catch(Exception e){
			  logger.error(e.getMessage(),e);
		  }
		  return dataList;
		 }
}
