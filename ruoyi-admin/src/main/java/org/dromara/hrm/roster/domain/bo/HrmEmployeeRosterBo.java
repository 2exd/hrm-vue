package org.dromara.hrm.roster.domain.bo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.hrm.roster.domain.HrmEmployeeRoster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 员工花名册业务对象 hrm_employee_roster
 *
 * @author Lion Li
 * @date 2026-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrmEmployeeRoster.class, reverseConvertGenerate = false)
public class HrmEmployeeRosterBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 系统用户ID（关联sys_user.user_id）
     */
    @NotNull(message = "系统用户ID（关联sys_user.user_id）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long userId;

    /**
     * 员工姓名
     */
    @NotBlank(message = "员工姓名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String employeeName;

    /**
     * 身份证号（唯一）
     */
    @NotBlank(message = "身份证号（唯一）不能为空", groups = {AddGroup.class, EditGroup.class})
    private String idCard;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 家庭住址
     */
    private String homeAddress;

    /**
     * 紧急联系人姓名
     */
    private String emergencyContact;

    /**
     * 紧急联系人电话
     */
    private String emergencyContactPhone;

    /**
     * 银行卡号
     */
    private String bankCard;

    /**
     * 银行卡行号
     */
    private String bankBranchCode;

    /**
     * 开户行名称
     */
    private String bankName;

    /**
     * 日薪
     */
    private Long dailySalary;

    /**
     * 月薪
     */
    private Long monthlySalary;

    /**
     * 劳动合同签订时间（进场时间）
     */
    @NotNull(message = "劳动合同签订时间（进场时间）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Date contractSignTime;

    /**
     * 劳动合同编码
     */
    @NotBlank(message = "劳动合同编码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String contractCode;

    /**
     * 劳动合同到期时间
     */
    @NotNull(message = "劳动合同到期时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private Date contractExpireTime;

    /**
     * 退场时间
     */
    private Date exitTime;

    /**
     * 备注
     */
    private String remark;

    @JsonDeserialize(using = StringToListLongDeserializer.class)
    private List<Long> file;

    /**
     * 自定义反序列化器：将逗号分隔的字符串转为List<Long>
     */
    public static class StringToListLongDeserializer extends StdDeserializer<List<Long>> {

        public StringToListLongDeserializer() {
            this(null);
        }

        public StringToListLongDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public List<Long> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            String value = jp.getText();
            List<Long> result = new ArrayList<>();
            if (value == null || value.trim().isEmpty()) {
                return result;
            }
            // 按逗号分割字符串，转换为Long类型
            String[] ids = value.split(",");
            for (String id : ids) {
                try {
                    result.add(Long.parseLong(id.trim()));
                } catch (NumberFormatException e) {
                    // 忽略无效的数字格式，避免解析失败
                    continue;
                }
            }
            return result;
        }
    }
}
