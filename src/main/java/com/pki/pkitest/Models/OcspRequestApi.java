package com.pki.pkitest.Models;

public class OcspRequestApi {
    public String request;

    public OcspRequestApi(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
