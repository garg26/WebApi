package com.neostencil.responses;

import java.util.HashMap;

public class ReviewStatistics {

    int total;
    float avg;

    public float getAvg() {
        return avg;
    }

    public void setAvg(float avg) {
        this.avg = avg;
    }

    HashMap<String, Integer> response;

    public HashMap<String, Integer> getResponse() {
        return response;
    }

    public void setResponse(HashMap<String, Integer> response) {
        this.response = response;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
