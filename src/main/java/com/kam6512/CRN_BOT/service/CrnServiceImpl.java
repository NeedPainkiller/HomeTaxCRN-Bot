package com.kam6512.CRN_BOT.service;

import com.kam6512.CRN_BOT.api.HomeTaxApi;
import com.kam6512.CRN_BOT.api.HomeTaxService;
import io.reactivex.Observable;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CrnServiceImpl implements CrnService {
    private final static String rawData = "<map id=\"ATTABZAA001R08\"><pubcUserNo/><mobYn>N</mobYn><inqrTrgtClCd>1</inqrTrgtClCd><txprDscmNo>CRN_HERE</txprDscmNo><dongCode>15</dongCode><psbSearch>Y</psbSearch><map id=\"userReqInfoVO\"/></map>";
    private final static String spliter = "[ ,/\\n]+";

    @Value("${slackBotToken}")
    private String slackToken;
    private final XPath xpath;
    private DocumentBuilder builder;
    private final HomeTaxService homeTaxService;
    private final MediaType mediaType;


    @Autowired
    public CrnServiceImpl() {
        homeTaxService = HomeTaxApi.getInstance().getHomeTaxService();
        xpath = XPathFactory.newInstance().newXPath();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log(e);
        }
        mediaType = MediaType.parse("application/xml");
    }

    public Observable<Message> onReceivedMessage(WebSocketSession session, Event event) {
        String source = event.getText();
        List<String> items = Arrays.asList(source.split(spliter));
        AtomicReference<String> currentNumber = new AtomicReference<>();
        return Observable.fromIterable(items)
                .doOnNext(currentNumber::set)
                .map(item -> rawData.replace("CRN_HERE", item))
                .map(raw -> RequestBody.create(mediaType, raw))
                .flatMap(homeTaxService::getResult)
                .map(ResponseBody::string)
                .map(StringReader::new)
                .map(InputSource::new)
                .map(inputSource -> builder.parse(inputSource))
                .map(document -> xpath.evaluate("//trtCntn", document, XPathConstants.STRING))
                .map(value -> currentNumber.get() + " : " + value.toString())
                .map(Message::new);
    }

    private void log(Object o) {
        try {
            System.out.println(o.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
