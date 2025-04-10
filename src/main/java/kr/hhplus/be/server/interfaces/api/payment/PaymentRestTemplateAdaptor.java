package kr.hhplus.be.server.interfaces.api.payment;

import kr.hhplus.be.server.interfaces.adptor.RestTemplateAdaptor;

public class PaymentRestTemplateAdaptor implements RestTemplateAdaptor {

    @Override
    public <T> T post(String url, Object request, Class<T> responseType) {
        return null;
    }
}
