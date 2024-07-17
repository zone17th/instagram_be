package org.app.instagram_be.mappers;

import org.app.instagram_be.model.dto.LoginResponseDTO;
import org.app.instagram_be.model.entities.Account;
import org.app.instagram_be.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    LoginResponseDTO toLoginResponseDTO(User user);
}
