package com.noetic.ucip_billing.handlers;

//import com.noetic.ucip_billing.entities.UcipChargingEntity;
//import com.noetic.ucip_billing.repositories.UcipChargingRepository;
import com.noetic.ucip_billing.services.PostPaidOrPrePaidCheckService;
import com.noetic.ucip_billing.services.UCIPChargingService;
import com.noetic.ucip_billing.services.zongmml.ZongMMLRequest;
import com.noetic.ucip_billing.utils.AppResponse;
import com.noetic.ucip_billing.utils.Constants;
import com.noetic.ucip_billing.utils.ChargeRequestDTO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

//import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Service
public class ChargingRequestHandler {
    Logger log = LoggerFactory.getLogger(ChargingRequestHandler.class);

    private UCIPChargingService ucipChargingService = null;


    PostPaidOrPrePaidCheckService postPaidOrPrePaidCheckService = null;


    //must uncomment while pushing it on production
//    public AppResponse processReqeust(ChargeRequestDTO chargeRequestDTO) throws InterruptedException, ExecutionException, JSONException, IOException {
//
//        //Mobilink Request
//        if (chargeRequestDTO.getOperatorId() == 10 || chargeRequestDTO.getOperatorId() == 40) {
//            postPaidOrPrePaidCheckService = new PostPaidOrPrePaidCheckService(chargeRequestDTO.getMsisdn(), chargeRequestDTO.getTransactionId());
//            if (postPaidOrPrePaidCheckService.isPostPaid()) {
//                return postPaidResponse();
//            } else {
//                if (chargeRequestDTO.getMsisdn().equals("3222200051") || chargeRequestDTO.getMsisdn().equals("03222200051") || chargeRequestDTO.getMsisdn().equals("923222200051")) {
//                    chargeRequestDTO.setAmount("1");
//                }
//                ucipChargingService = new UCIPChargingService(chargeRequestDTO.getMsisdn(),
//                        Double.parseDouble(chargeRequestDTO.getAmount()), chargeRequestDTO.getTransactionId());
//                HttpResponse httpResponse = ucipChargingService.sendRequest(false);
////                return parseUcipResponse(httpResponse, chargeRequestDTO);
//
//                return parseUcipResponse(httpResponse, chargeRequestDTO);
//            }
//        } else if (chargeRequestDTO.getOperatorId() == 50) {
//            AppResponse appResponse = sendZongMMRequest(chargeRequestDTO);
//            return appResponse;
//        } else {
//            return null;
//        }
//    }



    public AppResponse processReqeust(ChargeRequestDTO chargeRequestDTO) throws InterruptedException, ExecutionException, JSONException, IOException {

        //Mobilink Request
        if (chargeRequestDTO.getOperatorId() == 10 || chargeRequestDTO.getOperatorId() == 40) {
            postPaidOrPrePaidCheckService = new PostPaidOrPrePaidCheckService(chargeRequestDTO.getMsisdn(), chargeRequestDTO.getTransactionId());
            System.out.println("line 78");
            if (postPaidOrPrePaidCheckService.isPostPaid()) {

                return postPaidResponse();
            }
                //changes here
            } else if (chargeRequestDTO.getOperatorId() == 20 || chargeRequestDTO.getOperatorId() == 30){
                System.out.println("line 84");
                if (chargeRequestDTO.getMsisdn().equals("3222200051") || chargeRequestDTO.getMsisdn().equals("03222200051") || chargeRequestDTO.getMsisdn().equals("923222200051")) {
                    chargeRequestDTO.setAmount("1");
                }
            System.out.println("line 89");
                ucipChargingService = new UCIPChargingService(chargeRequestDTO.getMsisdn(),
                        Double.parseDouble(chargeRequestDTO.getAmount()), chargeRequestDTO.getTransactionId());
            System.out.println("line 92 charging handler");
                HttpResponse httpResponse = ucipChargingService.sendRequest(false);
//                return parseUcipResponse(httpResponse, chargeRequestDTO);
            System.out.println("line 94 in charging handler");

                return parseUcipResponse(httpResponse, chargeRequestDTO);

            }
         else if (chargeRequestDTO.getOperatorId() == 50) {
            System.out.println("line 98");
            AppResponse appResponse = sendZongMMRequest(chargeRequestDTO);
            return appResponse;
        } else {
            System.out.println("line 101");
            return null;
        }
        return null;
    }

    //must uncomment while pushing it on production
//    private AppResponse parseUcipResponse(HttpResponse httpResponse,ChargeRequestDTO chargeRequestDTO) throws IOException {
//
//        HttpEntity entity = httpResponse.getEntity();
//        String xmlResponse = EntityUtils.toString(entity);
//        String[] recArray = new String[2];
//        recArray = xmlConversion(xmlResponse);
//
//        int responseCode = -1;
//        String transID = recArray[0]; // TransactionID
//        EntityUtils.consume(entity);
//        if (recArray[1] != null) {
//            responseCode = Integer.valueOf(recArray[1]); // ResponseCode
//        }
//        log.info("jazz response:" +responseCode);
//
//        //saveChargingRecord(responseCode,chargeRequestDTO);
//        if(responseCode==0){
//            return createChargingResponse(Constants.CHARGED_SUCCESSFUL,Constants.CHARGED_SUCCESSFUL_MSG,transID);
//        }else {
//            return createChargingResponse(Constants.INSUFFICIENT_BALANCE,Constants.INSUFFICIENT_BALANCE_MSG,transID);
//        }
//    }


    //for testing
    private AppResponse parseUcipResponse(HttpResponse httpResponse, ChargeRequestDTO chargeRequestDTO) throws IOException {

        System.out.println("line 139 parseucipresponxe");

        //must be uncomment while pushing it on production
//        HttpEntity entity = httpResponse.getEntity();
//        String xmlResponse = EntityUtils.toString(entity);
//        System.out.println("line 142");
//        String[] recArray = new String[2];
//        recArray = xmlConversion(xmlResponse);

        System.out.println("line 146");
        Scanner scanner= new Scanner(System.in);
        System.out.println("press 0 for charging and 1 for not charging");


        if (scanner.nextInt()==0){
            int responseCode = 0;
            String transID = "1"; // TransactionID
//        EntityUtils.consume(entity);
//        if (recArray[1] != null) {
//            responseCode = Integer.valueOf(recArray[1]); // ResponseCode
//        }
            log.info("jazz response:" + responseCode);
            return createChargingResponse(Constants.CHARGED_SUCCESSFUL,Constants.CHARGED_SUCCESSFUL_MSG,transID);
        }
        else if (scanner.nextInt()==1){
            int responseCode = 1;
            String transID = "2"; // TransactionID
//        EntityUtils.consume(entity);
//        if (recArray[1] != null) {
//            responseCode = Integer.valueOf(recArray[1]); // ResponseCode
//        }
            log.info("jazz response:" + responseCode);
            return createChargingResponse(Constants.INSUFFICIENT_BALANCE,Constants.INSUFFICIENT_BALANCE_MSG,transID);
        }



//      int responseCode = 0;
//        String transID = "1"; // TransactionID
////        EntityUtils.consume(entity);
////        if (recArray[1] != null) {
////            responseCode = Integer.valueOf(recArray[1]); // ResponseCode
////        }
//        log.info("jazz response:" + responseCode);

        //saveChargingRecord(responseCode,chargeRequestDTO);
        System.out.println("line 157");
//        if (responseCode == 0) {
//
//            System.out.println("res===0");
//
//            //must be uncomment while pushing it on production
//           return createChargingResponse(Constants.CHARGED_SUCCESSFUL,Constants.CHARGED_SUCCESSFUL_MSG,transID);
//
//            //for testing
//           // return "charged successfull";
//        } else {
//            System.out.println("resp==124");
//            return createChargingResponse(Constants.INSUFFICIENT_BALANCE,Constants.INSUFFICIENT_BALANCE_MSG,transID);
//        }
        return null;
    }

    private AppResponse sendZongMMRequest(ChargeRequestDTO chargeRequestDTO) {
        String number = "";
        String serviceId = "";

        ZongMMLRequest zongMMLRequest = new ZongMMLRequest();
        if (chargeRequestDTO.getMsisdn().startsWith("92")) {
            number = chargeRequestDTO.getMsisdn();
        } else if (chargeRequestDTO.getMsisdn().startsWith("03")) {
            number = chargeRequestDTO.getMsisdn().replaceFirst("03", "92");
        } else if (chargeRequestDTO.getMsisdn().startsWith("3")) {
            number = "92" + chargeRequestDTO.getMsisdn();
        }
        System.out.println("number = " + number);
        System.out.println("Amount " + chargeRequestDTO.getAmount());
        Integer adjustmentAmountRelative = 0;
        Integer amount = Integer.valueOf(chargeRequestDTO.getAmount());

        if (amount == 239) {
            adjustmentAmountRelative = 200;
            serviceId = "Noet01";
        } else if (amount == 598) {
            adjustmentAmountRelative = 500;
            serviceId = "Noet05";
        } else if (amount == 1159) {
            adjustmentAmountRelative = 1000;
            serviceId = "Noet10";
        } else if (amount == 2988) {
            adjustmentAmountRelative = 2500;
            serviceId = "Noet25";
        }

        System.out.println("final Amount " + adjustmentAmountRelative);
        zongMMLRequest.logIn();
        String mmlResponse = zongMMLRequest.deductBalance(number, String.valueOf(adjustmentAmountRelative), serviceId);
        AppResponse appResponse = parseMMLResponse(mmlResponse);
        //saveChargingRecord(appResponse.getCode(),chargeRequestDTO);
        return appResponse;
    }

    private AppResponse parseMMLResponse(String mmlResponse) {
        AppResponse appResponse = new AppResponse();
        String[] res = mmlResponse.split("RETN=");
        String[] codeArr = res[1].split(",");
        String code = codeArr[0];
        if (code.equalsIgnoreCase("0000")) {
            appResponse.setCode(Constants.CHARGED_SUCCESSFUL);
            appResponse.setMsg(Constants.CHARGED_SUCCESSFUL_MSG);
            appResponse.setTransID("transId");
        } else {
            appResponse.setCode(Constants.INSUFFICIENT_BALANCE);
            appResponse.setMsg(Constants.INSUFFICIENT_BALANCE_MSG);
            appResponse.setTransID("transId");
        }
        return appResponse;
    }

//    private void saveChargingRecord(int ucipResponse,ChargeRequestDTO chargeRequestDTO){
//        UcipChargingEntity ucipChargingEntity = new UcipChargingEntity();
//        ucipChargingEntity.setAmount(chargeRequestDTO.getAmount());
//        ucipChargingEntity.setCdate(Timestamp.valueOf(LocalDateTime.now()));
//        ucipChargingEntity.setIsPostpaid(1);
//        ucipChargingEntity.setMsisdn(chargeRequestDTO.getMsisdn());
//        ucipChargingEntity.setUcipResponse(ucipResponse);
//        if(ucipResponse==0){
//            ucipChargingEntity.setIsCharged(1);
//        }else {
//            ucipChargingEntity.setIsCharged(0);
//        }
//        ucipChargingRepository.save(ucipChargingEntity);
//    }

    protected String[] xmlConversion(String xml) {
        String[] retArray = new String[2];
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(xml));

            Document doc = docBuilder.parse(src);

            // normalize text representation
            doc.getDocumentElement().normalize();

            NodeList listOfPersons = doc.getElementsByTagName("member");

            System.out.println(listOfPersons.getLength());

            if (listOfPersons.getLength() == 2) {

                Node firstPersonNode11 = listOfPersons.item(0);

                if (firstPersonNode11.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) firstPersonNode11;

                    retArray[0] = eElement.getElementsByTagName("value").item(0).getTextContent();
                }    //end of if clause

                // Return Response Code
                Node firstPersonNode22 = listOfPersons.item(1);
                if (firstPersonNode22.getNodeType() == Node.ELEMENT_NODE) {

                    Element firstPersonElement22 = (Element) firstPersonNode22;

                    retArray[1] = firstPersonElement22.getElementsByTagName("value").item(0).getTextContent();
                } //end of if clause

            } else {
                //Return Transaction ID
                Node firstPersonNode = listOfPersons.item(16);
                if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element firstPersonElement = (Element) firstPersonNode;

                    //-------
                    NodeList lastNameList = firstPersonElement.getElementsByTagName("string");
                    Element lastNameElement = (Element) lastNameList.item(0);

                    NodeList textLNList = lastNameElement.getChildNodes();
                    System.out.println("Para 1 Value : " + ((Node) textLNList.item(0)).getNodeValue().trim());
                    retArray[0] = ((Node) textLNList.item(0)).getNodeValue().trim();

                } //End Transaction IF
                //Return Response Code
                Node firstPersonNode1 = listOfPersons.item(17);
                if (firstPersonNode1.getNodeType() == Node.ELEMENT_NODE) {

                    Element firstPersonElement1 = (Element) firstPersonNode1;

                    NodeList lastNameList = firstPersonElement1.getElementsByTagName("i4");
                    Element lastNameElement = (Element) lastNameList.item(0);

                    NodeList textLNList = lastNameElement.getChildNodes();
                    retArray[1] = ((Node) textLNList.item(0)).getNodeValue().trim();

                } //End Response Code IF
            }
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());

        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return retArray;

    }

//must be uncomment while pushing it on production
////    private AppResponse createChargingResponse(int code,String msg,String transID){
////        AppResponse appResponse = new AppResponse();
////        appResponse.setCode(code);
////        appResponse.setMsg(msg);
////        appResponse.setTransID(transID);
////        return appResponse;
//    }

    //for testing
    private AppResponse createChargingResponse(int code, String msg, String transID) {
        AppResponse appResponse = new AppResponse();
        appResponse.setCode(code);
        appResponse.setMsg(msg);
        appResponse.setTransID(transID);
        return appResponse;
    }

        private AppResponse postPaidResponse() {
            System.out.println("in postpaid 372");
            AppResponse appResponse = new AppResponse();
            appResponse.setCode(Constants.IS_POSTPAID);
            appResponse.setMsg(Constants.IS_POSTPAID_MSG);
            appResponse.setTransID("trans_id");
            return appResponse;
        }
    }

