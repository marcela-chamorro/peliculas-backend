package com.unrn.peliculas.dto;

import lombok.Data;

import java.util.List;

@Data
public class DescuentoStockRequestDTO {

    private List<DescuentoStockDTO> peliculas;
}
