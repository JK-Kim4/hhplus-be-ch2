package kr.hhplus.be.server.interfaces.adaptor;

import org.springframework.stereotype.Component;

@Component
public class DataPlatformSender implements RestTemplateAdaptor {
    @Override
    public <T> T post(String url, Object request, Class<T> responseType) {
        System.out.println("hello");
        return null;
    }
}
