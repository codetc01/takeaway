package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/27 15:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Top10VO {
    private Long dishId;
    private Long setmealId;
    private Integer total;
    private String name;
}
