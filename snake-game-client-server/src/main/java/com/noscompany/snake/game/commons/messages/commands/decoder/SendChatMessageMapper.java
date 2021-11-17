package com.noscompany.snake.game.commons.messages.commands.decoder;

import com.jayway.jsonpath.JsonPath;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;

@Slf4j
class SendChatMessageMapper {

    public Option<String> map(AtmosphereResource r, String msg) {
        try {
            return Option.of(chatMessageContent(msg));
        } catch (Exception e) {
            return Option.none();
        }
    }

    private String chatMessageContent(String msg) {
        return JsonPath
                .parse(msg)
                .read("$.messageContent");
    }
}