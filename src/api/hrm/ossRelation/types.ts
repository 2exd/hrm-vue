export interface OssRelationVO {
  /**
   * 主键ID
   */
  id: string | number;

  /**
   * 备注
   */
  remark: string;

  /**
   * 业务类型（EMPLOYEE/员工、CONTRACT/合同、ATTENDANCE/考勤）
   */
  businessType: string;

  /**
   * 业务表主键ID（如员工表id、合同表id）
   */
  businessId: string | number;

  /**
   * OSS文件主键ID
   */
  ossId: string | number;

}

export interface OssRelationForm extends BaseEntity {
  /**
   * 主键ID
   */
  id?: string | number;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 业务类型（EMPLOYEE/员工、CONTRACT/合同、ATTENDANCE/考勤）
   */
  businessType?: string;

  /**
   * 业务表主键ID（如员工表id、合同表id）
   */
  businessId?: string | number;

  /**
   * OSS文件主键ID
   */
  ossId?: string | number;

}

export interface OssRelationQuery extends PageQuery {

  /**
   * 日期范围参数
   */
  params?: any;
}
