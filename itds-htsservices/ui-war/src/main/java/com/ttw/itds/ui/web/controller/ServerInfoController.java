package com.ttw.itds.ui.web.controller;

import com.ttw.itds.ui.service.impl.ServiceResponse;
import com.ttw.itds.ui.shared.codec.Base64Codec;
import com.ttw.itds.ui.shared.codec.JsonCodec;
import com.ttw.itds.ui.shared.dto.ServerInfoDto;
import com.ttw.itds.ui.shared.dto.examlog.ExamProductSample;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/properties")
public class ServerInfoController extends BaseItdsController {
    private final Logger log = Logger.getLogger(ServerInfoController.class);

    @Autowired
    private ServerInfoDto serverInfo;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ServerInfoDto> getProperties() {
        log.debug("REST call to getProperties");
        ServiceResponse<ServerInfoDto> response = new ServiceResponse<>();
        response.setValue(serverInfo);
        return response.getHttpResponseEntity();
    }
    
	@RequestMapping(value="ifs", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getIfsParameters(HttpServletRequest request, HttpServletResponse response) {

		log.info("service all: getIfsParameters(). ");

		Map<String, String> infoMap = new LinkedHashMap<String, String>();
		
		String ifsViewSampleUrl = serverInfo.getIfsViewSampleUrl();
		String ifsCreateSampleUrl = serverInfo.getIfsCreateSampleUrl();
		
		log.info("ifs_view_sample_url (from property)=" + ifsViewSampleUrl);
		log.info("ifs_create_sample_url (from property)="+ifsCreateSampleUrl);
		infoMap.put("ifs_view_sample_url",ifsViewSampleUrl);
		infoMap.put("ifs_create_sample_url", ifsCreateSampleUrl);

		String ifsViewSampleUrlBase64 = Base64Codec.encode(serverInfo.getIfsViewSampleUrl());
		String ifsCreateSampleUrlBase64 = Base64Codec.encode(serverInfo.getIfsCreateSampleUrl());
		log.info("ifs_view_sample_url_base64 (Base64 encoded)=" + ifsViewSampleUrlBase64);
		log.info("ifs_create_sample_url_base64 (Base64 encoded)="+ifsCreateSampleUrlBase64);
		
		infoMap.put("ifs_view_sample_url_base64",ifsViewSampleUrlBase64);
		infoMap.put("ifs_create_sample_url_base64", ifsCreateSampleUrlBase64);
		
		String jsonPayload = JsonCodec.marshal(infoMap);
		log.info("parameters are:  " + jsonPayload);
		return jsonPayload;
	}
    
}
