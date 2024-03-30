package org.mifos.chatbot.server.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mifos.chatbot.server.model.LoanAccount;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanAccounts {
    private List<LoanAccount> loanAccounts;
    private List<Object> groupLoanIndividualMonitoringAccounts;
    private List<Object> guarantorAccounts;
}
