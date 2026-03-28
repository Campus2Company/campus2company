package com.campus2company.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountStatusChangedEvent extends BaseEvent {

    private UUID userId;
    private String email;
    private String role;
    private String previousStatus;
    private String newStatus;
}
