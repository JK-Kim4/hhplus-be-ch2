package kr.hhplus.be.server.interfaces.adaptor;

import org.springframework.stereotype.Component;


public interface RestTemplateAdaptor {

    <T> T post(String url, Object request, Class<T> responseType);

}
