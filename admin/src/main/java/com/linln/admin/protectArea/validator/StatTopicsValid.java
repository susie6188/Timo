package com.linln.admin.protectArea.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

/**
 * @author susie
 * @date 2021/01/18
 */
@Data
public class StatTopicsValid implements Serializable {
    @NotEmpty(message = "统计专题名称不能为空")
    private String topics;
}