package com.luis.drakdex.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CriaturaDTO {

    private Long id;

    // @NotBlank: Não permite null nem texto vazio ""
    @NotBlank(message = "O nome da criatura é obrigatório!")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    private String nome;

    @NotBlank(message = "O tipo da criatura é obrigatório!")
    private String tipo;

    // @Min: Garante que o número é pelo menos 1
    @Min(value = 1, message = "O nível não pode ser menor que 1")
    private Integer nivel;

    // Descrição é opcional, então não colocamos @NotBlank, mas limitamos o tamanho
    @Size(max = 255, message = "A descrição não pode ter mais de 255 caracteres")
    private String descricao;
}