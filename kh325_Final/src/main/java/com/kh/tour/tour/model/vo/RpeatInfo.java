package com.kh.tour.tour.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpeatInfo {
	private int reSeq;		   // 반복시퀀스
	private int contentId;     // 콘텐츠ID
	private int serialNum;     // 반복일련번호
	private int fldgubun;      // 일련번호
	private String infoName;   // 제목
	private String infoText;   // 내용

}
