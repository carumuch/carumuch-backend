package com.carumuch.capstone.bidding.presentation.dto.response;

import com.carumuch.capstone.bidding.domain.Bid;
import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.Location;

import lombok.Getter;


@Getter
public class BidResDto {
    private final Long id;
    private final int cost;
    private final String repairMethod;
    private final String bidStatus;
    private final BodyShopInfo bodyShop;

    public BidResDto(Bid bid) {
        this.id = bid.getId();
        this.cost = bid.getCost();
        this.repairMethod = bid.getRepairMethod();
        this.bidStatus = bid.getBidStatus().getKey();
        this.bodyShop = new BodyShopInfo(bid.getBodyShop());
    }

	//TODO: BodyShop의 레거시 Dto의 연관을 끊기 위해 임의로 작성한 Record입니다. 개선 작업이 필요합니다.
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
				bodyShop.isPickupAvailability()
			);
		}
	}
}
