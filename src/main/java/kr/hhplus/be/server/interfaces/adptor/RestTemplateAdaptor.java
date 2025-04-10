package kr.hhplus.be.server.interfaces.adptor;

import org.springframework.stereotype.Component;

@Component
public interface RestTemplateAdaptor {

    <T> T post(String url, Object request, Class<T> responseType);

}
