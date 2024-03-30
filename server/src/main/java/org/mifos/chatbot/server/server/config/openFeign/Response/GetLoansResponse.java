package org.mifos.chatbot.server.server.config.openFeign.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mifos.chatbot.server.config.openFeign.Response.Delinquent;
import org.mifos.chatbot.server.config.openFeign.Response.DisbursementDetail;
import org.mifos.chatbot.server.config.openFeign.Response.RepaymentSchedule;
import org.mifos.chatbot.server.config.openFeign.Response.Timeline;
import org.mifos.chatbot.server.config.openFeign.Response.Transaction;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetLoansResponse {

    public List<Transaction> transactions;
    public Delinquent delinquent;
    private Integer annualInterestRate;
    private Integer approvedPrincipal;
    private List<DisbursementDetail> disbursementDetails;
    private Timeline timeline;
    private RepaymentSchedule repaymentSchedule;

}
