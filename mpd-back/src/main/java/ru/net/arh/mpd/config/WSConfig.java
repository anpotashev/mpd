package ru.net.arh.mpd.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import ru.net.arh.mpd.web.LogginHandlerDecorator;

@Configuration
@EnableWebSocketMessageBroker
@EnableAutoConfiguration
@EnableCaching
@EnableScheduling
public class WSConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/mpd");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp").setAllowedOrigins("*").withSockJS().setClientLibraryUrl("/webjars/sockjs-client/1.1.2/sockjs.js");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        //todo поиграться с максимальным размером
//        registry.setMessageSizeLimit(200000); // default : 64 * 1024
//        registry.setSendTimeLimit(20 * 10000); // default : 10 * 10000
//        registry.setSendBufferSizeLimit(3* 512 * 1024); // default : 512 * 1024
        registry.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(WebSocketHandler handler) {
                return new LogginHandlerDecorator(handler);
            }
        });
    }
}
