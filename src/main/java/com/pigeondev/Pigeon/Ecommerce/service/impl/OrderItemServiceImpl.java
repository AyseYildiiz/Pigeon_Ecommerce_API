package com.pigeondev.Pigeon.Ecommerce.service.impl;

import com.pigeondev.Pigeon.Ecommerce.dto.OrderItemDto;
import com.pigeondev.Pigeon.Ecommerce.dto.OrderRequest;
import com.pigeondev.Pigeon.Ecommerce.dto.Response;
import com.pigeondev.Pigeon.Ecommerce.entity.Order;
import com.pigeondev.Pigeon.Ecommerce.entity.OrderItem;
import com.pigeondev.Pigeon.Ecommerce.entity.Product;
import com.pigeondev.Pigeon.Ecommerce.entity.User;
import com.pigeondev.Pigeon.Ecommerce.enums.OrderStatus;
import com.pigeondev.Pigeon.Ecommerce.exception.NotFoundException;
import com.pigeondev.Pigeon.Ecommerce.mapper.EntityDtoMapper;
import com.pigeondev.Pigeon.Ecommerce.repository.OrderItemRepo;
import com.pigeondev.Pigeon.Ecommerce.repository.OrderRepo;
import com.pigeondev.Pigeon.Ecommerce.repository.ProductRepo;
import com.pigeondev.Pigeon.Ecommerce.service.interf.OrderItemService;
import com.pigeondev.Pigeon.Ecommerce.service.interf.UserService;
import com.pigeondev.Pigeon.Ecommerce.specifications.OrderItemSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepo;
    private final UserService userService;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response placeOrder(OrderRequest orderRequest) {
        User user = userService.getLoginUser();
        //Map orderRequest items to other entities
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(orderItemRequest -> {
            Product product = productRepo.findById(orderItemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity())));
            orderItem.setStatus(OrderStatus.PENDING);
            orderItem.setUser(user);
            return orderItem;
        }).collect(Collectors.toList());
        //Calculate total price
        BigDecimal totalPrice = orderRequest.getTotalPrice() != null && orderRequest.getTotalPrice().compareTo(BigDecimal.ZERO) > 0
                ? orderRequest.getTotalPrice()
                : orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //Create order entity
        Order order = new Order();
        order.setOrderItemList(orderItems);
        order.setTotalPrice(totalPrice);

        //Set the order reference in each order item
        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        orderRepo.save(order);
        return Response.builder()
                .status(200)
                .message("Order successfully placed")
                .build();

    }

    @Override
    public Response updateOrderItemStatus(Long orderItemId, String status) {
        OrderItem orderItem = orderItemRepo.findById(orderItemId)
                .orElseThrow((() -> new NotFoundException("Order item not found")));

        orderItem.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderItemRepo.save(orderItem);
        return Response.builder()
                .status(200)
                .message("Order status successfully updated")
                .build();
    }

    @Override
    public Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable) {
        Specification<OrderItem> spec = OrderItemSpecification.hasStatus(status)
                .and(OrderItemSpecification.createdBetween(startDate, endDate))
                .and(OrderItemSpecification.hasItemId(itemId));
        Page<OrderItem> orderItemPage = orderItemRepo.findAll(spec, pageable);

        if (orderItemPage.isEmpty()) {
            throw new NotFoundException("Order item not found");
        }

        List<OrderItemDto> orderItemDtos = orderItemPage.getContent().stream().map(entityDtoMapper::mapOrderItemToDtoWithProductsAndUser).toList();

        return Response.builder()
                .status(200)
                .orderItemList(orderItemDtos)
                .totalPage(orderItemPage.getTotalPages())
                .totalElements(orderItemPage.getTotalElements())
                .build();
    }
}
