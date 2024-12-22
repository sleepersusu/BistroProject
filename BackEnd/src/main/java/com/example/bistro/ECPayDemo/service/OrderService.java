package com.example.bistro.ECPayDemo.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.bistro.ecpay.payment.integration.AllInOne;
import com.example.bistro.ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class OrderService {
	public String ecpayCheckout(String amount, String ordersName, String ordersTel) {
	    String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
	    AllInOne all = new AllInOne("");
	    AioCheckOutALL obj = new AioCheckOutALL();
	    
	    // 確保金額為整數字串
	    String totalAmount = String.valueOf(Math.round(Double.parseDouble(amount)));
	    
	    obj.setMerchantTradeNo(uuId);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    obj.setMerchantTradeDate(sdf.format(new Date()));
	    
	    obj.setTotalAmount(totalAmount); // 使用處理過的整數金額
	    obj.setTradeDesc("訂購人：" + ordersName + " 電話：" + ordersTel);
	    obj.setItemName("餐點訂單");
	    
	    // 設定回調 URL
//	    obj.setReturnURL("     https://5d0a-36-236-213-175.ngrok-free.app/ecpay/callback");
//	    obj.setOrderResultURL("https://5d0a-36-236-213-175.ngrok-free.app/payment-result");

		obj.setReturnURL("	   https://5d0a-36-236-213-175.ngrok-free.app/payment-result");
		obj.setOrderResultURL("https://5d0a-36-236-213-175.ngrok-free.app/ecpay/callback");

//	    obj.setClientBackURL("https://5d0a-36-236-213-175.ngrok-free.app/payment-result");
// 		不設定 ClientBackURL，因為已經在 payment-result 中處理轉導了
	    
	    obj.setNeedExtraPaidInfo("N");
	    
	    String form = all.aioCheckOut(obj, null);
	    return form;
	}
}


//public String ecpayCheckout(String amount, String ordersName, String ordersTel) {
//String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
//AllInOne all = new AllInOne("");
//AioCheckOutALL obj = new AioCheckOutALL();
//
//obj.setMerchantTradeNo(uuId);
//SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//obj.setMerchantTradeDate(sdf.format(new Date()));
//
//// 使用傳入的參數
//obj.setTotalAmount(amount);
//// 設定商品名稱，包含訂購人資訊
//obj.setTradeDesc("訂購人：" + ordersName + " 電話：" + ordersTel);
//obj.setItemName("餐點訂單");
//
//obj.setReturnURL("http://localhost:8085/ecpay/callback");
//obj.setNeedExtraPaidInfo("N");
//obj.setClientBackURL("http://localhost:8085/payment-result");
//
//String form = all.aioCheckOut(obj, null);
//return form;
//}




//	public String ecpayCheckout() {
//		
//		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);//生成一個 UUID（通用唯一識別碼）
//
//		AllInOne all = new AllInOne("");//負責處理支付流程
//
//		AioCheckOutALL obj = new AioCheckOutALL();//綠界支付 SDK 中用來封裝支付所需參數的類別
//		obj.setMerchantTradeNo(uuId);//設置商戶交易編號
//        // 使用現在時間
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//設置交易時間
//        obj.setMerchantTradeDate(sdf.format(new Date()));
//        
//		obj.setTotalAmount("50");
//		obj.setTradeDesc("test Description");
//		obj.setItemName("TestItem");
//		
//		// 設定本地測試用的回傳 URL
//        obj.setReturnURL("http://localhost:8085/ecpay/callback");
//        
//		obj.setNeedExtraPaidInfo("N");
//		
//        // ClientBackURL 是使用者付款完後，綠界會讓使用者的瀏覽器跳轉到這個網址
//        obj.setClientBackURL("http://localhost:8085/payment-result");
//        
//		String form = all.aioCheckOut(obj, null);
//
//		return form;
//	}