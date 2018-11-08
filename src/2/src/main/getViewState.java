package main;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class getViewState {

	public static String viewState;
	public getViewState() {
	}
	public void find(String url) throws IOException {
		System.out.println("!@$%^&*())))))))))))))))(((((((((&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		
//		Document document = Jsoup.parse(response);
		Document document = Jsoup.connect(url).get();

		this.viewState = document.select("#__VIEWSTATE").attr("value");
		System.out.println(this.viewState);
	}
	public void findFromString(String html) throws IOException {
		System.out.println("!@$%^&*())))))))))))))))(((((((((&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		Document document = Jsoup.parse(html);

		this.viewState = document.select("#__VIEWSTATE").attr("value");
		System.out.println(this.viewState);
	}

}
