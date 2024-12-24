package com.example.bistro.frontstage.members;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class memberProfileDTO {
	private String userName;
	private String userEmail;
	private String userPhone;
	private Short userFavor;
	private Short userGender;
	private String userAddress;
	private Date userBirthdate;
}
