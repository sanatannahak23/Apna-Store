package com.apna_store.order_service.service;

import com.apna_store.order_service.clients.CartClient;
import com.apna_store.order_service.clients.InventoryClient;
import com.apna_store.order_service.clients.UserClient;
import com.apna_store.order_service.dto.request.OrderRequest;
import com.apna_store.order_service.dto.response.ApiResponse;
import com.apna_store.order_service.dto.response.OrderResponse;
import com.apna_store.order_service.repository.OrderRepository;
import com.apna_store.order_service.service.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CartClient cartClient;

    private final UserClient userClient;

    private final InventoryClient inventoryClient;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
//        ✅Validate cart:
//            cart is not empty
//            items still valid(products exist, active, not discontinued)


//        ✅Validate address ( if required, shipping address must be available).
//
//        2. Inventory Management
//        ✅ Lock inventory (optimistic locking or reserved stock mechanism) for each item.
//            Example: move from availableQuantity → reservedQuantity.
//        ✅ Ensure no overselling (if not enough stock, abort order creation).
//        ✅ Handle concurrency (optimistic locking / atomic update).
//
//        3. Order Creation
//        ✅ Create Order entity with status = PENDING_PAYMENT.
//        ✅ Copy cart items → order items (snapshot at the time of order).
//            store productId, name, price, quantity, total per item
//            store userId and shipping address
//
//        4. Payment Handling
//        ✅ Call Payment Service to initiate payment.
//                Generate a paymentIntent or transactionId.
//                Save it in the Order (paymentId reference).
//        ✅ Order status flow:
//                PENDING_PAYMENT → PAID (success)
//                PENDING_PAYMENT → PAYMENT_FAILED (failure)
//
//        5. Event Handling / Saga (if using microservices)
//        Since you’re in a microservices architecture:
//                Use event-driven communication (Kafka, RabbitMQ, etc.).
//                Order Service emits ORDER_CREATED.
//                Payment Service consumes → initiates payment.
//                Payment Service emits PAYMENT_SUCCESS or PAYMENT_FAILED.
//                Order Service updates status accordingly.
//                Inventory Service listens for confirmed order → permanently deducts stock.
//
//        6. Failure Handling & Compensation
//        ✅ If payment fails:
//                Release reserved inventory (rollback reservation).
//                Mark order as CANCELLED.
//        ✅ If payment succeeds:
//                Deduct reserved inventory → permanent stock update.
//                Mark order as CONFIRMED.

//        @startuml
//        actor User
//        participant "Cart Service" as Cart
//        participant "Order Service" as Order
//        participant "Inventory Service" as Inventory
//        participant "Payment Service" as Payment
//
//        User -> Order:POST / orders(checkout)
//        Order -> Cart:GET / cart / {userId}
//        Cart-- > Order:cart items
//
//        Order -> Inventory:Reserve stock ( for each item)
//        Inventory-- > Order:Stock reserved (or fail)
//
//        alt Stock available
//        Order -> Order:Create Order (status = PENDING_PAYMENT)
//        Order -> Payment:Initiate Payment
//        Payment-- > Order:Payment initiated (transactionId)
//
//    alt Payment success
//        Payment -> Order:PAYMENT_SUCCESS event
//        Order -> Inventory:Confirm stock deduction
//        Inventory-- > Order:Stock deducted
//        Order -> User:Order CONFIRMED
//    else Payment failure
//        Payment -> Order:PAYMENT_FAILED event
//        Order -> Inventory:Release reserved stock
//        Inventory-- > Order:Stock released
//        Order -> User:Order CANCELLED
//        end
//    else Stock not available
//        Inventory-- > Order:Reservation failed
//        Order -> User:Out of stock error
//        end
//        @enduml

        return null;
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        return null;
    }

    @Override
    public List<OrderResponse> getOrderByUserId(Long userId) {
        return List.of();
    }

    @Override
    public OrderResponse updateStatus(Long orderId) {
        return null;
    }

    @Override
    public OrderResponse cancelOrder(Long orderId) {
        return null;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return List.of();
    }

    @Override
    public OrderResponse updateOrder(Long orderId) {
        return null;
    }
}
