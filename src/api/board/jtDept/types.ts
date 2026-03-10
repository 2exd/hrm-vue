export interface JtDeptVO {
  /**
   * 部门ID（主键）
   */
  id: string | number;

  /**
   * 部门名称
   */
  deptName: string;

  /**
   * 部门备注/说明
   */
  remark: string;

}

export interface JtDeptForm extends BaseEntity {
  /**
   * 部门ID（主键）
   */
  id?: string | number;

  /**
   * 部门名称
   */
  deptName?: string;

  /**
   * 部门备注/说明
   */
  remark?: string;

}

export interface JtDeptQuery extends PageQuery {

  /**
   * 部门名称
   */
  deptName?: string;

  /**
   * 日期范围参数
   */
  params?: any;
}
