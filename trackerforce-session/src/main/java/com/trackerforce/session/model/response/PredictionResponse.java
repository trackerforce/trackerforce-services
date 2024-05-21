package com.trackerforce.session.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PredictionResponse {

	@JsonProperty("prediction_accuracy")
	private long accuracy;

	private String predicted;

}
