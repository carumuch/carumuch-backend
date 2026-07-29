package com.carumuch.capstone.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Column(name = "sido")
    private String sido;

    @Column(name = "sigungu")
    private String siqungu;

    @Column(name = "bname")
    private String bname;

    @Column(name = "jibunAddress")
    private String jibunAddress;

    @Column(name = "roadAddress")
    private String roadAddress;

	@Column(name = "detail")
    private String detail;

    public Location(
		String sido,
		String siqungu,
		String bname,
		String jibunAddress,
		String roadAddress,
		String detail
	) {
        this.sido = sido;
        this.siqungu = siqungu;
        this.bname = bname;
        this.jibunAddress = jibunAddress;
        this.roadAddress = roadAddress;
        this.detail = detail;
    }
}
