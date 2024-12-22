//package com.example.bistro.ECPayDemo.service;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.UUID;
//
//import org.springframework.stereotype.Service;
//
//import com.example.bistro.ecpay.payment.integration.AllInOne;
//import com.example.bistro.ecpay.payment.integration.domain.AioCheckOutALL;
//
//@Service
//public class OrderService2 {
//
//	public String ecpayCheckout() {
//		
//		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);//生成一個 UUID（通用唯一識別碼）
//
//		AllInOne all = new AllInOne("");//負責處理支付流程
//
//		AioCheckOutALL obj = new AioCheckOutALL();//綠界支付 SDK 中用來封裝支付所需參數的類別
//		obj.setMerchantTradeNo(uuId);//設置商戶交易編號
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//設置交易時間(現在時間)
//        obj.setMerchantTradeDate(sdf.format(new Date()));
//        
//        //設置交易金額、描述和商品名稱：
//		obj.setTotalAmount("50");
//		obj.setTradeDesc("test Description");
//		obj.setItemName("TestItem");
//		
//		// 設定本地測試用的回傳 URL
//        obj.setReturnURL("http://localhost:8085/ecpay/callback");
//        
//		obj.setNeedExtraPaidInfo("N");//設定是否需要額外的付款資訊
//		
//        // ClientBackURL 是使用者付款完後，綠界會讓使用者的瀏覽器跳轉到這個網址
//        obj.setClientBackURL("http://localhost:8085/payment-result");
//        
//		String form = all.aioCheckOut(obj, null);
//
//		return form;
//	}
//}