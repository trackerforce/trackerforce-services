package com.trackerforce.identity.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Organization {

	private String name;

	private String alias;

	public Organization(String alias) {
		this.alias = alias;
	}

}
