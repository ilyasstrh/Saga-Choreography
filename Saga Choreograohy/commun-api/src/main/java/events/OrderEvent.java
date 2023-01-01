package events;

import dto.OrderRequestDto;
import dto.OrderResponseDto;
import enums.OrderStatus;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class OrderEvent implements Event {
    private UUID eventId = UUID.randomUUID();
    private Date  eventDate = new Date();
    private OrderRequestDto orderRequestDto;
    private OrderStatus orderStatus ;

    public OrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus) {
        this.orderRequestDto = orderRequestDto;
        this.orderStatus = orderStatus;
    }

    @Override
    public UUID getEventID() {
        return this.eventId;
    }

    @Override
    public Date getDate() {
        return this.eventDate;
    }
}
