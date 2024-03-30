package org.mifos.chatbot.server.server.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mifos.chatbot.server.request.Config;
import org.mifos.chatbot.server.request.Slots;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Domain {
    Config config;
    List<String> entities;
    Slots slots;
    List<String> responses;
    List<String> actions;

}
