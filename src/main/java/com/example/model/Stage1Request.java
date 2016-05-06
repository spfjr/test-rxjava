package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collections;
import java.util.List;

/**
 * @author Shaun Fleming
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stage1Request
{
    private Integer requestId;
    private List<String> messages;

    public Stage1Request(final Integer requestId, final String message)
    {
        this.requestId = requestId;
        this.messages = Collections.singletonList(message);
    }
}
