package th.go.excise.ims.ta.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import th.co.baiwa.buckwaframework.common.bean.ReportJsonBean;
import th.co.baiwa.buckwaframework.common.bean.ResponseData;
import th.co.baiwa.buckwaframework.common.constant.ProjectConstant;
import th.co.baiwa.buckwaframework.common.constant.ReportConstants.FILE_EXTENSION;
import th.co.baiwa.buckwaframework.common.rest.adapter.BigDecimalTypeAdapter;
import th.co.baiwa.buckwaframework.common.rest.adapter.DateThaiTypeAdapter;
import th.co.baiwa.buckwaframework.support.ApplicationCache;
import th.go.excise.ims.ta.service.AbstractTaFormTSService;
import th.go.excise.ims.ta.service.TaFormTS0101Service;
import th.go.excise.ims.ta.service.TaFormTS0102Service;
import th.go.excise.ims.ta.service.TaFormTS0103Service;
import th.go.excise.ims.ta.service.TaFormTS0104Service;
import th.go.excise.ims.ta.service.TaFormTS0105Service;
import th.go.excise.ims.ta.service.TaFormTS0106Service;
import th.go.excise.ims.ta.service.TaFormTS0107Service;
import th.go.excise.ims.ta.service.TaFormTS0108Service;
import th.go.excise.ims.ta.service.TaFormTS0109Service;
import th.go.excise.ims.ta.service.TaFormTS0110Service;
import th.go.excise.ims.ta.service.TaFormTS0111Service;
import th.go.excise.ims.ta.service.TaFormTS0112Service;
import th.go.excise.ims.ta.service.TaFormTS0113Service;
import th.go.excise.ims.ta.service.TaFormTS01141Service;
import th.go.excise.ims.ta.service.TaFormTS01142Service;
import th.go.excise.ims.ta.service.TaFormTS0114Service;
import th.go.excise.ims.ta.service.TaFormTS0115Service;
import th.go.excise.ims.ta.service.TaFormTS0116Service;
import th.go.excise.ims.ta.service.TaFormTS01171Service;
import th.go.excise.ims.ta.service.TaFormTS0117Service;
import th.go.excise.ims.ta.service.TaFormTS0118Service;
import th.go.excise.ims.ta.service.TaFormTS0119Service;
import th.go.excise.ims.ta.service.TaFormTS0120Service;
import th.go.excise.ims.ta.service.TaFormTS0121Service;
import th.go.excise.ims.ta.service.TaFormTS0302Service;
import th.go.excise.ims.ta.service.TaFormTS0303Service;
import th.go.excise.ims.ta.service.TaFormTS0423Service;
import th.go.excise.ims.ta.service.TaFormTS0424Service;

@Controller
@RequestMapping("/api/ta/report")
public class TaFormTSController {

	private static final Logger logger = LoggerFactory.getLogger(TaFormTSController.class);

	private Gson gson = new GsonBuilder().serializeNulls()
		.registerTypeAdapter(Date.class, DateThaiTypeAdapter.getInstance())
		.registerTypeAdapter(BigDecimal.class, BigDecimalTypeAdapter.getInstance())
		.create();

	private Map<String, AbstractTaFormTSService> taFormTSMap = new HashMap<>();
	
	@Autowired
	public TaFormTSController(
		
		TaFormTS0101Service taFormTS0101Service,
		TaFormTS0102Service taFormTS0102Service,
		TaFormTS0103Service taFormTS0103Service,
		TaFormTS0104Service taFormTS0104Service,
		TaFormTS0105Service taFormTS0105Service,
		TaFormTS0106Service taFormTS0106Service,
		TaFormTS0107Service taFormTS0107Service,
		TaFormTS0108Service taFormTS0108Service,
		TaFormTS0109Service taFormTS0109Service,
		TaFormTS0110Service taFormTS0110Service,
		TaFormTS0111Service taFormTS0111Service,
		TaFormTS0112Service taFormTS0112Service,
		TaFormTS0113Service taFormTS0113Service,
		TaFormTS0114Service taFormTS0114Service,
		TaFormTS01141Service taFormTS01141Service,
		TaFormTS01142Service taFormTS01142Service,
		TaFormTS0115Service taFormTS0115Service,
		TaFormTS0116Service taFormTS0116Service,
		TaFormTS0117Service taFormTS0117Service,
		TaFormTS01171Service taFormTS01171Service,
		TaFormTS0118Service taFormTS0118Service,
		TaFormTS0119Service taFormTS0119Service,
		TaFormTS0120Service taFormTS0120Service,
		TaFormTS0121Service taFormTS0121Service,
		TaFormTS0302Service taFormTS0302Service,
		TaFormTS0303Service taFormTS0303Service,
		TaFormTS0423Service taFormTS0423Service,
		TaFormTS0424Service taFormTS0424Service
		) {
		
		taFormTSMap.put("ta-form-ts0101", taFormTS0101Service);
		taFormTSMap.put("ta-form-ts0102", taFormTS0102Service);
		taFormTSMap.put("ta-form-ts0103", taFormTS0103Service);
		taFormTSMap.put("ta-form-ts0104", taFormTS0104Service);
		taFormTSMap.put("ta-form-ts0105", taFormTS0105Service);
		taFormTSMap.put("ta-form-ts0106", taFormTS0106Service);
		taFormTSMap.put("ta-form-ts0107", taFormTS0107Service);
		taFormTSMap.put("ta-form-ts0108", taFormTS0108Service);
		taFormTSMap.put("ta-form-ts0109", taFormTS0109Service);
		taFormTSMap.put("ta-form-ts0110", taFormTS0110Service);
		taFormTSMap.put("ta-form-ts0111", taFormTS0111Service);
		taFormTSMap.put("ta-form-ts0112", taFormTS0112Service);
		taFormTSMap.put("ta-form-ts0113", taFormTS0113Service);
		taFormTSMap.put("ta-form-ts0114", taFormTS0114Service);
		taFormTSMap.put("ta-form-ts01141", taFormTS01141Service);
		taFormTSMap.put("ta-form-ts01142", taFormTS01142Service);
		taFormTSMap.put("ta-form-ts0115", taFormTS0115Service);
		taFormTSMap.put("ta-form-ts0116", taFormTS0116Service);
		taFormTSMap.put("ta-form-ts0117", taFormTS0117Service);
		taFormTSMap.put("ta-form-ts01171", taFormTS01171Service);
		taFormTSMap.put("ta-form-ts0118", taFormTS0118Service);
		taFormTSMap.put("ta-form-ts0119", taFormTS0119Service);
		taFormTSMap.put("ta-form-ts0120", taFormTS0120Service);
		taFormTSMap.put("ta-form-ts0121", taFormTS0121Service);
		taFormTSMap.put("ta-form-ts0302", taFormTS0302Service);
		taFormTSMap.put("ta-form-ts0303", taFormTS0303Service);
		taFormTSMap.put("ta-form-ts0423", taFormTS0423Service);
		taFormTSMap.put("ta-form-ts0424", taFormTS0424Service);
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/pdf/{tsNumber}")
	public void generatePdfReportFormTS(@PathVariable("tsNumber") String tsNumber, @ModelAttribute ReportJsonBean reportJsonBean, HttpServletResponse response) throws Exception {
		logger.info("generatePdfReportFormTS tsNumber={}", tsNumber);
		
		AbstractTaFormTSService taFormTSService = taFormTSMap.get(tsNumber);
		Object formVo = gson.fromJson(reportJsonBean.getJson(), taFormTSService.getVoClass());
		byte[] reportFile = taFormTSService.generateReport(formVo);

		String filename = String.format(taFormTSService.getReportName() + "_%s." + FILE_EXTENSION.PDF, DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()));
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
		response.setContentType("application/octet-stream");

		FileCopyUtils.copy(reportFile, response.getOutputStream());
	}
	
	@GetMapping("/form-ts-number/{tsNumber}")
	@ResponseBody
	public ResponseData<List<String>> getFormTSNumber(@PathVariable("tsNumber") String tsNumber) {
		logger.info("getFormTSNumber tsNumber={}", tsNumber);
		
		ResponseData<List<String>> response = new ResponseData<>();
		try {
			AbstractTaFormTSService taFormTSService = taFormTSMap.get(tsNumber);
			response.setData(taFormTSService.getFormTsNumberList());
			response.setStatus(ProjectConstant.RESPONSE_STATUS.SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.setMessage(ApplicationCache.getMessage(ProjectConstant.RESPONSE_MESSAGE.ERROR500_CODE).getMessageTh());
			response.setStatus(ProjectConstant.RESPONSE_STATUS.FAILED);
		}
		
		return response;
	}

	@PostMapping("/get-from-ts/{tsNumber}/{formTsNumber}")
	@ResponseBody
	public ResponseData<String> getFromTs(@PathVariable("tsNumber") String tsNumber, @PathVariable("formTsNumber") String formTsNumber){
		logger.info("getFormTSNumber tsNumber={}", tsNumber);
		ResponseData<String> response = new ResponseData<>();
		try {
			AbstractTaFormTSService taFormTSService = taFormTSMap.get(tsNumber);
			response.setData(gson.toJson(taFormTSService.getFormTS(formTsNumber), taFormTSService.getVoClass()));
			response.setStatus(ProjectConstant.RESPONSE_STATUS.SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.setMessage(ApplicationCache.getMessage(ProjectConstant.RESPONSE_MESSAGE.ERROR500_CODE).getMessageTh());
			response.setStatus(ProjectConstant.RESPONSE_STATUS.FAILED);
		}

		return response;
	}
	@PostMapping("/save-from-ts/{tsNumber}")
	@ResponseBody
	public ResponseData<?> saveFromTs(@PathVariable("tsNumber") String tsNumber, @RequestBody ReportJsonBean reportJsonBean){
		logger.info("getFormTSNumber tsNumber={}", tsNumber);
		ResponseData<String> response = new ResponseData<>();
		try {
			AbstractTaFormTSService taFormTSService = taFormTSMap.get(tsNumber);
			Object formVo = gson.fromJson(reportJsonBean.getJson(), taFormTSService.getVoClass());
			taFormTSService.saveFormTS(formVo);
			response.setMessage(ApplicationCache.getMessage(ProjectConstant.RESPONSE_MESSAGE.SAVE.SUCCESS_CODE).getMessageTh());
			response.setStatus(ProjectConstant.RESPONSE_STATUS.SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.setMessage(e.getMessage());
			response.setStatus(ProjectConstant.RESPONSE_STATUS.FAILED);
		}

		return response;
	}

}
