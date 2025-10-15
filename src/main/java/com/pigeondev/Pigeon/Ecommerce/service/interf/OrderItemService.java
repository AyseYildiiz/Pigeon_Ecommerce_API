package com.pigeondev.Pigeon.Ecommerce.service.interf;

import com.pigeondev.Pigeon.Ecommerce.dto.OrderRequest;
import com.pigeondev.Pigeon.Ecommerce.dto.Response;
import com.pigeondev.Pigeon.Ecommerce.enums.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface OrderItemService {

    Response placeOrder(OrderRequest orderRequest);

    Response updateOrderItemStatus(Long orderItemId, String status);

    Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable);

}
