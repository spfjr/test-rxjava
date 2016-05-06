package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * @author Shaun Fleming
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stage1Response
{
    private Integer requestId;
    private List<String> messages;
}
