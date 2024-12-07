package com.example.bistro.frontstage.check;

import com.example.bistro.frontstage.check.linepay.vo.CheckoutPaymentRequestForm;
import com.example.bistro.frontstage.check.linepay.vo.ProductForm;
import com.example.bistro.frontstage.check.linepay.vo.ProductPackageForm;
import com.example.bistro.frontstage.check.linepay.vo.RedirectUrls;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

public class CartCheck {

    private CartCheck() {}

    public static String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, keys.getBytes()).doFinal(data.getBytes()));
    }

    public static String toBase64String(byte[] bytes) {
        // 使用 Base64 進行編碼
        byte[] byteArray = Base64.encodeBase64(bytes);
        // 確保使用正確的字符集將 byte 數組轉換為 String
        return new String(byteArray);
    }

    public static void main(String[] args) {

        CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();

        form.setAmount(new BigDecimal("100"));
        form.setCurrency("JPY");
        form.setOrderId("merchant_order_id");

        ProductPackageForm productPackageForm = new ProductPackageForm();
        productPackageForm.setId("package_id");
        productPackageForm.setName("shop_name");
        productPackageForm.setAmount(new BigDecimal("100"));

        ProductForm productForm = new ProductForm();
        productForm.setId("product_id");
        productForm.setName("product_name");
        productForm.setImageUrl("");
        productForm.setQuantity(new BigDecimal("10"));
        productForm.setPrice(new BigDecimal("10"));
        productPackageForm.setProducts(Arrays.asList(productForm));

        form.setPackages(Arrays.asList(productPackageForm));
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setConfirmUrl("");
        form.setRedirectUrls(redirectUrls);

        Gson gson = new GsonBuilder().create();
        //https://pay.line.me/tw/center/payment/interlockKey?locale=zh_TW#
        String ChannelSecret = "49ddab45fef813d93579bda5d6f7a8cd";
        //2024
        String requestUri = "/v3/payments/request";
        String nonce = UUID.randomUUID().toString();
        String signature = encrypt(ChannelSecret, ChannelSecret + requestUri + gson.toJson(form) + nonce);

        // 打印簽名結果以驗證
        System.out.println("Signature: " + signature);
    }
}
