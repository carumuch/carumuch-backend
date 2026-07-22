package com.carumuch.capstone.bidding.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.support.fixture.BodyShopFixture;
import com.carumuch.capstone.support.fixture.EstimateFixture;

class BidTest {

	private static final Long BIDDER_USER_ID = 1L;
	private static final Long ESTIMATE_REQUESTER_USER_ID = 10L;
	private static final Long OTHER_USER_ID = 999L;

	@Test
	@DisplayName("열린 견적서에 입찰을 생성하면 대기 상태로 저장한다")
	void 열린_견적서에_입찰을_생성하면_대기_상태로_저장한다() {
		BodyShop bodyShop = createBodyShop();
		Estimate estimate = createOpenEstimate();

		Bid bid = Bid.apply(100_000, "판금 도색", bodyShop, estimate);

		assertThat(bid.getCost()).isEqualTo(100_000);
		assertThat(bid.getRepairMethod()).isEqualTo("판금 도색");
		assertThat(bid.getBidStatus()).isEqualTo(BidStatus.WAITING);
		assertThat(bid.getBodyShop()).isSameAs(bodyShop);
		assertThat(bid.getEstimate()).isSameAs(estimate);
	}

	@Test
	@DisplayName("닫힌 견적서에는 입찰을 생성할 수 없다")
	void 닫힌_견적서에는_입찰을_생성할_수_없다() {
		Estimate closedEstimate = createClosedEstimate();

		assertThatThrownBy(() -> Bid.apply(100_000, "판금 도색", createBodyShop(), closedEstimate))
			.isInstanceOf(CustomException.class)
			.extracting("status", "message")
			.containsExactly(HttpStatus.BAD_REQUEST, "입찰이 종료된 견적서 입니다.");
	}

	@Test
	@DisplayName("입찰 금액이 0 이하이면 입찰을 생성할 수 없다")
	void 입찰_금액이_0_이하이면_입찰을_생성할_수_없다() {
		assertThatThrownBy(() -> Bid.apply(0, "판금 도색", createBodyShop(), createOpenEstimate()))
			.isInstanceOf(CustomException.class)
			.extracting("status", "message")
			.containsExactly(HttpStatus.BAD_REQUEST, "입찰 금액은 0원보다 커야 합니다.");
	}

	@Test
	@DisplayName("대기 중인 입찰은 수락할 수 있다")
	void 대기_중인_입찰은_수락할_수_있다() {
		Bid bid = createWaitingBid();

		bid.accept();

		assertThat(bid.getBidStatus()).isEqualTo(BidStatus.ACCEPTED);
	}

	@Test
	@DisplayName("대기 중인 입찰은 거절할 수 있다")
	void 대기_중인_입찰은_거절할_수_있다() {
		Bid bid = createWaitingBid();

		bid.reject();

		assertThat(bid.getBidStatus()).isEqualTo(BidStatus.REJECTED);
	}

	@Test
	@DisplayName("대기 중인 입찰은 취소할 수 있다")
	void 대기_중인_입찰은_취소할_수_있다() {
		Bid bid = createWaitingBid();

		bid.cancel();

		assertThat(bid.getBidStatus()).isEqualTo(BidStatus.CANCELED);
	}

	@Test
	@DisplayName("대기 상태가 아닌 입찰은 추가로 처리할 수 없다")
	void 대기_상태가_아닌_입찰은_추가로_처리할_수_없다() {
		Bid bid = createWaitingBid();
		bid.accept();

		assertThatThrownBy(bid::reject)
			.isInstanceOf(CustomException.class)
			.extracting("status", "message")
			.containsExactly(HttpStatus.BAD_REQUEST, "대기 중인 입찰만 처리할 수 있습니다.");
	}

	@Test
	@DisplayName("대기 중인 입찰은 금액과 수리 방식을 수정할 수 있다")
	void 대기_중인_입찰은_금액과_수리_방식을_수정할_수_있다() {
		Bid bid = createWaitingBid();

		bid.update(150_000, "교환 수리");

		assertThat(bid.getCost()).isEqualTo(150_000);
		assertThat(bid.getRepairMethod()).isEqualTo("교환 수리");
	}

	@Test
	@DisplayName("대기 상태가 아닌 입찰은 수정할 수 없다")
	void 대기_상태가_아닌_입찰은_수정할_수_없다() {
		Bid bid = createWaitingBid();
		bid.cancel();

		assertThatThrownBy(() -> bid.update(150_000, "교환 수리"))
			.isInstanceOf(CustomException.class)
			.extracting("status", "message")
			.containsExactly(HttpStatus.BAD_REQUEST, "대기 중인 입찰만 처리할 수 있습니다.");
	}

	@Test
	@DisplayName("입찰 수정 시 금액이 0 이하이면 예외가 발생한다")
	void 입찰_수정_시_금액이_0_이하이면_예외가_발생한다() {
		Bid bid = createWaitingBid();

		assertThatThrownBy(() -> bid.update(0, "교환 수리"))
			.isInstanceOf(CustomException.class)
			.extracting("status", "message")
			.containsExactly(HttpStatus.BAD_REQUEST, "입찰 금액은 0원보다 커야 합니다.");
	}

	@Test
	@DisplayName("입찰 공업사는 자신의 입찰을 검증할 수 있다")
	void 입찰_공업사는_자신의_입찰을_검증할_수_있다() {
		Bid bid = createWaitingBid();

		assertThatCode(() -> bid.validateBidder(BIDDER_USER_ID))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("입찰 공업사가 아니면 입찰자 검증에서 예외가 발생한다")
	void 입찰_공업사가_아니면_입찰자_검증에서_예외가_발생한다() {
		Bid bid = createWaitingBid();

		assertThatThrownBy(() -> bid.validateBidder(OTHER_USER_ID))
			.isInstanceOf(CustomException.class)
			.extracting("status", "message")
			.containsExactly(HttpStatus.FORBIDDEN, "입찰한 공업사만 처리할 수 있습니다.");
	}

	@Test
	@DisplayName("견적서 작성자는 자신의 입찰을 검증할 수 있다")
	void 견적서_작성자는_자신의_입찰을_검증할_수_있다() {
		Bid bid = createWaitingBid();

		assertThatCode(() -> bid.validateEstimateRequester(ESTIMATE_REQUESTER_USER_ID))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("견적서 작성자가 아니면 견적서 소유자 검증에서 예외가 발생한다")
	void 견적서_작성자가_아니면_견적서_소유자_검증에서_예외가_발생한다() {
		Bid bid = createWaitingBid();

		assertThatThrownBy(() -> bid.validateEstimateRequester(OTHER_USER_ID))
			.isInstanceOf(CustomException.class)
			.extracting("status", "message")
			.containsExactly(HttpStatus.FORBIDDEN, "견적서 주인만 처리할 수 있습니다.");
	}

	@Test
	@DisplayName("입찰 공업사와 견적서 작성자만 입찰에 접근할 수 있다")
	void 입찰_공업사와_견적서_작성자만_입찰에_접근할_수_있다() {
		Bid bid = createWaitingBid();

		assertThat(bid.canAccess(BIDDER_USER_ID)).isTrue();
		assertThat(bid.canAccess(ESTIMATE_REQUESTER_USER_ID)).isTrue();
		assertThat(bid.canAccess(OTHER_USER_ID)).isFalse();
	}

	private Bid createWaitingBid() {
		return Bid.apply(100_000, "판금 도색", createBodyShop(), createOpenEstimate());
	}

	private BodyShop createBodyShop() {
		return BodyShopFixture.BODY_SHOP_FIXTURE_1.create(BIDDER_USER_ID);
	}

	private Estimate createOpenEstimate() {
		return EstimateFixture.ESTIMATE_FIXTURE_1.create();
	}

	private Estimate createClosedEstimate() {
		return EstimateFixture.ESTIMATE_FIXTURE_2.create();
	}
}
