package com.fw.core.config.feign;

import java.io.IOException;
import java.io.InputStream;

import com.fw.model.integration.ILExceptionT24;
import com.fw.model.integration.ILResponseErrorT24;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;

public class IlT24CustomErrorDecoder implements ErrorDecoder {
	@Autowired
	private ObjectMapper om;

	@Override
	public Exception decode(String methodKey, Response response) {
		try (InputStream bodyIs = response.body().asInputStream()) {
			ILResponseErrorT24 message = om.readValue(bodyIs, ILResponseErrorT24.class);
			return new ILExceptionT24(message.getError(), message.getStatus());
		} catch (IOException e) {
			return new Exception(e.getMessage());
		}
	}
}
