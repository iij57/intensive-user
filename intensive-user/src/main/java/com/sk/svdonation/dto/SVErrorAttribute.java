package com.sk.svdonation.dto;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@Component
public class SVErrorAttribute extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
		Map<String, Object> errorAttributes = new LinkedHashMap<>();
		errorAttributes.put("timestamp", new Date());
        errorAttributes.put("result", ResponseDTO.ResultCode.FAIL);
		Integer status = (Integer) webRequest.getAttribute("javax.servlet.error.status_code", RequestAttributes.SCOPE_REQUEST);
		if (status == null) {
			errorAttributes.put("responseCode", 999);
			errorAttributes.put("responseMessage", "None");
		}else{
			errorAttributes.put("responseCode", status);
			try {
				errorAttributes.put("responseMessage", HttpStatus.valueOf(status).getReasonPhrase());
			}
			catch (Exception ex) {
				// Unable to obtain a reason
				errorAttributes.put("responseMessage", "Http Status " + status);
			}
		}

		return errorAttributes;
    }
}