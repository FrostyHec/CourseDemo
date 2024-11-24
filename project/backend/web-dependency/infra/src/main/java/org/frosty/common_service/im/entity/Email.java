package org.frosty.common_service.im.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Email {
    private String fromEmail;
    private String fromName;
    private String toEmail;
    private String subject;
    private String text;
    // TODO 支持附件等
}
