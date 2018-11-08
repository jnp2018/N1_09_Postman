package main;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class workFollow {
	public static String theFinalSourceHTML = null;
    public workFollow(String user, char[] pass) throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
        try {
//  GUI GET REQUEST DE VAO TRANG DANG NHAP CUA HE THONG QLDT PTIT
            HttpGet httpget = new HttpGet("http://qldt.ptit.edu.vn/Default.aspx?page=dangnhap");
            CloseableHttpResponse response1 = httpclient.execute(httpget);
            
            getViewState getviewstate = new getViewState();
            try {
                HttpEntity entity = response1.getEntity();
            	getviewstate.find("http://qldt.ptit.edu.vn/Default.aspx?page=dangnhap");
                
                System.out.println("Login form get: " + response1.getStatusLine());
                EntityUtils.consume(entity);

                System.out.println("Initial set of cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
                response1.close();
            }

//  GUI POST REQUEST DE DANG NHAP TREN HE THONG QLDT PTIT
            HttpEntity entity0 = MultipartEntityBuilder
                    .create()
                    .addTextBody("__EVENTTARGET", "")
                    .addTextBody("__EVENTARGUMENT", "")
                    .addTextBody("__VIEWSTATE", getviewstate.viewState)
                    .addTextBody("__VIEWSTATEGENERATOR", "CA0B0334")
                    .addTextBody("ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$txtTaiKhoa", user)
                    .addTextBody("ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$txtMatKhau", String.valueOf(pass))
                    .addTextBody("ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$btnDangNhap", "Đăng Nhập")
                    .build();

            HttpPost login = new HttpPost("http://qldt.ptit.edu.vn/default.aspx");
            login.setEntity(entity0);

            CloseableHttpResponse response2 = httpclient.execute(login);
            
//            Document document = Jsoup.parse(response2.toString());
//            String viewState = document.select("#__VIEWSTATE").attr("value");
//            System.out.println(viewState);
//            String eventValidate = document.select("#__EVENTVALIDATION").attr("value");

            try {
                HttpEntity entity = response2.getEntity();

                System.out.println(">>> If response status == 302 => Dang nhap thanh cong <3...");
                System.out.println("Login form get: " + response2.getStatusLine());
                EntityUtils.consume(entity);

                System.out.println("Post logon cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
                response2.close();
            }
            
//  GUI GET REQUEST DE VAO TRANG XEM DIEM THI
            HttpGet page = new HttpGet("http://qldt.ptit.edu.vn/Default.aspx?page=xemdiemthi");
            CloseableHttpResponse response3 = httpclient.execute(page);
            System.out.println(">>> print response3 (GET REQUEST DE VAO TRANG XEM DIEM THI):");
            String responseString0 = new BasicResponseHandler().handleResponse(response3);
            getviewstate.findFromString(responseString0);
            System.out.println(responseString0);
            FileUtils.writeStringToFile(new File("response3.html"), responseString0, "UTF-8");
//  GUI POST REQUEST DE XEM DIEM TAT CA HOC KY (TU KI 1 DEN BAY GIO)
            HttpPost login1 = new HttpPost("http://qldt.ptit.edu.vn/Default.aspx?page=xemdiemthi");
            HttpEntity entity1 = MultipartEntityBuilder
                    .create()
                    .addTextBody("__EVENTTARGET", "ctl00$ContentPlaceHolder1$ctl00$lnkChangeview2")
                    .addTextBody("__EVENTARGUMENT", "")
                    .addTextBody("__VIEWSTATE", getViewState.viewState)
                    .addTextBody("__VIEWSTATEGENERATOR", "CA0B0334")
                    .addTextBody("ctl00$ContentPlaceHolder1$ctl00$txtChonHK", "")
                    .build();

            login1.setEntity(entity1);

            CloseableHttpResponse response4 = httpclient.execute(login1);
            try {
                HttpEntity entity2 = response4.getEntity();
                System.out.println("Login form get: " + response4.getStatusLine());

                System.out.println("Post logon cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
                
                System.out.println(">>> print response4 (POST REQUEST DE XEM DIEM TAT CA HOC KY (TU KI 1 DEN BAY GIO)):");
                if(entity2!=null) {
                    theFinalSourceHTML = new String();
                    theFinalSourceHTML = EntityUtils.toString(entity2);
                    System.out.println(theFinalSourceHTML);
                    FileUtils.writeStringToFile(new File("theFinalSourceHTML.html"), theFinalSourceHTML, "UTF-8");
                }
                EntityUtils.consume(entity2);
                
            } finally {
                response4.close();
            }
        } finally {
            httpclient.close();
        }
        
//        PHAN TICH SOURCE HTML: lay du lieu diem cua mon hoc ki vua roi  (use Jsoup)
        System.out.println(">>> print du lieu diem cua hoc ki vua roi phan tich tu source HTML:");
        if (theFinalSourceHTML != null) {
            Document doc = Jsoup.parse(theFinalSourceHTML);
            Element diemCNPM = doc.select("#ctl00_ContentPlaceHolder1_ctl00_div1 > table > tbody > tr:nth-child(77)").first();
            String theFinalDiem = "Diem cua mon " + diemCNPM.getElementsByTag("span").get(2).text() + "\n" +
                            "STT: " + diemCNPM.getElementsByTag("span").get(0).text() + "\n" +
                            "Mã Môn: " + diemCNPM.getElementsByTag("span").get(1).text() + "\n" +
                            "Tên Môn: " + diemCNPM.getElementsByTag("span").get(2).text() + "\n" +
                            "TC: " + diemCNPM.getElementsByTag("span").get(3).text() + "\n" +
                            "%CC: " + diemCNPM.getElementsByTag("span").get(4).text() + "\n" +
                            "%KT: " + diemCNPM.getElementsByTag("span").get(5).text() + "\n" +
                            "%TH: " + diemCNPM.getElementsByTag("span").get(6).text() + "\n" +
                            "%BT: " + diemCNPM.getElementsByTag("span").get(7).text() + "\n" +
                            "%Thi: " + diemCNPM.getElementsByTag("span").get(8).text() + "\n" +
                            "Điểm CC: " + diemCNPM.getElementsByTag("span").get(9).text() + "\n" +
                            "Điểm KT: " + diemCNPM.getElementsByTag("span").get(10).text() + "\n" +
                            "Điểm TH: " + diemCNPM.getElementsByTag("span").get(12).text() + "\n" +
                            "Điểm BT: " + diemCNPM.getElementsByTag("span").get(13).text() + "\n" +
                            "Điểm Thi: " + diemCNPM.getElementsByTag("span").get(15).text() + "\n" +
                            "TK(10): " + diemCNPM.getElementsByTag("span").get(16).text() + "\n";
            
            Element diem1 = doc.select("#ctl00_ContentPlaceHolder1_ctl00_div1 > table > tbody > tr:nth-child(78)").first();
            theFinalDiem += "\nDiem cua mon " + diem1.getElementsByTag("span").get(2).text() + "\n" +
                            "STT: " + diem1.getElementsByTag("span").get(0).text() + "\n" +
                            "Mã Môn: " + diem1.getElementsByTag("span").get(1).text() + "\n" +
                            "Tên Môn: " + diem1.getElementsByTag("span").get(2).text() + "\n" +
                            "TC: " + diem1.getElementsByTag("span").get(3).text() + "\n" +
                            "%CC: " + diem1.getElementsByTag("span").get(4).text() + "\n" +
                            "%KT: " + diem1.getElementsByTag("span").get(5).text() + "\n" +
                            "%TH: " + diem1.getElementsByTag("span").get(6).text() + "\n" +
                            "%BT: " + diem1.getElementsByTag("span").get(7).text() + "\n" +
                            "%Thi: " + diem1.getElementsByTag("span").get(8).text() + "\n" +
                            "Điểm CC: " + diem1.getElementsByTag("span").get(9).text() + "\n" +
                            "Điểm KT: " + diem1.getElementsByTag("span").get(10).text() + "\n" +
                            "Điểm TH: " + diem1.getElementsByTag("span").get(12).text() + "\n" +
                            "Điểm BT: " + diem1.getElementsByTag("span").get(13).text() + "\n" +
                            "Điểm Thi: " + diem1.getElementsByTag("span").get(15).text() + "\n" +
                            "TK(10): " + diem1.getElementsByTag("span").get(16).text() + "\n";
            
            Element diem2 = doc.select("#ctl00_ContentPlaceHolder1_ctl00_div1 > table > tbody > tr:nth-child(79)").first();
            theFinalDiem += "\nDiem cua mon " + diem2.getElementsByTag("span").get(2).text() + "\n" +
                            "STT: " + diem2.getElementsByTag("span").get(0).text() + "\n" +
                            "Mã Môn: " + diem2.getElementsByTag("span").get(1).text() + "\n" +
                            "Tên Môn: " + diem2.getElementsByTag("span").get(2).text() + "\n" +
                            "TC: " + diem2.getElementsByTag("span").get(3).text() + "\n" +
                            "%CC: " + diem2.getElementsByTag("span").get(4).text() + "\n" +
                            "%KT: " + diem2.getElementsByTag("span").get(5).text() + "\n" +
                            "%TH: " + diem2.getElementsByTag("span").get(6).text() + "\n" +
                            "%BT: " + diem2.getElementsByTag("span").get(7).text() + "\n" +
                            "%Thi: " + diem2.getElementsByTag("span").get(8).text() + "\n" +
                            "Điểm CC: " + diem2.getElementsByTag("span").get(9).text() + "\n" +
                            "Điểm KT: " + diem2.getElementsByTag("span").get(10).text() + "\n" +
                            "Điểm TH: " + diem2.getElementsByTag("span").get(12).text() + "\n" +
                            "Điểm BT: " + diem2.getElementsByTag("span").get(13).text() + "\n" +
                            "Điểm Thi: " + diem2.getElementsByTag("span").get(15).text() + "\n" +
                            "TK(10): " + diem2.getElementsByTag("span").get(16).text() + "\n";
            
            Element diem3 = doc.select("#ctl00_ContentPlaceHolder1_ctl00_div1 > table > tbody > tr:nth-child(80)").first();
            theFinalDiem += "\nDiem cua mon " + diem3.getElementsByTag("span").get(2).text() + "\n" +
                            "STT: " + diem3.getElementsByTag("span").get(0).text() + "\n" +
                            "Mã Môn: " + diem3.getElementsByTag("span").get(1).text() + "\n" +
                            "Tên Môn: " + diem3.getElementsByTag("span").get(2).text() + "\n" +
                            "TC: " + diem3.getElementsByTag("span").get(3).text() + "\n" +
                            "%CC: " + diem3.getElementsByTag("span").get(4).text() + "\n" +
                            "%KT: " + diem3.getElementsByTag("span").get(5).text() + "\n" +
                            "%TH: " + diem3.getElementsByTag("span").get(6).text() + "\n" +
                            "%BT: " + diem3.getElementsByTag("span").get(7).text() + "\n" +
                            "%Thi: " + diem3.getElementsByTag("span").get(8).text() + "\n" +
                            "Điểm CC: " + diem3.getElementsByTag("span").get(9).text() + "\n" +
                            "Điểm KT: " + diem3.getElementsByTag("span").get(10).text() + "\n" +
                            "Điểm TH: " + diem3.getElementsByTag("span").get(12).text() + "\n" +
                            "Điểm BT: " + diem3.getElementsByTag("span").get(13).text() + "\n" +
                            "Điểm Thi: " + diem3.getElementsByTag("span").get(15).text() + "\n" +
                            "TK(10): " + diem3.getElementsByTag("span").get(16).text() + "\n";
            
            Element diem4 = doc.select("#ctl00_ContentPlaceHolder1_ctl00_div1 > table > tbody > tr:nth-child(81)").first();
            theFinalDiem += "\nDiem cua mon " + diem4.getElementsByTag("span").get(2).text() + "\n" +
                            "STT: " + diem4.getElementsByTag("span").get(0).text() + "\n" +
                            "Mã Môn: " + diem4.getElementsByTag("span").get(1).text() + "\n" +
                            "Tên Môn: " + diem4.getElementsByTag("span").get(2).text() + "\n" +
                            "TC: " + diem4.getElementsByTag("span").get(3).text() + "\n" +
                            "%CC: " + diem4.getElementsByTag("span").get(4).text() + "\n" +
                            "%KT: " + diem4.getElementsByTag("span").get(5).text() + "\n" +
                            "%TH: " + diem4.getElementsByTag("span").get(6).text() + "\n" +
                            "%BT: " + diem4.getElementsByTag("span").get(7).text() + "\n" +
                            "%Thi: " + diem4.getElementsByTag("span").get(8).text() + "\n" +
                            "Điểm CC: " + diem4.getElementsByTag("span").get(9).text() + "\n" +
                            "Điểm KT: " + diem4.getElementsByTag("span").get(10).text() + "\n" +
                            "Điểm TH: " + diem4.getElementsByTag("span").get(12).text() + "\n" +
                            "Điểm BT: " + diem4.getElementsByTag("span").get(13).text() + "\n" +
                            "Điểm Thi: " + diem4.getElementsByTag("span").get(15).text() + "\n" +
                            "TK(10): " + diem4.getElementsByTag("span").get(16).text() + "\n";
            
            Element diem5 = doc.select("#ctl00_ContentPlaceHolder1_ctl00_div1 > table > tbody > tr:nth-child(82)").first();
            theFinalDiem += "\nDiem cua mon " + diem5.getElementsByTag("span").get(2).text() + "\n" +
                            "STT: " + diem5.getElementsByTag("span").get(0).text() + "\n" +
                            "Mã Môn: " + diem5.getElementsByTag("span").get(1).text() + "\n" +
                            "Tên Môn: " + diem5.getElementsByTag("span").get(2).text() + "\n" +
                            "TC: " + diem5.getElementsByTag("span").get(3).text() + "\n" +
                            "%CC: " + diem5.getElementsByTag("span").get(4).text() + "\n" +
                            "%KT: " + diem5.getElementsByTag("span").get(5).text() + "\n" +
                            "%TH: " + diem5.getElementsByTag("span").get(6).text() + "\n" +
                            "%BT: " + diem5.getElementsByTag("span").get(7).text() + "\n" +
                            "%Thi: " + diem5.getElementsByTag("span").get(8).text() + "\n" +
                            "Điểm CC: " + diem5.getElementsByTag("span").get(9).text() + "\n" +
                            "Điểm KT: " + diem5.getElementsByTag("span").get(10).text() + "\n" +
                            "Điểm TH: " + diem5.getElementsByTag("span").get(12).text() + "\n" +
                            "Điểm BT: " + diem5.getElementsByTag("span").get(13).text() + "\n" +
                            "Điểm Thi: " + diem5.getElementsByTag("span").get(15).text() + "\n" +
                            "TK(10): " + diem5.getElementsByTag("span").get(16).text() + "\n";

            Element diem6 = doc.select("#ctl00_ContentPlaceHolder1_ctl00_div1 > table > tbody > tr:nth-child(83)").first();
            theFinalDiem += "\nDiem cua mon " + diem6.getElementsByTag("span").get(2).text() + "\n" +
                            "STT: " + diem6.getElementsByTag("span").get(0).text() + "\n" +
                            "Mã Môn: " + diem6.getElementsByTag("span").get(1).text() + "\n" +
                            "Tên Môn: " + diem6.getElementsByTag("span").get(2).text() + "\n" +
                            "TC: " + diem6.getElementsByTag("span").get(3).text() + "\n" +
                            "%CC: " + diem6.getElementsByTag("span").get(4).text() + "\n" +
                            "%KT: " + diem6.getElementsByTag("span").get(5).text() + "\n" +
                            "%TH: " + diem6.getElementsByTag("span").get(6).text() + "\n" +
                            "%BT: " + diem6.getElementsByTag("span").get(7).text() + "\n" +
                            "%Thi: " + diem6.getElementsByTag("span").get(8).text() + "\n" +
                            "Điểm CC: " + diem6.getElementsByTag("span").get(9).text() + "\n" +
                            "Điểm KT: " + diem6.getElementsByTag("span").get(10).text() + "\n" +
                            "Điểm TH: " + diem6.getElementsByTag("span").get(12).text() + "\n" +
                            "Điểm BT: " + diem6.getElementsByTag("span").get(13).text() + "\n" +
                            "Điểm Thi: " + diem6.getElementsByTag("span").get(15).text() + "\n" +
                            "TK(10): " + diem6.getElementsByTag("span").get(16).text() + "\n";
            
            System.out.println(theFinalDiem);
            FileUtils.writeStringToFile(new File("Diem.txt"), theFinalDiem, "UTF-8");
        } else System.out.println("Error!!! ko lay dc source HTML chua diem");
    }
}
