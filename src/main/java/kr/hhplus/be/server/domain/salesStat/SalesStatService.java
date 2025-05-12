package kr.hhplus.be.server.domain.salesStat;

import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO 2025.05.07 SalesStatService To SalesStatFacade
@Service
public class SalesStatService {

    private final SalesStatRepository salesStatRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public SalesStatService(
            SalesStatRepository salesStatRepository,
            PaymentRepository paymentRepository,
            OrderRepository orderRepository) {
        this.salesStatRepository = salesStatRepository;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public void createAll(SalesStatCommand.Creates command){
        List<SalesStat> salesStats = command.getCreates().stream()
                .map((create) -> SalesStat.create(
                        create.getProductId(),
                        create.getSalesDate(),
                        create.getSalesAmount())
                )
                .toList();

        salesStatRepository.saveAll(salesStats);
    }

}
