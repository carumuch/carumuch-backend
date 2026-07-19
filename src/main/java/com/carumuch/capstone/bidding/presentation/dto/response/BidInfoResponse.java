package com.carumuch.capstone.bidding.presentation.dto.response;

import com.carumuch.capstone.bidding.domain.Bid;
import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.Location;


public record BidInfoResponse(
	Long id,
	int cost,
	String repairMethod,
	String bidStatus,
	BodyShopInfo bodyShopInfo
) {
    public BidInfoResponse(Bid bid) {
		this(
			bid.getId(),
			bid.getCost(),
			bid.getRepairMethod(),
			bid.getBidStatus().getKey(),
			new BodyShopInfo(bid.getBodyShop())
		);
    }

	public record BodyShopInfo(
		Long id,
		String name,
		Location location,
		String description,
		String phoneNumber,
		int acceptCount,
		String link,
		boolean pickupAvailability
	) {
		private BodyShopInfo(BodyShop bodyShop) {
			this(
				bodyShop.getId(),
				bodyShop.getName(),
				bodyShop.getLocation(),
				bodyShop.getDescription(),
				bodyShop.getPhoneNumber().getValue(),
				bodyShop.getAcceptCount(),
				bodyShop.getLink(),
				bodyShop.isPickupAvailable()
			);
		}
	}
}
