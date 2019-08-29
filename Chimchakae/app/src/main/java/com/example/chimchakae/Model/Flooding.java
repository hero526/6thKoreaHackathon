package com.example.chimchakae.Model;


// 실시간 내수침수위 정보 서비스 JSON Data
public class Flooding {

    private String siteCode;            // 지역코드번호
    private String siteName;            // 지역이름
    private int alertLevel3;            // Level3 값
    private String alertLevel3Nm;       // Level3 명칭
    private int alertLevel4;            // Level4 값
    private String alertLevel4Nm;       // Level4 명칭
    private int flud;                   // flud 값
    private String obsrTimel;           // 측정 시간
    private int sttus;                  // 상태 값
    private String sttusNm;             // 상태 명칭


    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public int getAlertLevel3() {
        return alertLevel3;
    }

    public void setAlertLevel3(int alertLevel3) {
        this.alertLevel3 = alertLevel3;
    }

    public String getAlertLevel3Nm() {
        return alertLevel3Nm;
    }

    public void setAlertLevel3Nm(String alertLevel3Nm) {
        this.alertLevel3Nm = alertLevel3Nm;
    }

    public int getAlertLevel4() {
        return alertLevel4;
    }

    public void setAlertLevel4(int alertLevel4) {
        this.alertLevel4 = alertLevel4;
    }

    public String getAlertLevel4Nm() {
        return alertLevel4Nm;
    }

    public void setAlertLevel4Nm(String alertLevel4Nm) {
        this.alertLevel4Nm = alertLevel4Nm;
    }

    public int getFlud() {
        return flud;
    }

    public void setFlud(int flud) {
        this.flud = flud;
    }

    public String getObsrTimel() {
        return obsrTimel;
    }

    public void setObsrTimel(String obsrTimel) {
        this.obsrTimel = obsrTimel;
    }

    public int getSttus() {
        return sttus;
    }

    public void setSttus(int sttus) {
        this.sttus = sttus;
    }

    public String getSttusNm() {
        return sttusNm;
    }

    public void setSttusNm(String sttusNm) {
        this.sttusNm = sttusNm;
    }



}
