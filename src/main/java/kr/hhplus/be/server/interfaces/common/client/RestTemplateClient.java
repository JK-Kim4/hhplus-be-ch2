package kr.hhplus.be.server.interfaces.common.client;


public interface RestTemplateClient {

    <T> T post(String url, Object request, Class<T> responseType);

}
