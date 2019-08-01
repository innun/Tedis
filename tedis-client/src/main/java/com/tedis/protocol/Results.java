package com.tedis.protocol;

import java.util.List;

public class Results {
    private List<Result> results;

    public Results(List<Result> results) {
        this.results = results;
    }

    public void addResult(Result result) {
        results.add(result);    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
