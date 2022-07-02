package com.hcmue.dto.purchase;

import java.util.Date;

import lombok.Data;

@Data
public class PurchaseResponse {

	private String orderTrackingNumber;
    private Date dateCreated;

    public PurchaseResponse(String orderTrackingNumber, Date dateCreated) {
        this.orderTrackingNumber = orderTrackingNumber;
        this.dateCreated = dateCreated;
    }
	
}
