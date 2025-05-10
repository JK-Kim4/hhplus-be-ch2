package kr.hhplus.be.server.domain.salesStat;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public SalesStatInfo.SalesStats getSalesStatsByDate(SalesStatCommand.SalesStats command){
        //1.타켓 날짜에 결제 완료된 주문 목록 조회
        List<Payment> payments = paymentRepository.findAllByPaidDate(command.getTargetDate());
        Set<Long> targetOrderIds = payments.stream()
                .map(Payment::getOrderId).
                collect(Collectors.toSet());

        //2.주문 포함 판매 상품 목록 조회
        List<OrderItem> orderItems = orderRepository.findOrderItemsByOrderIds(targetOrderIds);
        Map<Long, Long> orderItemCountMap = orderItems.stream()
                .collect(Collectors.groupingBy(
                        OrderItem::getProductId,
                        Collectors.summingLong(OrderItem::getQuantity)
                ));

        //3.Product to SalesStats convert && return
        return SalesStatInfo.SalesStats.of(orderItemCountMap, command.getTargetDate());
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
