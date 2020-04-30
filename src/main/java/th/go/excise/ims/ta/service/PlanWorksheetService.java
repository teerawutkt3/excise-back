package th.go.excise.ims.ta.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import th.co.baiwa.buckwaframework.common.bean.DataTableAjax;
import th.co.baiwa.buckwaframework.common.constant.CommonConstants.FLAG;
import th.co.baiwa.buckwaframework.preferences.constant.ParameterConstants.PARAM_GROUP;
import th.co.baiwa.buckwaframework.security.domain.UserBean;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;
import th.co.baiwa.buckwaframework.support.domain.ParamInfo;
import th.go.excise.ims.common.constant.ProjectConstants;
import th.go.excise.ims.common.constant.ProjectConstants.TA_AUDIT_STATUS;
import th.go.excise.ims.common.constant.ProjectConstants.TA_WORKSHEET_STATUS;
import th.go.excise.ims.common.util.ExciseUtils;
import th.go.excise.ims.preferences.persistence.entity.ExcisePerson;
import th.go.excise.ims.preferences.persistence.entity.ExciseSubdept;
import th.go.excise.ims.preferences.persistence.repository.ExciseSubdeptRepository;
import th.go.excise.ims.preferences.vo.ExciseDepartment;
import th.go.excise.ims.ta.persistence.entity.TaPlanWorksheetDtl;
import th.go.excise.ims.ta.persistence.entity.TaPlanWorksheetHdr;
import th.go.excise.ims.ta.persistence.entity.TaPlanWorksheetSelect;
import th.go.excise.ims.ta.persistence.entity.TaPlanWorksheetSend;
import th.go.excise.ims.ta.persistence.entity.TaWorksheetHdr;
import th.go.excise.ims.ta.persistence.entity.TaWsReg4000;
import th.go.excise.ims.ta.persistence.repository.TaPlanWorksheetDtlRepository;
import th.go.excise.ims.ta.persistence.repository.TaPlanWorksheetHdrRepository;
import th.go.excise.ims.ta.persistence.repository.TaPlanWorksheetSelectRepository;
import th.go.excise.ims.ta.persistence.repository.TaPlanWorksheetSendRepository;
import th.go.excise.ims.ta.persistence.repository.TaWorksheetDtlRepository;
import th.go.excise.ims.ta.persistence.repository.TaWorksheetHdrRepository;
import th.go.excise.ims.ta.persistence.repository.TaWsReg4000Repository;
import th.go.excise.ims.ta.vo.PersonAssignForm;
import th.go.excise.ims.ta.vo.PlanWorksheetAssignVo;
import th.go.excise.ims.ta.vo.PlanWorksheetDatatableVo;
import th.go.excise.ims.ta.vo.PlanWorksheetDtlCusVo;
import th.go.excise.ims.ta.vo.PlanWorksheetSendTableVo;
import th.go.excise.ims.ta.vo.PlanWorksheetStatus;
import th.go.excise.ims.ta.vo.PlanWorksheetVo;
import th.go.excise.ims.ta.vo.TaxDraftVo;

@Service
public class PlanWorksheetService {

	private static final Logger logger = LoggerFactory.getLogger(PlanWorksheetService.class);

	@Autowired
	private WorksheetSequenceService worksheetSequenceService;

	@Autowired
	private TaWorksheetHdrRepository taWorksheetHdrRepository;
	@Autowired
	private TaWorksheetDtlRepository taWorksheetDtlRepository;

	@Autowired
	private TaPlanWorksheetHdrRepository taPlanWorksheetHdrRepository;
	@Autowired
	private TaPlanWorksheetDtlRepository taPlanWorksheetDtlRepository;

	@Autowired
	private TaPlanWorksheetSendRepository taPlanWorksheetSendRepository;
	@Autowired
	private TaPlanWorksheetSelectRepository taPlanWorksheetSelectRepository;

	@Autowired
	private TaWsReg4000Repository reg4000Repository;

	@Autowired
	private ExciseSubdeptRepository exciseSubdeptRepository;

	public TaPlanWorksheetHdr getPlanWorksheetHdr(PlanWorksheetVo formVo) {
		logger.info("getPlanWorksheetHdr budgetYear={}", formVo.getBudgetYear());
		return taPlanWorksheetHdrRepository.findByBudgetYear(formVo.getBudgetYear());
	}

	@Transactional(rollbackFor = { Exception.class })
	public void savePlanWorksheetHdr(PlanWorksheetVo formVo) {
		String officeCode = UserLoginUtils.getCurrentUserBean().getOfficeCode();
		String planNumber = worksheetSequenceService.getPlanNumber(officeCode, formVo.getBudgetYear());
		logger.info("savePlanWorksheetHdr officeCode={}, budgetYear={}, analysisNumber={}, planNumber={}", officeCode,
				formVo.getBudgetYear(), formVo.getAnalysisNumber(), planNumber);

		// ==> Update WorksheetHdr
		TaWorksheetHdr worksheetHdr = taWorksheetHdrRepository.findByAnalysisNumber(formVo.getAnalysisNumber());
		worksheetHdr.setWorksheetStatus(TA_WORKSHEET_STATUS.SELECTION);
		taWorksheetHdrRepository.save(worksheetHdr);

		// ==> Save PlanWorksheetHdr
		TaPlanWorksheetHdr planHdr = new TaPlanWorksheetHdr();
		planHdr.setAnalysisNumber(formVo.getAnalysisNumber());
		planHdr.setBudgetYear(formVo.getBudgetYear());
		planHdr.setPlanNumber(planNumber);
		planHdr.setSendAllFlag(formVo.getSendAllFlag());
		taPlanWorksheetHdrRepository.save(planHdr);

		// Add more logic for support Send All and Send Hierarchy
		List<TaPlanWorksheetSend> planSendList = new ArrayList<>();
		TaPlanWorksheetSend planSend = null;
		if (FLAG.Y_FLAG.equals(formVo.getSendAllFlag())) {
			assignCentralOfficeCode(planSendList, formVo.getBudgetYear(), planNumber);

			List<ExciseDepartment> sectorList = ApplicationCache.getExciseSectorList();
			List<ExciseDepartment> areaList = null;
			// Sector
			for (ExciseDepartment sector : sectorList) {
				if (!ExciseUtils.isCentral(sector.getOfficeCode())) {
					planSend = new TaPlanWorksheetSend();
					planSend.setBudgetYear(formVo.getBudgetYear());
					planSend.setPlanNumber(planNumber);
					planSend.setOfficeCode(sector.getOfficeCode());
					planSend.setSendDate(LocalDate.now());
					planSendList.add(planSend);
					// Area
					areaList = ApplicationCache.getExciseAreaList(sector.getOfficeCode());
					for (ExciseDepartment area : areaList) {
						planSend = new TaPlanWorksheetSend();
						planSend.setBudgetYear(formVo.getBudgetYear());
						planSend.setPlanNumber(planNumber);
						planSend.setOfficeCode(area.getOfficeCode());
						planSend.setSendDate(LocalDate.now());
						planSendList.add(planSend);
					}
				}
			}
		} else {
			assignCentralOfficeCode(planSendList, formVo.getBudgetYear(), planNumber);
		}
		taPlanWorksheetSendRepository.saveAll(planSendList);

		// Initial Data for PlanWorksheetSelect
		List<TaxDraftVo> taxDraftVoList = taWorksheetDtlRepository.findByAnalysisNumber(formVo.getAnalysisNumber());
		taPlanWorksheetSelectRepository.batchInsert(formVo.getBudgetYear(), taxDraftVoList);
	}

	private void assignCentralOfficeCode(List<TaPlanWorksheetSend> planSendList, String budgetYear, String planNumber) {
//		List<String> centralOfficeCodeList = new ArrayList<>(
//				Arrays.asList(EXCISE_OFFICE_CODE.TA_CENTRAL_OPERATOR1, EXCISE_OFFICE_CODE.TA_CENTRAL_OPERATOR2));
		List<ExciseSubdept> deptSelect = exciseSubdeptRepository.findByAuditSelectFlag();

		TaPlanWorksheetSend planSend = null;
		for (ExciseSubdept centralOfficeCode : deptSelect) {
			planSend = new TaPlanWorksheetSend();
			planSend.setBudgetYear(budgetYear);
			planSend.setPlanNumber(planNumber);
			planSend.setOfficeCode(centralOfficeCode.getOffCode());
			planSend.setSendDate(LocalDate.now());
			planSendList.add(planSend);
		}
	}

	@Transactional
	public void savePlanWorksheetDtl(PlanWorksheetVo formVo) {
		UserBean userBean = UserLoginUtils.getCurrentUserBean();
		String officeCode = userBean.getOfficeCode();
		String budgetYear = formVo.getBudgetYear();
		logger.info("savePlanWorkSheetDtl officeCode={}, budgetYear={}, planNumber={}, analysisNumber={}, newRegIds={}",
				officeCode, budgetYear, formVo.getPlanNumber(), formVo.getAnalysisNumber(),
				org.springframework.util.StringUtils.collectionToCommaDelimitedString(formVo.getIds()));

		List<String> planNewRegIdList = taPlanWorksheetDtlRepository.findNewRegIdByOfficeCodeAndPlanNumber(officeCode,
				formVo.getPlanNumber());
		List<String> planNewRegIdFlagNList = taPlanWorksheetDtlRepository
				.findNewRegIdByOfficeCodeAndPlanNumberAndIsDeletedFlagN(officeCode, formVo.getPlanNumber());
		List<String> selectNewRegIdList = formVo.getIds();

		// Keep New and Delete newRegId list
		TaPlanWorksheetDtl planDtl = null;
		List<String> insertNewRegIdList = new ArrayList<>();
		List<String> updateNewRegIdList = new ArrayList<>();

		// Loop selectNewRegIdList for Insert NewRegId
		for (String selNewRegId : selectNewRegIdList) {
			if (!planNewRegIdList.contains(selNewRegId)) {
				insertNewRegIdList.add(selNewRegId);
			}
		}

		// Loop planWorksheetDtlNewRegIdList for Update NewRegId
		for (String planRewRegId : planNewRegIdList) {
			if (!selectNewRegIdList.contains(planRewRegId)) {
				updateNewRegIdList.add(planRewRegId);
			}
		}

		// Insert newRegId to planWorksheetDtl
		if (CollectionUtils.isNotEmpty(insertNewRegIdList)) {
			for (String newRegId : insertNewRegIdList) {
				planDtl = new TaPlanWorksheetDtl();
				planDtl.setPlanNumber(formVo.getPlanNumber());
				planDtl.setAnalysisNumber(formVo.getAnalysisNumber());
				planDtl.setOfficeCode(officeCode);
				planDtl.setNewRegId(newRegId);
				planDtl.setAuditStatus(TA_AUDIT_STATUS.CODE_0100);
				planDtl.setAuSubdeptCode(userBean.getSubdeptCode());
				taPlanWorksheetDtlRepository.save(planDtl);

				updateFlagWorksheetSelect(budgetYear, newRegId, officeCode, FLAG.Y_FLAG, LocalDate.now());
			}
		}

		// Update newRegId from planWorksheetDtl
		String isDeletedPlanDtl = null;
		String selFlag = null;
		LocalDate selDate = null;
		if (CollectionUtils.isNotEmpty(updateNewRegIdList)) {
			for (String newRegId : updateNewRegIdList) {
				if (selectNewRegIdList.contains(newRegId)) {
					isDeletedPlanDtl = FLAG.N_FLAG;
					selFlag = FLAG.Y_FLAG;
					selDate = LocalDate.now();

				} else {
					isDeletedPlanDtl = FLAG.Y_FLAG;
					selFlag = null;
					selDate = null;
				}
				taPlanWorksheetDtlRepository.updateIsDeletedByPlanNumberAndNewRegId(isDeletedPlanDtl,
						formVo.getPlanNumber(), newRegId);
				updateFlagWorksheetSelect(budgetYear, newRegId, officeCode, selFlag, selDate);
			}
		} else {
			for (String selecNewRegId : selectNewRegIdList) {
				if (!planNewRegIdFlagNList.contains(selecNewRegId)) {
					isDeletedPlanDtl = FLAG.N_FLAG;
					selFlag = FLAG.Y_FLAG;
					selDate = LocalDate.now();
					taPlanWorksheetDtlRepository.updateIsDeletedByPlanNumberAndNewRegId(isDeletedPlanDtl,
							formVo.getPlanNumber(), selecNewRegId);
					updateFlagWorksheetSelect(budgetYear, selecNewRegId, officeCode, selFlag, selDate);
				}
			}
		}
	}

	// TODO updateFlagWorksheetSelect 
	private void updateFlagWorksheetSelect(String budgetYear, String newRegId, String officeCode, String selFlag,
			LocalDate selDate) {
		logger.info("updateFlagWorksheetSelect budgetYear={}, newRegId={}, officeCode={}, selFlag={}, selDate={}",
				budgetYear, newRegId, officeCode, selFlag, selDate);
		 List<TaPlanWorksheetSelect> planWorksheetSelList = taPlanWorksheetSelectRepository.findByBudgetYearAndNewRegId(budgetYear,
				newRegId);
		 for (TaPlanWorksheetSelect planWorksheetSel : planWorksheetSelList) {
			 if (ExciseUtils.isCentral(officeCode)) {
					planWorksheetSel.setCentralSelFlag(selFlag);
					planWorksheetSel.setCentralSelDate(selDate);
					planWorksheetSel.setCentralSelOfficeCode(officeCode);
				} else if (ExciseUtils.isSector(officeCode)) {
					planWorksheetSel.setSectorSelFlag(selFlag);
					planWorksheetSel.setSectorSelDate(selDate);
					planWorksheetSel.setSectorSelOfficeCode(officeCode);
				} else if (ExciseUtils.isArea(officeCode)) {
					planWorksheetSel.setAreaSelFlag(selFlag);
					planWorksheetSel.setAreaSelDate(selDate);
					planWorksheetSel.setAreaSelOfficeCode(officeCode);
				}
			 taPlanWorksheetSelectRepository.save(planWorksheetSel);
		}
	}

	public List<TaPlanWorksheetDtl> findPlanWorksheetDtl(PlanWorksheetVo formVo) {
		String officeCode = UserLoginUtils.getCurrentUserBean().getOfficeCode();
		logger.info("findPlanWorkSheetDtl officeCode={}, planNumber={}", officeCode, formVo.getPlanNumber());

		List<TaPlanWorksheetDtl> list = null;
		if (FLAG.N_FLAG.equals(formVo.getSendAllFlag())) {
			if (ExciseUtils.isCentral(officeCode)) {
				
				list = taPlanWorksheetDtlRepository.findByOfficeCodeAndPlanNumberForCentral(formVo.getPlanNumber(), officeCode);
			}else {
				
				list = taPlanWorksheetDtlRepository.findByOfficeCodeAndPlanNumber(officeCode, formVo.getPlanNumber());
			}
		} else {
			list = taPlanWorksheetDtlRepository.findByPlanNumber(formVo.getPlanNumber());
		}
		return list;
	}

	public DataTableAjax<PlanWorksheetDatatableVo> planDtlDatatable(PlanWorksheetVo formVo) {
		logger.info("planDtlDatatable budgetYear={}, officeCOde={}", formVo.getBudgetYear(), formVo.getOfficeCode());

		DataTableAjax<PlanWorksheetDatatableVo> dataTableAjax = new DataTableAjax<>();
		dataTableAjax.setData(taPlanWorksheetDtlRepository.findByCriteria(formVo));
		dataTableAjax.setDraw(formVo.getDraw() + 1);
		int count = taPlanWorksheetDtlRepository.countByCriteria(formVo).intValue();
		dataTableAjax.setRecordsFiltered(count);
		dataTableAjax.setRecordsTotal(count);

		return dataTableAjax;
	}

	public List<PlanWorksheetDatatableVo> planDtlDatatableAll(PlanWorksheetVo formVo) {
		formVo.setOfficeCode(UserLoginUtils.getCurrentUserBean().getOfficeCode());

		return taPlanWorksheetDtlRepository.findAllByCriteria(formVo);
	}

	public Boolean checkSubmitDatePlanWorksheetSend(PlanWorksheetVo formVo) {
		TaPlanWorksheetSend planSend = taPlanWorksheetSendRepository.findByPlanNumberAndOfficeCodeAndSubmitDateIsNull(
				formVo.getPlanNumber(), UserLoginUtils.getCurrentUserBean().getOfficeCode());
		if (planSend != null) {
			return false;
		}
		return true;
	}

	@Transactional
	public void deletePlanWorksheetDlt(String id) {
		logger.info("deletePlanWorksheetDlt by ID : {}", id);
		String budgetYear = ExciseUtils.getCurrentBudgetYear();
		TaPlanWorksheetHdr planHdr = taPlanWorksheetHdrRepository.findByBudgetYear(budgetYear);
		if (planHdr != null) {
			taPlanWorksheetDtlRepository.deleteByPlanNumberAndNewRegId(planHdr.getPlanNumber(), id);
		}
	}

	@Transactional
	public void deletePlanWorksheetAssingDlt(String id, String officeCode) {
		logger.info("deletePlanWorksheetDlt by ID : {}", id);
		String budgetYear = ExciseUtils.getCurrentBudgetYear();
		TaPlanWorksheetHdr planHdr = taPlanWorksheetHdrRepository.findByBudgetYear(budgetYear);

//		Optional<TaPlanWorksheetDtl> planDtl = taPlanWorksheetDtlRepository.findById(Long.parseLong(id));
		if (planHdr != null) {
			taPlanWorksheetDtlRepository.deleteByPlanNumberAndNewRegId(planHdr.getPlanNumber(), id);

			TaPlanWorksheetSend planSendStampDate = taPlanWorksheetSendRepository
					.findByPlanNumberAndOfficeCode(planHdr.getPlanNumber(), officeCode);

			List<String> PlanDtlList = taPlanWorksheetDtlRepository
					.findNewRegIdByOfficeCodeAndPlanNumberAndIsDeletedFlagN(officeCode,planHdr.getPlanNumber());
			if (planSendStampDate != null) {
//					logger.info(" find plan exist size is {} ",Integer.toString(PlanDtlList.size()));
				planSendStampDate.setFacInNum(PlanDtlList.size());

				taPlanWorksheetSendRepository.save(planSendStampDate);
			}

		}
	}

	public void savePlanWorksheetSend(PlanWorksheetVo formVo) {
		String officeCode = UserLoginUtils.getCurrentUserBean().getOfficeCode();
		formVo.setOfficeCode(officeCode);
		TaPlanWorksheetSend planSend = taPlanWorksheetSendRepository
				.findByPlanNumberAndOfficeCode(formVo.getPlanNumber(), officeCode);
		Long count = taPlanWorksheetDtlRepository.countByCriteria(formVo);
		planSend.setFacInNum(new Integer(count.intValue()));
		planSend.setSubmitDate(LocalDate.now());
		taPlanWorksheetSendRepository.save(planSend);

		TaPlanWorksheetHdr planHdr = this.taPlanWorksheetHdrRepository.findByPlanNumber(formVo.getPlanNumber());

		if (FLAG.N_FLAG.equalsIgnoreCase(planHdr.getSendAllFlag())) {
			if (ExciseUtils.isCentral(officeCode)) {
				List<ExciseDepartment> sectorList = ApplicationCache.getExciseSectorList();
				for (ExciseDepartment sector : sectorList) {
					if (ExciseUtils.isCentral(sector.getOfficeCode())) {
						continue;
					}
					saveObjectTaPlanWorksheetSend(formVo, sector.getOfficeCode());
				}
			}
			if (ExciseUtils.isSector(officeCode)) {
				List<ExciseDepartment> areaList = ApplicationCache.getExciseAreaList(officeCode);
				for (ExciseDepartment area : areaList) {
					saveObjectTaPlanWorksheetSend(formVo, area.getOfficeCode());
				}
			}
		}

		List<TaPlanWorksheetDtl> planDtl = this.taPlanWorksheetDtlRepository.findByOfficeCodeAndPlanNumberLike(
				ExciseUtils.whereInLocalOfficeCode(officeCode), formVo.getPlanNumber());
		for (TaPlanWorksheetDtl dtl : planDtl) {
			dtl.setAuditStatus(ProjectConstants.TA_AUDIT_STATUS.CODE_0200);
			this.taPlanWorksheetDtlRepository.save(dtl);
		}

	}

	public void savePlanWorksheetSendToArea(PlanWorksheetVo formVo) {
		String officeCode = UserLoginUtils.getCurrentUserBean().getOfficeCode();
		formVo.setOfficeCode(officeCode);
		if (!ExciseUtils.isSector(officeCode)) {
			TaPlanWorksheetSend planSend = taPlanWorksheetSendRepository
					.findByPlanNumberAndOfficeCode(formVo.getPlanNumber(), officeCode);
			Long count = taPlanWorksheetDtlRepository.countByCriteria(formVo);
			planSend.setFacInNum(new Integer(count.intValue()));
			planSend.setSubmitDate(LocalDate.now());
			taPlanWorksheetSendRepository.save(planSend);
		}

		TaPlanWorksheetHdr planHdr = this.taPlanWorksheetHdrRepository.findByPlanNumber(formVo.getPlanNumber());

		if (FLAG.N_FLAG.equalsIgnoreCase(planHdr.getSendAllFlag())) {
			if (ExciseUtils.isCentral(officeCode)) {
				List<ExciseDepartment> sectorList = ApplicationCache.getExciseSectorList();
				for (ExciseDepartment sector : sectorList) {
					if (ExciseUtils.isCentral(sector.getOfficeCode())) {
						continue;
					}
//					saveObjectTaPlanWorksheetSendToArea(formVo, sector.getOfficeCode());
					saveObjectTaPlanWorksheetSendToSector(formVo, sector.getOfficeCode());
				}
			}
			if (ExciseUtils.isSector(officeCode)) {
				List<ExciseDepartment> areaList = ApplicationCache.getExciseAreaList(officeCode);
				for (ExciseDepartment area : areaList) {
					saveObjectTaPlanWorksheetSendToArea(formVo, area.getOfficeCode());
				}
			}
		}

		List<TaPlanWorksheetDtl> planDtl = this.taPlanWorksheetDtlRepository.findByOfficeCodeAndPlanNumberLike(
				ExciseUtils.whereInLocalOfficeCode(officeCode), formVo.getPlanNumber());
		for (TaPlanWorksheetDtl dtl : planDtl) {
			if (ExciseUtils.isCentral(officeCode)) {
				dtl.setAuditStatus(ProjectConstants.TA_AUDIT_STATUS.CODE_0200);
			} else {
				dtl.setAuditStatus(ProjectConstants.TA_AUDIT_STATUS.CODE_0101);
			}
			this.taPlanWorksheetDtlRepository.save(dtl);
		}
	}

	private void saveObjectTaPlanWorksheetSendToArea(PlanWorksheetVo formVo, String officeCode) {

		TaPlanWorksheetSend planSendStampDate = taPlanWorksheetSendRepository
				.findByPlanNumberAndOfficeCode(formVo.getPlanNumber(), officeCode);
		if (planSendStampDate == null) {
			planSendStampDate = new TaPlanWorksheetSend();
			planSendStampDate.setBudgetYear(formVo.getBudgetYear());
			planSendStampDate.setPlanNumber(formVo.getPlanNumber());
			planSendStampDate.setOfficeCode(officeCode);
			planSendStampDate.setSendDate(LocalDate.now());
			taPlanWorksheetSendRepository.save(planSendStampDate);
		}

	}

	private void saveObjectTaPlanWorksheetSendToSector(PlanWorksheetVo formVo, String officeCode) {

		Integer countCentalSend = taPlanWorksheetSelectRepository.findCentalAllSend();
		if (countCentalSend <= 0) {
			TaPlanWorksheetSend planSendStampDate = taPlanWorksheetSendRepository
					.findByPlanNumberAndOfficeCode(formVo.getPlanNumber(), officeCode);
			if (planSendStampDate == null) {
				planSendStampDate = new TaPlanWorksheetSend();
				planSendStampDate.setBudgetYear(formVo.getBudgetYear());
				planSendStampDate.setPlanNumber(formVo.getPlanNumber());
				planSendStampDate.setOfficeCode(officeCode);
				planSendStampDate.setSendDate(LocalDate.now());
				taPlanWorksheetSendRepository.save(planSendStampDate);
			}
		}
	}

	private void saveObjectTaPlanWorksheetSend(PlanWorksheetVo formVo, String officeCode) {
		TaPlanWorksheetSend planSendStampDate = taPlanWorksheetSendRepository
				.findByPlanNumberAndOfficeCode(formVo.getPlanNumber(), officeCode);

		formVo.setOfficeCode(officeCode);
		List<PlanWorksheetSendTableVo> planCount = taPlanWorksheetDtlRepository.findPlanWorksheetByDtl(formVo);
		planSendStampDate.setBudgetYear(formVo.getBudgetYear());
		planSendStampDate.setPlanNumber(formVo.getPlanNumber());
		planSendStampDate.setOfficeCode(officeCode);
		planSendStampDate.setSubmitDate(LocalDate.now());
		planSendStampDate.setFacInNum(planCount.get(0).getCountPlan());
		taPlanWorksheetSendRepository.save(planSendStampDate);
//		if (planSendStampDate == null) {
//			planSendStampDate = new TaPlanWorksheetSend();
//			planSendStampDate.setBudgetYear(formVo.getBudgetYear());
//			planSendStampDate.setPlanNumber(formVo.getPlanNumber());
//			planSendStampDate.setOfficeCode(officeCode);
//			planSendStampDate.setSubmitDate(LocalDate.now());
//			planSendStampDate.setFacInNum(planCount.get(0).getCountPlan());
//			taPlanWorksheetSendRepository.save(planSendStampDate);
//		}
	}

	public PlanWorksheetStatus getPlanHeaderStatus(PlanWorksheetVo formVo) {
		PlanWorksheetStatus status = new PlanWorksheetStatus();
		TaPlanWorksheetHdr planWsHdr = taPlanWorksheetHdrRepository.findByBudgetYear(formVo.getBudgetYear());

		if (planWsHdr != null) {
			ParamInfo statusDesc = ApplicationCache.getParamInfoByCode("TA_PLAN_STATUS", planWsHdr.getPlanStatus());
			if (statusDesc != null) {
				status.setPlanStatus(statusDesc.getParamCode());
				status.setApprovalComment(planWsHdr.getApprovalComment());
				status.setApprovedComment(planWsHdr.getApprovedComment());
				status.setPlanStatusDesc(statusDesc.getValue1());
			}
		}

		return status;
	}

	@Transactional
	public void insertComment(TaPlanWorksheetHdr form) {
		TaPlanWorksheetHdr comment = taPlanWorksheetHdrRepository.findByBudgetYear(form.getBudgetYear());
		if (StringUtils.isEmpty(form.getApprovedComment())) {
			comment.setApprovalBy(UserLoginUtils.getCurrentUsername());
			comment.setApprovalDate(LocalDateTime.now());
			comment.setApprovalComment(form.getApprovalComment());
//			comment.setPlanStatus("W");
			comment.setPlanStatus(ProjectConstants.TA_AUDIT_STATUS.CODE_0300);
		} else {
			comment.setApprovedBy(UserLoginUtils.getCurrentUsername());
			comment.setApprovedDate(LocalDateTime.now());
			comment.setApprovedComment(form.getApprovedComment());
//			comment.setPlanStatus("P");
			comment.setPlanStatus(ProjectConstants.TA_AUDIT_STATUS.CODE_0300);
		}
		taPlanWorksheetHdrRepository.save(comment);

		taPlanWorksheetDtlRepository.updateAuditStatusByPlanNumber(ProjectConstants.TA_AUDIT_STATUS.CODE_0300,
				comment.getPlanNumber());
	}

	@Transactional(rollbackFor = { Exception.class })
	public void clearDataByBudgetYear(String budgetYear) {
		logger.info("clearDataByBudgetYear budgetYear={}", budgetYear);
		taPlanWorksheetDtlRepository.forceDeleteByBudgetYear(budgetYear);
		taPlanWorksheetHdrRepository.forceDeleteByBudgetYear(budgetYear);
		taPlanWorksheetSendRepository.forceDeleteByBudgetYear(budgetYear);
		taPlanWorksheetSelectRepository.forceDeleteByBudgetYear(budgetYear);
		taWorksheetHdrRepository.updateWorksheetStatusByBudgetYear(TA_WORKSHEET_STATUS.CONDITION, budgetYear);
	}

	// TODO Get WS_REG4000 findBy NEW_REG_ID
	public PlanWorksheetDtlCusVo findByNewRegId(PlanWorksheetDtlCusVo formVo) {
		PlanWorksheetDtlCusVo dtlCus = new PlanWorksheetDtlCusVo();
		TaWsReg4000 reg4000 = new TaWsReg4000();
		reg4000 = reg4000Repository.getByNewRegId(formVo.getNewRegId());
		dtlCus.setWsReg4000Id(reg4000.getWsReg4000Id());
		dtlCus.setNewRegId(reg4000.getNewRegId());
		dtlCus.setCusId(reg4000.getCusId());
		dtlCus.setCusFullname(reg4000.getCusFullname());
		dtlCus.setCusAddress(reg4000.getCusAddress());
		dtlCus.setCusTelno(reg4000.getCusTelno());
		dtlCus.setCusEmail(reg4000.getCusEmail());
		dtlCus.setCusUrl(reg4000.getCusUrl());
		dtlCus.setFacId(reg4000.getFacId());
		dtlCus.setFacFullname(reg4000.getFacFullname());
		dtlCus.setFacAddress(reg4000.getFacAddress());
		dtlCus.setFacTelno(reg4000.getFacTelno());
		dtlCus.setFacEmail(reg4000.getFacEmail());
		dtlCus.setFacUrl(reg4000.getFacUrl());
		dtlCus.setOfficeCode(reg4000.getOfficeCode());
		dtlCus.setActiveFlag(reg4000.getActiveFlag());
		dtlCus.setDutyCode(reg4000.getDutyCode());
		dtlCus.setFacType(reg4000.getFacType());
		dtlCus.setRegStatus(reg4000.getRegStatus());
		dtlCus.setRegDate(reg4000.getRegDate().toString());
		dtlCus.setRegCapital(reg4000.getRegCapital());
		dtlCus.setDutyCodeDesc(ApplicationCache
				.getParamInfoByCode(PARAM_GROUP.EXCISE_PRODUCT_TYPE, reg4000.getDutyCode()).getValue1());
		return dtlCus;
	}

	@Transactional
	public TaPlanWorksheetDtl savePlanWorksheetDtlByAssing(PlanWorksheetDatatableVo formVo) {
		Optional<TaPlanWorksheetDtl> taPlanOpt = taPlanWorksheetDtlRepository.findById(formVo.getPlanWorksheetDtlId());
		TaPlanWorksheetDtl planDtl = new TaPlanWorksheetDtl();
		if (taPlanOpt.isPresent()) {
			planDtl = taPlanOpt.get();
			// TODO SET SOMETHING
			planDtl.setAuJobResp(formVo.getAuJobResp());
			planDtl.setAuSubdeptCode(formVo.getAuSubdeptCode());
			planDtl.setOfficeCode(formVo.getOfficeCode());
			planDtl.setAssignedOfficerBy(UserLoginUtils.getCurrentUserBean().getUsername());
			planDtl.setAssignedOfficerDate(LocalDateTime.now());
			planDtl.setAuditStatus(formVo.getAuditStatus());
			planDtl = taPlanWorksheetDtlRepository.save(planDtl);

		}

		return planDtl;
	}

	@Transactional
	public void saveStatusPlanWorksheetDtlByAssing(ExcisePerson formVo, String status) {
		taPlanWorksheetDtlRepository.updateStatusPlanWorksheetDtl(formVo, status);
	}

	public void savePlanWorksheetDtlByAssingList(PersonAssignForm formVo) {
		taPlanWorksheetDtlRepository.updateStatusPlanWorksheetDtlByList(formVo);

	}

	@Transactional
	public void savePlanWorksheetDtlAndAssign(PlanWorksheetAssignVo formVo) {
		UserBean userBean = UserLoginUtils.getCurrentUserBean();
		String officeCode = userBean.getOfficeCode();
		String budgetYear = formVo.getBudgetYear();

		logger.info("savePlanWorkSheetDtl officeCode={}, budgetYear={}, planNumber={}, analysisNumber={}, newRegIds={}",
				officeCode, budgetYear, formVo.getPlanNumber(), formVo.getAnalysisNumber(),
				org.springframework.util.StringUtils.collectionToCommaDelimitedString(formVo.getIds()));

		List<String> planNewRegIdList = taPlanWorksheetDtlRepository.findNewRegIdByPlanNumber(formVo.getPlanNumber());
		List<String> planNewRegIdFlagNList = taPlanWorksheetDtlRepository
				.findNewRegIdByPlanNumberAndIsDeletedFlagN(formVo.getPlanNumber());
		List<String> selectNewRegIdList = formVo.getIds();

		// Keep New and Delete newRegId list
		TaPlanWorksheetDtl planDtl = null;
		List<String> insertNewRegIdList = new ArrayList<>();
		List<String> updateNewRegIdList = new ArrayList<>();

		// Loop selectNewRegIdList for Insert NewRegId
		for (String selNewRegId : selectNewRegIdList) {
			if (!planNewRegIdList.contains(selNewRegId)) {
				insertNewRegIdList.add(selNewRegId);
			}
		}

		// Loop planWorksheetDtlNewRegIdList for Update NewRegId
		for (String planRewRegId : planNewRegIdList) {
			if (!selectNewRegIdList.contains(planRewRegId)) {
				updateNewRegIdList.add(planRewRegId);
			}
		}
		String assingOfficeCode = formVo.getSector();
		if (ExciseUtils.isCentral(formVo.getSector())) {
			assingOfficeCode = formVo.getSector();
		} else if (ExciseUtils.isArea(formVo.getArea())) {
			assingOfficeCode = formVo.getArea();
		}

		// Insert newRegId to planWorksheetDtl
		if (CollectionUtils.isNotEmpty(insertNewRegIdList)) {
			for (String newRegId : insertNewRegIdList) {
				planDtl = new TaPlanWorksheetDtl();
				planDtl.setPlanNumber(formVo.getPlanNumber());
				planDtl.setAnalysisNumber(formVo.getAnalysisNumber());
				planDtl.setOfficeCode(assingOfficeCode);
				planDtl.setNewRegId(newRegId);
				planDtl.setAuditStatus(TA_AUDIT_STATUS.CODE_0300);
//				planDtl.setAuSubdeptCode(userBean.getSubdeptCode());
				taPlanWorksheetDtlRepository.save(planDtl);

				updateFlagWorksheetSelect(budgetYear, newRegId, officeCode, FLAG.Y_FLAG, LocalDate.now());
			}
		}

		// Update newRegId from planWorksheetDtl
		String isDeletedPlanDtl = null;
		String selFlag = null;
		LocalDate selDate = null;
		if (CollectionUtils.isNotEmpty(updateNewRegIdList)) {
			for (String newRegId : updateNewRegIdList) {
				if (selectNewRegIdList.contains(newRegId)) {
					isDeletedPlanDtl = FLAG.N_FLAG;
					selFlag = FLAG.Y_FLAG;
					selDate = LocalDate.now();

				} else {
					isDeletedPlanDtl = FLAG.Y_FLAG;
					selFlag = null;
					selDate = null;
				}
				taPlanWorksheetDtlRepository.updateIsDeletedByPlanNumberAndNewRegId(isDeletedPlanDtl,
						formVo.getPlanNumber(), newRegId);
				updateFlagWorksheetSelect(budgetYear, newRegId, officeCode, selFlag, selDate);
			}
		} else {
			for (String selecNewRegId : selectNewRegIdList) {
				if (!planNewRegIdFlagNList.contains(selecNewRegId)) {
					isDeletedPlanDtl = FLAG.N_FLAG;
					selFlag = FLAG.Y_FLAG;
					selDate = LocalDate.now();
					taPlanWorksheetDtlRepository.updateIsDeletedByPlanNumberAndNewRegId(isDeletedPlanDtl,
							formVo.getPlanNumber(), selecNewRegId);
					updateFlagWorksheetSelect(budgetYear, selecNewRegId, officeCode, selFlag, selDate);
				}
			}
		}

//		find worksheet send for update count plan
		TaPlanWorksheetSend planSendStampDate = taPlanWorksheetSendRepository
				.findByPlanNumberAndOfficeCode(formVo.getPlanNumber(), assingOfficeCode);

		List<String> PlanDtlList = taPlanWorksheetDtlRepository
				.findNewRegIdByOfficeCodeAndPlanNumberAndIsDeletedFlagN(assingOfficeCode, formVo.getPlanNumber());
		if (planSendStampDate != null) {
//			logger.info(" find plan exist size is {} ",Integer.toString(PlanDtlList.size()));
			planSendStampDate.setFacInNum(PlanDtlList.size());

			taPlanWorksheetSendRepository.save(planSendStampDate);
		}

	}

}
