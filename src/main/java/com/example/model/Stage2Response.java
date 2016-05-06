package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Shaun Fleming
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stage2Response
{
    private Integer requestId;
    private Integer subRequestId;
    private String message;

    public String getCompositeId()
    {
        return this.requestId + "-" + this.subRequestId;
    }
}
