package com.linln.admin.protectArea.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

/**
 * @author susie
 * @date 2020/11/26
 */
@Data
public class ProtectAreaBeforeValid implements Serializable {
    @NotEmpty(message = "名称不能为空")
    private String name;
}