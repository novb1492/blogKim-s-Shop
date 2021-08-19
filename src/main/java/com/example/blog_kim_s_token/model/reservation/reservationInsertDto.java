package com.example.blog_kim_s_token.model.reservation;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class reservationInsertDto {

    @NotBlank(message = "자리가 빈칸입니다")
    private String seat;

    @Min(value = 1,message = "월이 1보다 작습니다")
    @Max(value = 12,message = "월이 12월보다 큽니다")
    private int month;
    
    @Min(value = 1,message = "일이 1보다 작습니다")
    @Max(value = 31,message = "일이 31보다 큽니다")
    private int date;
    private List<Integer>times;
    @NotBlank(message = "결제번호가 없습니다")
    private String impId;
}