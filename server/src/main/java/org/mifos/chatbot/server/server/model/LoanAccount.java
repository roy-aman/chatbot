package org.mifos.chatbot.server.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mifos.chatbot.server.model.LoanType;
import org.mifos.chatbot.server.model.Status;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanAccount {
    private int id;
    private String accountNo;
    private String externalId;
    private int productId;
    private String productName;
    private String shortProductName;
    private Status status;
    private LoanType loanType;
    private int loanCycle;
    private boolean inArrears;
    private double originalLoan;
    private double loanBalance;
    private double amountPaid;
}
