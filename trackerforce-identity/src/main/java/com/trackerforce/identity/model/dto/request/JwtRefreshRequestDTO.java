package com.trackerforce.identity.model.dto.request;

import lombok.Data;

@Data
public class JwtRefreshRequestDTO {

	private String refreshToken;

}