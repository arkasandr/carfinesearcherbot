package ru.arkasandr.carfinesearcher.mapper;import org.mapstruct.Mapper;import org.mapstruct.factory.Mappers;import ru.arkasandr.carfinesearcher.controller.dto.UserDataDto;import ru.arkasandr.carfinesearcher.model.UserData;@Mapperpublic interface UserDataMapper {    UserDataMapper INSTANCE = Mappers.getMapper(UserDataMapper.class);    UserDataDto toDto(UserData data);    UserData toEntity(UserDataDto dto);}