package com.wby.weather.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class RestResponse {

    private static final NullObj NULL_OBJ = new NullObj();

    @ApiModelProperty("ok?")
    private boolean ok = true;
    @ApiModelProperty("response code")
    private int code = 0;
    @ApiModelProperty("response msg")
    private String msg;
    @ApiModelProperty("object data")
    private Object data = NULL_OBJ;

    @Data
    public static class NullObj implements Serializable {
        private String info = "please ignore the business data.";
    }

    @Data
    public static class Fail extends RestResponse {

        public Fail(boolean ok, int code, String message) {
            super();
            this.setOk(ok);
            this.setCode(code);
            this.setMsg(message);
        }
    }

    @Data
    @NoArgsConstructor
    public static class Ok extends RestResponse {

        public Ok(String message) {
            super();
            this.setMsg(message);
        }

        public Ok(String message, Object obj) {
            this(message);
            this.setData(obj);
        }
    }
}
