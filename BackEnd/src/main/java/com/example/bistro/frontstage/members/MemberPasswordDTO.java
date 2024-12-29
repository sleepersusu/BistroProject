package com.example.bistro.frontstage.members;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberPasswordDTO {
	private String phone;
	private String verificationCode;
	private String newPassword;
	private String confirmPassword;
}
