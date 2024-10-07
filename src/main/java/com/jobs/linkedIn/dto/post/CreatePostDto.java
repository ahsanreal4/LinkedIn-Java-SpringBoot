package com.jobs.linkedIn.dto.post;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostDto {
    @NotEmpty
    @Size(min =  5, max = 100)
    private String title;

    @NotNull
    @Positive
    private long postedBy;

    @NotEmpty
    @Size(min =  10, max = 255)
    private String description;
}
