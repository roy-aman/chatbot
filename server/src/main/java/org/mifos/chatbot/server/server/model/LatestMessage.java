package org.mifos.chatbot.server.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mifos.chatbot.server.model.Entity;
import org.mifos.chatbot.server.model.Intent;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LatestMessage {
    Entity entity;
    org.mifos.chatbot.server.model.Intent intent;
    List<Intent> intentRanking;
    String text;
}
