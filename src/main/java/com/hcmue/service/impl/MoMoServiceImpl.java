package com.hcmue.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hcmue.constant.AppError;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.payment.MoMoRequest;
import com.hcmue.dto.payment.MoMoResponse;
import com.hcmue.dto.product.ProductDto;
import com.hcmue.service.MoMoService;

@Service
public class MoMoServiceImpl implements MoMoService{
	
	@Autowired
	private RestTemplate restTemplate;

	@Value("${DEV_MOMO_ENDPOINT}")
	private String apiUrl;
	
	@Value("${DEV_ACCESS_KEY}")
	private String accessKey;
	
	@Value("${DEV_PARTNER_CODE}")
	private String partnerCode;
	
	@Value("${DEV_SECRET_KEY}")
	private String secretKey;
	
	@Override
	public AppServiceResult<MoMoResponse> createPayment(Long amount, String notifyUrl, String returnUrl) {
		try {
			Long date = new Date().getTime();
			String requestId = date + "id";
			String orderId = date + ":012345678";
			boolean autoCapture = true;
			String requestType = "captureWallet";
			String orderInfo = "Thanh toán qua ví MoMo";
			String extraData = "ew0KImVtYWlsIjogImh1b25neGRAZ21haWwuY29tIg0KfQ==";
			String signature = "accessKey="+ accessKey +"&amount=" + amount +"&extraData=" + extraData+"&ipnUrl=" + notifyUrl+"&orderId=" + orderId+"&orderInfo=" + orderInfo + "&partnerCode=" + partnerCode +"&redirectUrl=" + returnUrl+"&requestId=" + requestId+"&requestType=" + requestType; 
//			var hash = CryptoJS.HmacSHA256(signature, secretKey);
//			signature = CryptoJS.enc.Hex.stringify(hash);
			final Mac hMacSHA256 = Mac.getInstance("HmacSHA256");
			byte[] hmacKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
			final SecretKeySpec secretKeySpec = new SecretKeySpec(hmacKeyBytes, "HmacSHA256");
			hMacSHA256.init(secretKeySpec);
			byte[] dataBytes = signature.getBytes(StandardCharsets.UTF_8);
			byte[] res = hMacSHA256.doFinal(dataBytes);

			
			signature = Hex.encodeHexString(res);
			
			MoMoResponse result = restTemplate.postForObject(apiUrl + "/create", new MoMoRequest(
					    partnerCode,
					    "Test",
					    partnerCode,
					    requestType,
					    notifyUrl,
					    returnUrl,
					    orderId,
					    amount,
					    "vi",
					    autoCapture,
					    orderInfo,
					    requestId,
					    extraData,
					    signature
					)
			, MoMoResponse.class);
			System.out.println(signature);
			return new AppServiceResult<MoMoResponse>(true, 0, "Succeed!", result);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<MoMoResponse>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}
	
}
