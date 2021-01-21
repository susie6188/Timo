package com.linln.modules.protectArea.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.StatusUtil;
import com.linln.component.excel.annotation.Excel;
import com.linln.modules.system.domain.User;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author susie
 * @date 2020/11/26
 */
@Data
@Entity
@Table(name="pa_protectAreaBefore")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.NOT_DELETE)
public class ProtectAreaBefore implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    // 级别
    private String level;
    // 类型
    private String category;
    // 经度范围
    private String longitude;
    // 纬度范围
    private String latitude;
    // 四至范围
    private String fourRange;
    // 始建面积（公顷）
    private String initialArea;
    // 现状面积（公顷）
    private String currentArea;
    // 陆地面积（公顷）
    private String landArea;
    // 海域面积（公顷）
    private String seaArea;
    // 始建时间（年）
    private String originalTime;
    // 始建文号
    private String originalDocumentNo;
    // 晋升省级时间（年）
    private String promotionProvincialLevelTime;
    // 晋升省级面积（公顷）
    private String promotionProvincialLevelArea;
    // 晋升省级文号
    private String promotionProvincialLevelNo;
    // 晋升国家级时间（年）
    private String promotionNationalLevelTime;
    // 晋升国家级面积（公顷）
    private String promotionNationalLevelArea;
    // 晋升国家级文号
    private String promotionNationalLevelNo;
    // 所在省
    private String province;
    // 所在市
    private String city;
    // 所在县
    private String county;
    // 主要保护对象
    private String protectedObjects;
    // 是否独立管理机构
    private String isIndependentInstitution;
    // 管理机构级别
    private String institutionLevel;
    // 管理机构成立时间（年）
    private String institutionEstablishmentTime;
    // 管理机构性质
    private String institutionNature;
    // 管理机构名称
    private String institutionName;
    // 管理机构隶属
    private String institutionAffiliation;
    // 是否有总规
    private String isMasterPlan;
    // 总规批复时间
    private String replyTime;
    // 功能分区
    private String functionalPartition;
    // 整合情况
    private String integration;
    // 备注
    @Excel("备注")
    private String remarks;
    // 创建时间
    @CreatedDate
    private Date createDate;
    // 更新时间
    @LastModifiedDate
    private Date updateDate;
    // 创建者
    @CreatedBy
    @ManyToOne(fetch=FetchType.LAZY)
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="create_by")
    @JsonIgnore
    private User createBy;
    // 更新者
    @LastModifiedBy
    @ManyToOne(fetch=FetchType.LAZY)
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="update_by")
    @JsonIgnore
    private User updateBy;
    // 数据状态
    private Byte status = StatusEnum.OK.getCode();
}