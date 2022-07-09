package ru.arkasandr.carfinesearcher.model.enums;

import lombok.Getter;

@Getter
public enum RequestStatus {
    READY_FOR_SEND(),
    SENDING(),
    EXECUTED(),
    REJECTED(),
    DELETED();
}
