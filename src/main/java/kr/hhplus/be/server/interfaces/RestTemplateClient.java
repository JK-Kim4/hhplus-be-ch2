package kr.hhplus.be.server.interfaces;


public interface RestTemplateClient {

    <T> T post(String url, Object request, Class<T> responseType);

}
