package com.tedis.protocol;

public class Result {
    private int type;
    private String errorType;  // not null only when type == RESPData.ERROR_TYPE
    private String result;

    public Result(int type, String result) {
        this.type = type;
        this.result = result;
    }

    public Result(int type, String errorType, String result) {
        this.type = type;
        this.errorType = errorType;
        this.result = result;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    @Override
    public String toString() {
        return "Result{" +
                "type=" + type +
                ", errorType='" + errorType + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
