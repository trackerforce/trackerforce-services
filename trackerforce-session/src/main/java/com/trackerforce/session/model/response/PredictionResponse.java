package com.trackerforce.session.model.response;

public class PredictionResponse {

	private long accuracy;

	private String predicted;

	public long getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(long accuracy) {
		this.accuracy = accuracy;
	}

	public String getPredicted() {
		return predicted;
	}

	public void setPredicted(String predicted) {
		this.predicted = predicted;
	}

}
