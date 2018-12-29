package com.kam6512.CRN_BOT;

import com.kam6512.CRN_BOT.api.HomeTaxApi;
import com.kam6512.CRN_BOT.api.HomeTaxService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrnBotApplication.class)
@EnableAutoConfiguration
public class HomeTaxTests {


    @Test
    public void HomeTaxTest() {
        HomeTaxService homeTaxService = HomeTaxApi.getInstance().getHomeTaxService();
        String rawData = "<map id=\"ATTABZAA001R08\"><pubcUserNo/><mobYn>N</mobYn><inqrTrgtClCd>1</inqrTrgtClCd><txprDscmNo>3051577349</txprDscmNo><dongCode>15</dongCode><psbSearch>Y</psbSearch><map id=\"userReqInfoVO\"/></map>";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml"), rawData);
        homeTaxService.getResult(requestBody)
                .subscribe(responseBody -> log(responseBody.string()),
                        throwable -> log(throwable.toString())).dispose();
    }


    XPath xpath = XPathFactory.newInstance().newXPath();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


    @Test
    public void HomeTaxTestWithRegex() {
        HomeTaxService homeTaxService = HomeTaxApi.getInstance().getHomeTaxService();
        String rawData = "<map id=\"ATTABZAA001R08\"><pubcUserNo/><mobYn>N</mobYn><inqrTrgtClCd>1</inqrTrgtClCd><txprDscmNo>3051577349</txprDscmNo><dongCode>15</dongCode><psbSearch>Y</psbSearch><map id=\"userReqInfoVO\"/></map>";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml"), rawData);
        homeTaxService.getResult(requestBody)
                .map(ResponseBody::string)
                .map(StringReader::new)
                .map(InputSource::new)
                .map(inputSource -> factory.newDocumentBuilder().parse(inputSource))
                .map(document -> xpath.evaluate("//trtCntn", document, XPathConstants.STRING))
                .subscribe(this::log).dispose();
    }

    private void log(Object o) {
        try {
            System.out.println(o.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}