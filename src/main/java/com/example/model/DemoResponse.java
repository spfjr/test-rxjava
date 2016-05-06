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
public class DemoResponse
{
    private Integer requestId;
    private String message;
}
