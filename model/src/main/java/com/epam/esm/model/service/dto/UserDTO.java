package com.epam.esm.model.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDTO {

    private Long id;
    @Size(min = 2, max = 20)
    private String name;
    @Size(min = 2, max = 40)
    private String surname;
    private String username;
    private String password;
    private Set<OrderDTO> orders;
}
