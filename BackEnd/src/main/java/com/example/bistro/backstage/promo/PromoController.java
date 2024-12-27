package com.example.bistro.backstage.promo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PromoController {
    
    @Autowired
    private PromoService promoService;

    @GetMapping("/Bistro/PromoBean/showAll")
    public String showAll(Model model) {
        List<promoDTO> promoList = promoService.findAllPromo();
        model.addAttribute("PromoBeanList", promoList);
        return "pointPrizes/showPromo";
    }

    @PostMapping("/Bistro/PromoBean/create")
    public String createPromo(@RequestParam String promoCode, 
                            @RequestParam Integer memberId,
                            @RequestParam Integer pointPrizesId, 
                            Model model) {
        try {
            promoService.createPromo(promoCode, memberId, pointPrizesId);
            model.addAttribute("message", "會員優惠卷已成功新增！");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "pointPrizes/showPromo";
        }
        return "redirect:/Bistro/PromoBean/showAll";
    }

    @PostMapping("/Bistro/PromoBean/delete")
    public String deletePromo(@RequestParam Integer id) {
        promoService.deletePromoById(id);
        return "redirect:/Bistro/PromoBean/showAll";
    }
}