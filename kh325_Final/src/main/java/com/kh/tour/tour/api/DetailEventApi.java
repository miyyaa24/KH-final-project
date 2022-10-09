package com.kh.tour.tour.api;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kh.tour.tour.model.vo.DetailEvent;
import com.kh.tour.tour.model.vo.DetailTourist;


public class DetailEventApi {
	public static String TOUR_URL = "http://apis.data.go.kr/B551011/KorService/areaBasedList";
	public static String TOUR_EVENT_DETAIL_URL = "http://apis.data.go.kr/B551011/KorService/detailIntro";
	public static String SERVICE_KEY = "0L5Iqaft7cvvAFwyEcDDJLm%2FdpmPNBvCDwpM7tL3LZHsL2prES1i20X6QCvzIhZpF7he8BEFj%2FhFQhIplu2YfA%3D%3D";
	public static String TOUR_URL_EXTRASTRING= "MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=C";
	public static String TOUR_EVENT_DETAIL_EXTRASTRING1= "MobileOS=ETC&MobileApp=AppTest";
	
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public static void main(String[] args) {
//		practice4.heritageInfo_Url("1", "2", "3");
		DetailEventApi.callCurrentDetailEventByXML();
	}
	
	public static List<DetailEvent> callCurrentDetailEventByXML() {
		List<DetailEvent> list = new ArrayList<>();
		
		for (int j = 1; j < 375; j++) {

			try {
				StringBuilder urlBuilder = new StringBuilder();
				urlBuilder.append(TOUR_URL);
				urlBuilder.append("?" + "serviceKey=" + SERVICE_KEY); // 첫 번째만 물음표로 설정
				urlBuilder.append("&" + "numOfRows=" + 100);
				urlBuilder.append("&" + "pageNo=" + j);
				urlBuilder.append("&" + TOUR_URL_EXTRASTRING);

				URL url = new URL(urlBuilder.toString());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Content-type", "application/json");
				conn.setRequestProperty("Accept", "application/xml");

				int code = conn.getResponseCode(); // 실제 호출하는 부
//				System.out.println("ResponseCode : " + code);
				if (code < 200 || code >= 300) {
					System.out.println("페이지가 잘못되었습니다.");
					return null;
				}

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(conn.getInputStream()); //
				doc.normalizeDocument();

				NodeList nList = doc.getElementsByTagName("item");
				for (int i = 0; i < nList.getLength(); i++) {
					Node node = nList.item(i);

//					System.out.println("\nCurrent Element : " + node.getNodeName());
					if (node.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) node;

						int contentId = getIntData(eElement, "contentid"); //
						int contentTypeId = getIntData(eElement, "contenttypeid"); //
			

						//-------------------------------------------------info 시작
						System.out.println(contentTypeId);
					
						if(contentTypeId == 15) {
						URL url2 = new URL(DetailEventApi.eventUrl(contentId, contentTypeId).toString());
						
						System.out.println("url : " + url2);
						
						HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
//						conn2.setRequestProperty("Content-type", "application/json");
						conn2.setRequestProperty("Accept", "application/xml");

						int code2 = conn2.getResponseCode(); // 실제 호출하는 부
						System.out.println("ResponseCode2 : " + code2);
						if (code2 < 200 || code2 >= 300) {
							System.out.println("페이지가 잘못되었습니다.");
							return null;
						}
						
						DocumentBuilderFactory dbf2 = DocumentBuilderFactory.newInstance();
						DocumentBuilder db2 = dbf2.newDocumentBuilder();
						Document doc2 = db2.parse(conn2.getInputStream()); //
						doc2.normalizeDocument();

						NodeList nList2 = doc2.getElementsByTagName("item");
						Node node2 = nList2.item(0);
						
//						System.out.println("\nCurrent Element2 : " + node.getNodeName());
						if (node2.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement2 = (Element) node2;
							
							
							String sponsor1 = getStrData(eElement2, "sponsor1");
							String sponsor1Tel = getStrData(eElement2, "sponsor1tel");
							String sponsor2 = getStrData(eElement2, "sponsor2");
							String sponsor2Tel = getStrData(eElement2, "sponsor2tel");
							int eventEndDate = getIntData(eElement2, "eventenddate");
							String playTime = getStrData(eElement2, "playtime");
							String eventPlace = getStrData(eElement2, "eventplace");
							String eventHomePage = getStrData(eElement2, "eventhomepage");
							String ageLimit = getStrData(eElement2, "ageLimit");
							String bookingPlace = getStrData(eElement2, "bookingplace");
							String placeInfo = getStrData(eElement2, "placeinfo");
							String subEvent = getStrData(eElement2, "subevent");
							String evProgram = getStrData(eElement2, "program");
							int eventStartDate = getIntData(eElement2, "eventstartdate");
							String useTimeFestival = getStrData(eElement2, "usetimefestival");
							String discountInfoFestival = getStrData(eElement2, "discountinfofestival");
							String spendTimeFestival = getStrData(eElement2, "spendtimefestival");
							String festivalGrade = getStrData(eElement2, "festivalgrade");
						
							DetailEvent detailEvent = new DetailEvent(contentId, contentTypeId, sponsor1, sponsor1Tel, sponsor2, sponsor2Tel, eventEndDate, 
									playTime, eventPlace, eventHomePage, ageLimit, bookingPlace, placeInfo, subEvent, evProgram, 
									eventStartDate, useTimeFestival, discountInfoFestival, spendTimeFestival, festivalGrade);
							list.add(detailEvent);
							System.out.println(list.toString());
						}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	private static String getStrData(Element eElement, String tagName) {
		try {
			return eElement.getElementsByTagName(tagName).item(0).getTextContent();
		} catch (Exception e) {
			return "-";
		}
	}

	private static int getIntData(Element eElement, String tagName) {
		try {
			return Integer.parseInt(eElement.getElementsByTagName(tagName).item(0).getTextContent());
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	// 정보페이지 url만들어서 넘김 
	public static StringBuffer eventUrl(int contentId, int contentTypeId) {
		StringBuffer infoUrl = new StringBuffer();
		infoUrl.append(TOUR_EVENT_DETAIL_URL);
		infoUrl.append("?" + "serviceKey=" + SERVICE_KEY);
		infoUrl.append("&" + TOUR_EVENT_DETAIL_EXTRASTRING1);
		infoUrl.append("&" + "contentId=" + contentId);
		infoUrl.append("&" + "contentTypeId=" + contentTypeId);
	
//			System.out.println(infoUrl);
		
		
		return infoUrl;
		
	}
	
}