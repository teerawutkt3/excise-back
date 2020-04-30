package th.go.excise.ims.preferences.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import th.co.baiwa.buckwaframework.common.bean.ResponseData;
import th.co.baiwa.buckwaframework.common.constant.DocumentConstants.MODULE_NAME;
import th.co.baiwa.buckwaframework.common.constant.ProjectConstant.RESPONSE_STATUS;
import th.co.baiwa.buckwaframework.support.ApplicationCache;
import th.go.excise.ims.common.constant.ProjectConstants.EXCISE_OFFICE_CODE;
import th.go.excise.ims.preferences.vo.ExciseDepartment;
import th.go.excise.ims.preferences.vo.ExciseSubdept;

@RestController
@RequestMapping("/api/preferences/department")
public class ExciseDepartmentController {

	private static final Logger logger = LoggerFactory.getLogger(ExciseDepartmentController.class);

	@PostMapping("/sector-list")
	@ApiOperation(
		tags = MODULE_NAME.PREFERENCES,
		value = "Get Excise Sector List"
	)
	public ResponseData<List<ExciseDepartment>> getAllSectorList() {
		logger.info("getAllSectorList");
		
		ResponseData<List<ExciseDepartment>> response = new ResponseData<>();
		List<ExciseDepartment> exciseSectorList = ApplicationCache.getExciseSectorList();
		if (!CollectionUtils.isEmpty(exciseSectorList)) {
			response.setData(exciseSectorList);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
		} else {
			response.setMessage("Excise Sector List Not Found");
			response.setStatus(RESPONSE_STATUS.FAILED);
		}
		
		return response;
	}

	@PostMapping("/area-list/{officeCode}")
	@ApiOperation(
		tags = MODULE_NAME.PREFERENCES,
		value = "Get Excise Area List by Sector Code"
	)
	public ResponseData<List<ExciseDepartment>> getAreaListBySectorCode(@PathVariable("officeCode") String officeCode) {
		logger.info("getAreaListBySectorCode officeCode={}", officeCode);
		
		ResponseData<List<ExciseDepartment>> response = new ResponseData<>();
		List<ExciseDepartment> exciseAreaList = ApplicationCache.getExciseAreaList(officeCode);
		if (!CollectionUtils.isEmpty(exciseAreaList)) {
			response.setData(exciseAreaList);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
		} else {
			response.setMessage("Excise Area List Not Found");
			response.setStatus(RESPONSE_STATUS.FAILED);
		}
		
		return response;
	}

	@PostMapping("/branch-list/{officeCode}")
	@ApiOperation(
		tags = MODULE_NAME.PREFERENCES,
		value = "Get Excise Branch List by Area Code"
	)
	public ResponseData<List<ExciseDepartment>> getBranchListByAreaCode(@PathVariable("officeCode") String officeCode) {
		logger.info("getBranchListByAreaCode officeCode={}", officeCode);
		
		ResponseData<List<ExciseDepartment>> response = new ResponseData<>();
		List<ExciseDepartment> exciseBranchList = ApplicationCache.getExciseBranchList(officeCode);
		if (!CollectionUtils.isEmpty(exciseBranchList)) {
			response.setData(exciseBranchList);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
		} else {
			response.setMessage("Excise Area List Not Found");
			response.setStatus(RESPONSE_STATUS.FAILED);
		}
		
		return response;
	}
	
	@PostMapping("/dept-dtl/{officeCode}")
	@ApiOperation(
		tags = MODULE_NAME.PREFERENCES,
		value = "Get Excise Department Details"
	)
	public ResponseData<ExciseDepartment> getExciseDept(@PathVariable("officeCode") String officeCode) {
		logger.info("getBranchListByAreaCode officeCode={}", officeCode);
		
		ResponseData<ExciseDepartment> response = new ResponseData<>();
		ExciseDepartment exciseDept = ApplicationCache.getExciseDepartment(officeCode);
		if (exciseDept != null ) {
			response.setData(exciseDept);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
		} else {
			response.setMessage("Office Code Not Found");
			response.setStatus(RESPONSE_STATUS.FAILED);
		}
		
		return response;
	}
	
	@PostMapping("/dept/central-ta")
	@ApiOperation(
		tags = MODULE_NAME.PREFERENCES,
		value = "Get Department of Tax Audit"
	)
	public ResponseData<List<ExciseDepartment>> getDeptTaxAudit() {
		logger.info("getDeptTaxAudit");
		
		List<ExciseDepartment> deptList = new ArrayList<>();
		deptList.add(ApplicationCache.getExciseDepartment(EXCISE_OFFICE_CODE.TA_CENTRAL_SELECTOR));
		deptList.add(ApplicationCache.getExciseDepartment(EXCISE_OFFICE_CODE.TA_CENTRAL_OPERATOR1));
		deptList.add(ApplicationCache.getExciseDepartment(EXCISE_OFFICE_CODE.TA_CENTRAL_OPERATOR2));
		
		ResponseData<List<ExciseDepartment>> response = new ResponseData<>();
		response.setData(deptList);
		response.setStatus(RESPONSE_STATUS.SUCCESS);
		
		return response;
	}
	
	@GetMapping("/subdept/{officeCode}")
	@ApiOperation(
			tags = MODULE_NAME.PREFERENCES,
			value = "Get Subdept by OfficeCode"
			)
	public ResponseData<List<ExciseSubdept>> getSubdeptListByOfficeCode(@PathVariable("officeCode") String officeCode) {
		logger.info("getBranchListByAreaCode officeCode={}", officeCode);
		
		List<ExciseSubdept> subdeptList = ApplicationCache.getExciseSubdeptList(officeCode);
		
		ResponseData<List<ExciseSubdept>> response = new ResponseData<>();
		if (!CollectionUtils.isEmpty(subdeptList)) {
			response.setData(subdeptList);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
		} else {
			response.setMessage("Excise Sub-Department Not Found");
			response.setStatus(RESPONSE_STATUS.FAILED);
		}
		
		return response;
	}

}
