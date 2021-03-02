package com.epam.esm.controller.rest;

import com.epam.esm.model.service.OrderService;
import com.epam.esm.model.service.UserService;
import com.epam.esm.model.service.dto.OrderDTO;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.dto.UserDTO;
import com.epam.esm.model.service.exception.IncorrectArgumentException;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.exception.ServiceException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends CommonController<UserDTO> {

    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> find(@PathVariable(name = "id") Long id)
            throws ServiceException, NotExistEntityException {
        UserDTO userDTO = userService.find(id);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/{id}/orders")
    public ResponseEntity<OrderDTO> orderCertificate(@PathVariable(name = "id") Long id, @RequestBody OrderDTO orderDTO)
            throws ServiceException {
        orderDTO.setUserId(id);
        orderDTO.setPurchaseTime(LocalDateTime.now().toString());
        OrderDTO order = orderService.add(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/top-tag")
    public ResponseEntity<TagDTO> getTopUserTag() throws ServiceException {
        TagDTO tag = orderService.getTopUserTag();
        return ResponseEntity.ok(tag);
    }

    @Override
    @GetMapping
    public ResponseEntity<PagedModel<UserDTO>> findAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) throws ServiceException, IncorrectArgumentException {
        List<UserDTO> tags = userService.findAll(page, size);
        long count = userService.count();
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, count);
        List<Link> linkList = buildLink(page, size, (int) pageMetadata.getTotalPages());
        PagedModel<UserDTO> pagedModel = PagedModel.of(tags, pageMetadata, linkList);
        return ResponseEntity.ok(pagedModel);
    }
}
