package org.dromara.hrm.money.service;

import org.dromara.hrm.money.domain.vo.HrmEmployeeSalaryVo;
import org.dromara.hrm.money.domain.bo.HrmEmployeeSalaryBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 员工薪资Service接口
 *
 * @author 2exd
 * @date 2026-03-13
 */
public interface IHrmEmployeeSalaryService {

    /**
     * 查询员工薪资
     *
     * @param id 主键
     * @return 员工薪资
     */
    HrmEmployeeSalaryVo queryById(Long id);

    /**
     * 分页查询员工薪资列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工薪资分页列表
     */
    TableDataInfo<HrmEmployeeSalaryVo> queryPageList(HrmEmployeeSalaryBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的员工薪资列表
     *
     * @param bo 查询条件
     * @return 员工薪资列表
     */
    List<HrmEmployeeSalaryVo> queryList(HrmEmployeeSalaryBo bo);

    /**
     * 新增员工薪资
     *
     * @param bo 员工薪资
     * @return 是否新增成功
     */
    Boolean insertByBo(HrmEmployeeSalaryBo bo);

    /**
     * 修改员工薪资
     *
     * @param bo 员工薪资
     * @return 是否修改成功
     */
    Boolean updateByBo(HrmEmployeeSalaryBo bo);

    /**
     * 校验并批量删除员工薪资信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
