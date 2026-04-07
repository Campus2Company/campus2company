package com.campus2company.universityadmin.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUniversityProfileRequest {

    private String name;
    private String description;
    private String country;
}