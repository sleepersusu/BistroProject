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
        obj.setReturnURL("http://localhost:8085/ecpay/callback");
        obj.setOrderResultURL("http://localhost:8085/payment-result");

        obj.setNeedExtraPaidInfo("N");

        String form = all.aioCheckOut(obj, null);
        return form;
    }
}

