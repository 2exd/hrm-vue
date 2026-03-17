import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { EmployeeSocialInsuranceVO, EmployeeSocialInsuranceForm, EmployeeSocialInsuranceQuery } from '@/api/money/employeeSocialInsurance/types';

/**
 * 查询员工五险一金及专项附加扣除列表
 * @param query
 * @returns {*}
 */

export const listEmployeeSocialInsurance = (query?: EmployeeSocialInsuranceQuery): AxiosPromise<EmployeeSocialInsuranceVO[]> => {
  return request({
    url: '/money/employeeSocialInsurance/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询员工五险一金及专项附加扣除详细
 * @param id
 */
export const getEmployeeSocialInsurance = (id: string | number): AxiosPromise<EmployeeSocialInsuranceVO> => {
  return request({
    url: '/money/employeeSocialInsurance/' + id,
    method: 'get'
  });
};

/**
 * 新增员工五险一金及专项附加扣除
 * @param data
 */
export const addEmployeeSocialInsurance = (data: EmployeeSocialInsuranceForm) => {
  return request({
    url: '/money/employeeSocialInsurance',
    method: 'post',
    data: data
  });
};

/**
 * 修改员工五险一金及专项附加扣除
 * @param data
 */
export const updateEmployeeSocialInsurance = (data: EmployeeSocialInsuranceForm) => {
  return request({
    url: '/money/employeeSocialInsurance',
    method: 'put',
    data: data
  });
};

/**
 * 删除员工五险一金及专项附加扣除
 * @param id
 */
export const delEmployeeSocialInsurance = (id: string | number | Array<string | number>) => {
  return request({
    url: '/money/employeeSocialInsurance/' + id,
    method: 'delete'
  });
};
