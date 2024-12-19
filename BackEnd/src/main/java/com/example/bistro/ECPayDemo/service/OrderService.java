package com.example.bistro.ECPayDemo.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.bistro.ecpay.payment.integration.AllInOne;
import com.example.bistro.ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class OrderService {

	public String ecpayCheckout() {
		
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);

		AllInOne all = new AllInOne("");

		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo(uuId);
        // 使用現在時間
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        obj.setMerchantTradeDate(sdf.format(new Date()));
        
		obj.setTotalAmount("50");
		obj.setTradeDesc("test Description");
		obj.setItemName("TestItem");
		
		// 設定本地測試用的回傳 URL
        obj.setReturnURL("http://localhost:8085/ecpay/callback");
        
		obj.setNeedExtraPaidInfo("N");
		
        // ClientBackURL 是使用者付款完後，綠界會讓使用者的瀏覽器跳轉到這個網址
        obj.setClientBackURL("http://localhost:8085/payment-result");
        
		String form = all.aioCheckOut(obj, null);

		return form;
	}
}