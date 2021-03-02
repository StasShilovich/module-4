package com.epam.esm.model.service.converter.impl;

import com.epam.esm.model.dao.entity.Order;
import com.epam.esm.model.dao.entity.User;
import com.epam.esm.model.service.converter.DTOMapper;
import com.epam.esm.model.service.dto.OrderDTO;
import com.epam.esm.model.service.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDTOMapper implements DTOMapper<UserDTO, User> {

    private final OrderDTOMapper orderMapper;

    public UserDTOMapper(OrderDTOMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public UserDTO toDTO(User user) {
        Set<OrderDTO> orderDTOS = user.getOrders() != null ?
                user.getOrders().stream().map(orderMapper::toDTO).collect(Collectors.toSet()) : null;
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .orders(orderDTOS)
                .build();
    }

    @Override
    public User fromDTO(UserDTO userDTO) {
        Set<Order> orders = userDTO.getOrders() != null ?
                userDTO.getOrders().stream().map(orderMapper::fromDTO).collect(Collectors.toSet()) : null;
        return User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .orders(orders)
                .build();
    }
}
