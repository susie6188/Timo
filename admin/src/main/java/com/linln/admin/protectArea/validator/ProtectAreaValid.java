package com.linln.admin.protectArea.validator;

import com.linln.component.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

/**
 * @author susie
 * @date 2020/11/26
 */
@Data
public class ProtectAreaValid implements Serializable {
    @NotEmpty(message = "名称不能为空")
    private String name;
    // 始建时间（年）
    @Digits(integer = 4,fraction = 0)
    private  Long originalTime;
    // 晋升省级时间（年）
    @Digits(integer = 4,fraction = 0)
    private Long promotionProvincialLevelTime;
    // 晋升国家级时间（年）
    @Digits(integer = 4,fraction = 0)
    private Long promotionNationalLevelTime;
    // 管理机构成立时间（年）
    @Digits(integer = 4,fraction = 0)
    private Long institutionEstablishmentTime;

    // 始建面积（公顷）
    @Digits(integer = 10,fraction = 2)
    @PositiveOrZero
    private Double initialArea;
    // 现状面积（公顷）
    @Digits(integer = 10,fraction = 2)
    @PositiveOrZero
    private Double currentArea;
    // 陆地面积（公顷）
    @Digits(integer = 10,fraction = 2)
    @PositiveOrZero
    private Double landArea;
    // 海域面积（公顷）
    @Digits(integer = 10,fraction = 2)
    @PositiveOrZero
    private Double seaArea;
    // 晋升省级面积（公顷）
    @Digits(integer = 10,fraction = 2)
    @PositiveOrZero
    private Double promotionProvincialLevelArea;
    // 晋升国家级面积（公顷）
    @Digits(integer = 10,fraction = 2)
    @PositiveOrZero
    private Double promotionNationalLevelArea;
}