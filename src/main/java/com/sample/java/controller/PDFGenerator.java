package com.sample.java.controller;

import com.sample.java.wrapper.HttpClient;
import com.sample.java.wrapper.HttpWrapper;
import com.sample.java.handler.PDFHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class PDFGenerator {
    @Autowired
    HttpWrapper httpWrapper;

    @Autowired
    PDFHandler pdfHandler;

    @Autowired
    HttpClient httpClient;

    @RequestMapping(value = "/download", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> downloadPDFFile() {
        // To fetch from the input
        String jsonInputString = "{\"pickPackageId\":\"50177256\",\"totalQuantity\":\"5\",\"totalWeight\":\"500\"}";
        String host = "http://localhost:8084/book-shipment/shipping-label/_get?";

        String base64String = httpClient.sendRequest(host,jsonInputString);

        byte[] decoded = pdfHandler.generateAndSavePDFFile(base64String);

        HttpHeaders headers = httpWrapper.setHttpHeaders(ApiType.PDF_GENERATOR);

        return ResponseEntity.ok().headers(headers).contentLength(decoded.length).body(decoded);
    }
}
