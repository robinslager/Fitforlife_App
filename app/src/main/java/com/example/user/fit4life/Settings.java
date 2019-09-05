package com.example.user.fit4life;

public class Settings {
//     here all changeable vars in 1 document.
//     everything that can be changed should be here.

    /*
    The Url To server Directory Where all the android files are / serversided urls
     */
    private String BaseUrl = "https://fitforlife-dev.nl/";
    private String backendUrl = "backend/android/";
    private String DownloadUrl = "Download_content/";
    // TODO button hidden area from normal user to update table version








    public String getBaseServerUrl() {
        return BaseUrl + backendUrl;
    }
    public String getDownloadDir() {
        return BaseUrl + DownloadUrl;
    }
}
