package com.example.bistro.backstage.campaign;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.bistro.backstage.campaignPrize.CampaignPrizes;
import com.example.bistro.backstage.line.LineMember;
import com.example.bistro.backstage.line.LineMemberRepository;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class CampaignController {

	@Autowired
	CampaignService campaignService;
	
	@Autowired
	LineMemberRepository lineMemberRepo;
	
	 @Autowired
	LineMessagingClient lineMessagingClient;
	
	@GetMapping("/Bistro/campaign/findAll")
	public String getMethodName(Model model) {
		List<Campaign> allCampaign = campaignService.findAllCampaign();
		model.addAttribute("allCampaign", allCampaign);
		return "campaign/campaignView";
	}	
	
	@GetMapping("/campaign/download")
	public ResponseEntity<byte[]> downloadPhotos(@RequestParam Integer id) {
		Campaign campaign = campaignService.findCampaignById(id);

		byte[] campaignImg = campaign.getCampaignImg();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(campaignImg, headers, HttpStatus.OK);

	}
	
	@PostMapping("/Bistro/campaign/create")
	public String createPost(@RequestParam String campaignTitle, 
							@RequestParam MultipartFile campaignImg,
							@RequestParam String campaignDescription, 
							@RequestParam String campaignType, 
							@RequestParam Integer minOrderAmount,
							@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @RequestParam Date startDate,
							@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @RequestParam Date endDate,
							@RequestParam String note,
							@RequestParam(defaultValue = "false") boolean sendNotification) throws IOException {
		Campaign campaign = new Campaign();
		campaign.setCampaignTitle(campaignTitle);
		campaign.setCampaignImg(campaignImg.getBytes());
		campaign.setCampaignDescription(campaignDescription);
		campaign.setCampaignType(campaignType);
		campaign.setMinOrderAmount(minOrderAmount);
		campaign.setStartDate(startDate);
		campaign.setEndDate(endDate);
		campaign.setNote(note);
		
		Campaign savedCampaign = campaignService.insertCampaign(campaign);
		if (sendNotification) {
            try {
                sendMessage(savedCampaign);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		return "redirect:/Bistro/campaign/findAll";
	}
	
	
	@PostMapping("/Bistro/campaign/update")
	public String updatePost(@RequestParam Integer id,
	                        @RequestParam String campaignTitle,
	                        @RequestParam String campaignDescription,
	                        @RequestParam String campaignType,
	                        @RequestParam Integer minOrderAmount,
	                        @RequestParam String note,
	                        @RequestParam String startDate,
	                        @RequestParam String endDate,
	                        @RequestParam(required = false) MultipartFile campaignImg) {
	    try {
	        Campaign campaign = campaignService.findCampaignById(id);
	        campaign.setId(id);
	        campaign.setCampaignTitle(campaignTitle);
	        campaign.setCampaignDescription(campaignDescription);
	        campaign.setCampaignType(campaignType);
	        campaign.setMinOrderAmount(minOrderAmount);
	        campaign.setNote(note);
	        if (campaignImg != null && !campaignImg.isEmpty()) {
	            campaign.setCampaignImg(campaignImg.getBytes());
	        } 
	        else {
            Campaign existingCampaign = campaignService.findCampaignById(id);
            if (existingCampaign != null) {
                campaign.setCampaignImg(existingCampaign.getCampaignImg());
            }
	        }

	        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
	        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	        Date startDateTime = inputFormat.parse(startDate);
	        campaign.setStartDate(outputFormat.parse(outputFormat.format(startDateTime)));

	        Date endDateTime = inputFormat.parse(endDate);
	        campaign.setEndDate(outputFormat.parse(outputFormat.format(endDateTime)));

	        Campaign existingCampaign = campaignService.findCampaignById(id);
	        if (existingCampaign != null) {
	            campaign.setCreatedAt(existingCampaign.getCreatedAt());
	        }

	        campaignService.updateCampaign(campaign);
	        return "redirect:/Bistro/campaign/findAll";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/Bistro/campaign/findAll";
	    }
	}
	
	@PostMapping("/Bistro/campaign/delete")
	public String deleteById(@RequestParam Integer id) {
		campaignService.deleteCampaignById(id);
		return "redirect:/Bistro/campaign/findAll";
	}
	
	 private void sendMessage(Campaign campaign) {
	        String message = String.format(
	            "🎉 活動通知 🎉\n\n" +
	            "【%s】\n\n" +
	            "📅 活動期間：\n%s ~ %s\n\n" +
	            "✨ 活動內容：\n%s\n\n" +
	            "💰 消費門檻：%d元\n\n" +
	            "%s\n\n" + 
	            "更多詳情請至官網查看\n" +
	            "http://localhost:5173/campaign\n\n" +
	            "酌夜語 敬上",
	            campaign.getCampaignTitle(),
	            formatDate(campaign.getStartDate()),
	            formatDate(campaign.getEndDate()),
	            campaign.getCampaignDescription(),
	            campaign.getMinOrderAmount(),
	            campaign.getNote()
	        );

	        List<LineMember> allLineMember = lineMemberRepo.findAll();
	        
	        for (LineMember member : allLineMember) {
	            try {
	                TextMessage textMessage = new TextMessage(message);
	                PushMessage pushMessage = new PushMessage(member.getLineUserId(), textMessage);
	                lineMessagingClient.pushMessage(pushMessage).get();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    private String formatDate(Date date) {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	        return sdf.format(date);
	    }
	
	
	
	
	
}
