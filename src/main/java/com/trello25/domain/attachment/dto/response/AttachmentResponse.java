package com.trello25.domain.attachment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttachmentResponse {
    private String originalFileName;
    private String urlPath;
}