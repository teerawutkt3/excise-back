package th.go.excise.ims.scheduler.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.baiwa.buckwaframework.common.util.ConvertDateUtils;
import th.co.baiwa.buckwaframework.common.util.NumberUtils;
import th.co.baiwa.buckwaframework.security.constant.SecurityConstants.SYSTEM_USER;
import th.go.excise.ims.ia.persistence.entity.IaWsLic6010;
import th.go.excise.ims.ia.persistence.repository.IaWsLic6010Repository;
import th.go.excise.ims.ia.persistence.repository.jdbc.Int0604JdbcRepository;
import th.go.excise.ims.ws.client.pcc.common.exception.PccRestfulException;
import th.go.excise.ims.ws.client.pcc.licfri6010.model.License;
import th.go.excise.ims.ws.client.pcc.licfri6010.model.RequestData;
import th.go.excise.ims.ws.client.pcc.licfri6010.service.LicFri6010Service;
import th.go.excise.ims.ws.persistence.entity.WsLicfri6010;
import th.go.excise.ims.ws.persistence.repository.WsLicfri6010Repository;

@Service
public class SyncWsLicfri6010Service {

	private static final Logger logger = LoggerFactory.getLogger(SyncWsRegfri4000Service.class);

	private final int WS_DATA_SIZE = 1000;

	@Autowired
	private LicFri6010Service licFri6010Service;

	@Autowired
	private WsLicfri6010Repository wsLicfri6010Repository;

	@Autowired
	private Int0604JdbcRepository int0604JdbcRepository;

	@Autowired
	private IaWsLic6010Repository iaWsLic6010Repository;

	@Transactional(rollbackOn = { Exception.class })
	public void syncData(RequestData requestData) throws PccRestfulException {
		logger.info("syncData Licfri6010 Start");
		long start = System.currentTimeMillis();

		requestData.setDataPerPage(String.valueOf(WS_DATA_SIZE));
		int indexPage = 0;

		List<License> licenseList = null;
		WsLicfri6010 licfri6010 = null;
		List<WsLicfri6010> licfri6010List = new ArrayList<>();
		do {
			indexPage++;
			requestData.setPageNo(String.valueOf(indexPage));
			licenseList = licFri6010Service.execute(requestData).getLicenseList();
			if (licenseList != null && licenseList.size() > 0) {
				try {
					for (License license : licenseList) {
						licfri6010 = new WsLicfri6010();
						licfri6010.setOffcode(license.getOffcode());
						licfri6010.setLicType(license.getLicType());
						licfri6010.setLicNo(license.getLicNo());
						licfri6010.setLicName(license.getLicName());
						licfri6010.setLicFee(NumberUtils.toBigDecimal(license.getLicFee()));
						licfri6010.setLicInterior(NumberUtils.toBigDecimal(license.getLicInterior()));
						licfri6010.setLicPrice(NumberUtils.toBigDecimal(license.getLicPrice()));
						licfri6010.setLicDate(StringUtils.isNotEmpty(license.getLicDate()) ? ConvertDateUtils.parseStringToLocalDate(license.getLicDate(), ConvertDateUtils.YYYYMMDD, ConvertDateUtils.LOCAL_EN) : null);
						licfri6010.setStartDate(StringUtils.isNotEmpty(license.getLicDate()) ? ConvertDateUtils.parseStringToLocalDate(license.getStartDate(), ConvertDateUtils.YYYYMMDD, ConvertDateUtils.LOCAL_EN) : null);
						licfri6010.setExpDate(StringUtils.isNotEmpty(license.getLicDate()) ? ConvertDateUtils.parseStringToLocalDate(license.getExpDate(), ConvertDateUtils.YYYYMMDD, ConvertDateUtils.LOCAL_EN) : null);
						licfri6010.setSendDate(StringUtils.isNotEmpty(license.getLicDate()) ? ConvertDateUtils.parseStringToLocalDate(license.getSendDate(), ConvertDateUtils.YYYYMMDD, ConvertDateUtils.LOCAL_EN) : null);
						licfri6010.setPrintCount(NumberUtils.toBigDecimal(license.getPrintCount()));
						licfri6010.setNid(license.getNid());
						licfri6010.setNewRegId(license.getNewregId());
						licfri6010.setCusId(license.getCusId());
						licfri6010.setCusAddrseq(license.getCusAddrseq());
						licfri6010.setCusFullname(license.getCusFullName());
						licfri6010.setFacId(license.getFacId());
						licfri6010.setFacAddrseq(license.getFacAddrseq());
						licfri6010.setFacFullname(license.getFacFullName());
						licfri6010.setIncCode(license.getIncCode());
						licfri6010.setCreatedBy(SYSTEM_USER.BATCH);
						licfri6010.setCreatedDate(LocalDateTime.now());
						wsLicfri6010Repository.save(licfri6010);
						// licfri6010List.add(licfri6010);
					}

//					wsLicfri6010Repository.saveAll(licfri6010List);
//					wsLicfri6010Repository.batchInsert(licfri6010List);
					licfri6010List = new ArrayList<>();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} while (licenseList.size() == WS_DATA_SIZE);

		// Set dateStart and dateEnd
//		LocalDate localDateStart = LocalDate.parse(requestData.getYearMonthFrom() + "01", DateTimeFormatter.BASIC_ISO_DATE);
//		LocalDate localDateEnd = LocalDate.parse(requestData.getYearMonthTo() + "01", DateTimeFormatter.BASIC_ISO_DATE);
//		String dateStart = localDateStart.with(TemporalAdjusters.firstDayOfMonth()).format(DateTimeFormatter.BASIC_ISO_DATE);
//		String dateEnd = localDateEnd.with(TemporalAdjusters.lastDayOfMonth()).format(DateTimeFormatter.BASIC_ISO_DATE);

//		wsLicfri6010Repository.forceDeleteByOfficeCodeAndLicDate(requestData.getOffcode(), dateStart, dateEnd);

		logger.info("Batch Insert Licfri6010 Success");

		long end = System.currentTimeMillis();
		logger.info("syncData Licfri6010 Success, using {} seconds", (float) (end - start) / 1000F);
	}

	public void syncWs6010ToIaWs6010() {
		try {
			List<WsLicfri6010> wsLicfri6010List = int0604JdbcRepository.findWsLicfri6010Formapping();
			List<WsLicfri6010> nextLic = null;
			IaWsLic6010 iaWsLic6010;
			List<IaWsLic6010> iaWsLic6010List = new ArrayList<>();
			for (WsLicfri6010 wsLicfri6010 : wsLicfri6010List) {
				if ("0105547056889".equals(wsLicfri6010.getNid()) && "61M00001".equals(wsLicfri6010.getFacId()) && "ภส.08-02".equals(wsLicfri6010.getLicType())) {
					System.out.println();
				}
				if (StringUtils.isNotBlank(wsLicfri6010.getFacId())) {
					nextLic = new ArrayList<>();
					nextLic = int0604JdbcRepository.nextLic(wsLicfri6010);
					if (nextLic != null && nextLic.size() > 0) {

						iaWsLic6010 = new IaWsLic6010();
						iaWsLic6010.setCurrentLicId(wsLicfri6010.getWsLicfri6010Id());
						iaWsLic6010.setNewLicId(nextLic.get(0).getWsLicfri6010Id());
						iaWsLic6010.setNewLicDate(nextLic.get(0).getLicDate());
						iaWsLic6010.setNewLicNo(nextLic.get(0).getLicNo());
						iaWsLic6010List.add(iaWsLic6010);
					}
				}
			}
			iaWsLic6010Repository.saveAll(iaWsLic6010List);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
