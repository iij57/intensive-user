package com.sk.svdonation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
public class SVWalletHistoryDTO {
    private long amountOfSVC;
    private long amountOfSVP;
    private String category;
    private String name;
    private String etc;
    @JsonIgnore
    private LocalDateTime ldt_usedAt;
    private String usedAt;
}