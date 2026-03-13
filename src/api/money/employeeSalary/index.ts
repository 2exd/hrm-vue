import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { EmployeeSalaryVO, EmployeeSalaryForm, EmployeeSalaryQuery } from '@/api/money/employeeSalary/types';

/**
 * 查询员工薪资列表
 * @param query
 * @returns {*}
 */

export const listEmployeeSalary = (query?: EmployeeSalaryQuery): AxiosPromise<EmployeeSalaryVO[]> => {
  return request({
    url: '/money/employeeSalary/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询员工薪资详细
 * @param id
 */
export const getEmployeeSalary = (id: string | number): AxiosPromise<EmployeeSalaryVO> => {
  return request({
    url: '/money/employeeSalary/' + id,
    method: 'get'
  });
};

/**
 * 新增员工薪资
 * @param data
 */
export const addEmployeeSalary = (data: EmployeeSalaryForm) => {
  return request({
    url: '/money/employeeSalary',
    method: 'post',
    data: data
  });
};

/**
 * 修改员工薪资
 * @param data
 */
export const updateEmployeeSalary = (data: EmployeeSalaryForm) => {
  return request({
    url: '/money/employeeSalary',
    method: 'put',
    data: data
  });
};

/**
 * 删除员工薪资
 * @param id
 */
export const delEmployeeSalary = (id: string | number | Array<string | number>) => {
  return request({
    url: '/money/employeeSalary/' + id,
    method: 'delete'
  });
};
