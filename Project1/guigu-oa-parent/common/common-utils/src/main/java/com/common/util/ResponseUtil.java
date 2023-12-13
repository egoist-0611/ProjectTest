package com.common.util;

import com.common.exception.UtilFunctionException;
import com.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 响应信息
 */
public class ResponseUtil {

    /**
     * 响应信息
     *
     * @param response    HttpServletResponse响应对象
     * @param responseMsg Result统一的结果集对象
     */
    public static void out(HttpServletResponse response, Result<Object> responseMsg) {
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            new ObjectMapper().writeValue(response.getWriter(), responseMsg);
        } catch (IOException e) {
            throw new UtilFunctionException("响应信息异常");
        }
    }
}
