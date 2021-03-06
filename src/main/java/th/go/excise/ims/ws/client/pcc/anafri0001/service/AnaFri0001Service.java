package th.go.excise.ims.ws.client.pcc.anafri0001.service;

import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import th.go.excise.ims.ws.client.pcc.common.PccServiceProperties;
import th.go.excise.ims.ws.client.pcc.common.exception.PccRestfulException;
import th.go.excise.ims.ws.client.pcc.common.model.PccResponseHeader;
import th.go.excise.ims.ws.client.pcc.common.service.AbstractPccRestfulService;
import th.go.excise.ims.ws.client.service.RestfulClientService;
import th.go.excise.ims.ws.client.pcc.anafri0001.model.RequestData;
import th.go.excise.ims.ws.client.pcc.anafri0001.model.ResponseData;

@Service
public class AnaFri0001Service extends AbstractPccRestfulService<RequestData, ResponseData> {
	
	@Autowired
	public AnaFri0001Service(
			@Value("${ws.excise.endpoint.ana.anafri0001}") String url,
			PccServiceProperties pccServicePrpperties,
			RestfulClientService restfulClientService,
			Gson gson) {
		super.url = url;
		super.pccServicePrpperties = pccServicePrpperties;
		super.restfulClientService = restfulClientService;
		super.gson = gson;
	}
	
	@Override
	public ResponseData execute(RequestData requestData) throws PccRestfulException {
		return executePost(requestData);
	}

	@Override
	protected Type getResponseDataType() {
		return new TypeToken<PccResponseHeader<ResponseData>>(){}.getType();
	}

}
