package org.mifos.chatbot.server.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.mifos.chatbot.server.model.Intent;
import org.mifos.chatbot.server.model.Textmodel;
import org.mifos.chatbot.server.model.Tracker;
import org.mifos.chatbot.server.service.GetLoanServiceImpl;
import org.mifos.chatbot.server.service.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ChatServiceImpl {
    private static final String LOAN_STATUS_OF_CLIENT = "loan_status_of_client";
    private static final String LOGIN_CREDENTIALS = "login_credentials";
    private static final String LOAN_STATUS = "loan_status";
    private static final String DISBURSEMENT_AMOUNT = "disbursement_amount";
    private static final String CLIENT_COUNT = "get_client_count";
    private static final String MATURITY_DATE = "maturity_date";
    private static final String NEXT_DUE_DATE = "next_due_date";
    private static final String NEXT_DUE_PRINCIPAL = "next_due_principal";
    private static final String APPROVED_PRINCIPAL = "approved_principal";
    private static final String INTEREST_RATE = "interest_rate";
    private static final String PREVIOUS_PAYMENT_DATE = "previous_payment_date";
    private static final String PREVIOUS_PAYMENT_AMOUNT = "previous_payment_amount";
    private static final String PREVIOUS_PAYMENT_INTEREST = "previous_payment_interest";
    private static final String ARREAR_DAY = "arrear_day";
    private static final String LOAN_DISBURSED_DATE = "loan_disbursed_date";
    private static final String LOAN_APPROVED_DATE = "loan_approved_date";
    private static final String FIRST_REPAYMENT_DATE = "first_repayment_date";
    private static final String CLIENT_ACTIVATION_DATE = "client_activation_date";
    org.mifos.chatbot.server.service.Helper helper = new Helper();
    @Value("${RASA_SERVER}")
    private String RASA_SERVER;
    @Value("${WEBHOOK}")
    private String WEBHOOK;
    @Autowired
    private GetLoanServiceImpl loanService;

    public List<Textmodel> processUserUtterance(Response botResponse, String conversationId, HttpServletRequest request) throws IOException {
        log.info("hello this is from the processuserutterance method" + botResponse);
        String res = botResponse.body().string();
        log.info("Response in processUserUtterance" + res);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(res);
        if (jsonNode.size() == 0) {
            return new ArrayList<>();
        }
        Tracker tracker = retriveConversationTracker(jsonNode.get(0).get("recipient_id").textValue());
        Intent intent = findIntent(tracker);
        log.info("intent loggin " + intent.toString());
        return classifyIntent(jsonNode, intent, tracker, request);
    }

    public Response getResponse(String message) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        System.out.println("JSON Payload: " + helper.createJSONRequest(message));
        log.info("request body in rasa service " + message);
        RequestBody body = RequestBody.create(mediaType, (message));
        Request request = new Request.Builder()
                .url("http://localhost:5005/webhooks/rest/webhook")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            //TODO: handle exception
        }
        log.info("ahasas" + response.message());
        return response;
    }

    public List<Textmodel> classifyIntent(JsonNode jsonNode, Intent intent, Tracker tracker, HttpServletRequest request) throws IOException {
        String botResponse = "";
        List<Textmodel> list = new ArrayList<>();
//        String res = botResponse1.body().string();
//        log.info(res);
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonNode = mapper.readTree(res);
        log.info("hello this is " + jsonNode.toString());
//        log.info(jsonNode.get("text").toString());
        // Extract the recipient_id fi
        String intentName = intent.getName();
        String text = gettext11(intentName, botResponse, tracker, request);
        if (text.equals("Intent not found")) {
            for (int i = 0; i < jsonNode.size(); i++) {
                list.add(new Textmodel(jsonNode.get(i).get("recipient_id").textValue(), jsonNode.get(i).get("text").textValue()));
            }
            return list;
        } else {
            list.add(new Textmodel(jsonNode.get(0).get("recipient_id").textValue(), text));
            return list;
        }
    }

    public String gettext11(String intentName, String botResponse, Tracker tracker, HttpServletRequest request) {
        if (intentName.equals(LOGIN_CREDENTIALS)) {
            loanService.authorization(tracker.getLatestMessage().getText());
        }
        if (intentName.equals(LOAN_STATUS)) {
//            textmodel.setRecipientId(jsonNode.get("recipient_id").asText());
            return loanService.getLoanStatus(botResponse, tracker, request);
//            return textmodel;
        }
        if (intentName.equals(LOAN_STATUS_OF_CLIENT)) {
            return loanService.getLoanStatus(botResponse, tracker, request);
        } else if (intentName.equals(APPROVED_PRINCIPAL)) {
            return loanService.getApprovedPrincipalAmount(botResponse, request).toString();
        } else if (intentName.equals(INTEREST_RATE)) {
            return loanService.getInterestRate(botResponse, request).toString();
        } else if (intentName.equals(MATURITY_DATE)) {
            return loanService.getMaturityDate(botResponse, request);
        } else if (intentName.equals(NEXT_DUE_DATE)) {
            return loanService.getNextDueDate(botResponse, request);
        } else if (intentName.equals(NEXT_DUE_PRINCIPAL)) {
            return loanService.getNextDuePrincipal(botResponse, request);
        } else if (intentName.equals(PREVIOUS_PAYMENT_DATE)) {
            return loanService.getPreviousPaymentDate(botResponse, request);
        } else if (intentName.equals(PREVIOUS_PAYMENT_AMOUNT)) {
            return loanService.getPreviousPaymentAmount(botResponse, request);
        } else if (intentName.equals(PREVIOUS_PAYMENT_INTEREST)) {
            return loanService.getPreviousPaymentInterest(botResponse, request);
        } else if (intentName.equals(ARREAR_DAY)) {
            return loanService.getArrearDays(botResponse, request);
        } else if (intentName.equals(CLIENT_COUNT)) {
            return loanService.getClientCount(botResponse, request);
        } else if (intentName.equals(LOAN_DISBURSED_DATE)) {
            return loanService.getLoanDisbursedDate(botResponse, request);
        } else if (intentName.equals(LOAN_APPROVED_DATE)) {
            return loanService.getLoanApprovedDate(botResponse, request);
        } else if (intentName.equals(FIRST_REPAYMENT_DATE)) {
            return loanService.getFirstRepaymentDate(botResponse, request);
        } else if (intentName.equals(CLIENT_ACTIVATION_DATE)) {
            return loanService.getClientActivationDate(botResponse, request);
        } else if (intentName.equals(DISBURSEMENT_AMOUNT)) {
            return loanService.getDisbursementAmount(botResponse, request);
        } else {
            return "Intent not found";
        }
    }

    public Tracker retriveConversationTracker(String conversationId) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        log.info("hellodcsa");

        Request request = new Request.Builder()
                .url(RASA_SERVER + "/conversations/" + conversationId + "/tracker")
                .header("Connection", "close")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
//                    log.info("res body "+response.body().string());
        if (response.code() != 200) {
            //TODO add logging
        }
        JsonObject obj = helper.createJSON(response.body().string());
//        log.info("Json obj check "+ obj.toString());
        Tracker tracker = helper.createTrackerPOJO(obj);
        return tracker;
    }

    public Intent findIntent(Tracker tracker) {
        return tracker.getLatestMessage().getIntent();
    }
}
