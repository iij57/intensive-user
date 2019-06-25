package com.sk.svdonation.dto;

import org.springframework.http.HttpStatus;

/**
 * 클라이언트에게 보내는 결과 객체
 */
public class ResponseDTO {
    public static enum ResultCode {
        OK("작업에 성공했습니다."), FAIL("작업에 실패했습니다.");

        private String msg;

        ResultCode(String msg) {
            this.msg = msg;
        }

        /**
         * @return the msg
         */
        public String getMsg() {
            return msg;
        }
    };
    
    /**
     * 결과 코드
     */
    private final ResultCode result;
    /**
     * 결과 코드
     */
    private final String responseCode;
    /**
     * 결과 메시지
     */
    private final String responseMessage;
    /**
     * 결과 객체
     */
    private final Object data;

    private ResponseDTO(ResultCode result, String responseCode, String resultMsg, Object data) {
        this.result = result;
        this.responseCode = responseCode;
        this.responseMessage = resultMsg;
        this.data = data;
    }
    
    /**
     * @return the result
     */
    public ResultCode getResult() {
        return result;
    }

    /**
     * @return the responseCode
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * @return the responseMessage
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

    public static class Builder {
        private ResultCode result = ResultCode.OK;
        private String responseCode = "200";
        private String responseMessage = HttpStatus.OK.name();
        private Object data = null;

        public Builder result(ResultCode result) {
            this.result = result;
            return this;
        }

        public Builder responseCode(String responseCode) {
            this.responseCode = responseCode;
            return this;
        }

        public Builder responseMessage(String responseMessage) {
            this.responseMessage = responseMessage;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public ResponseDTO build() {
            return new ResponseDTO(result, responseCode,  "".equals(responseMessage) ? result.getMsg() : responseMessage, data);
        }
    }
}