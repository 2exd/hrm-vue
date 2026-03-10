import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { EmployeeRosterVO, EmployeeRosterForm, EmployeeRosterQuery } from '@/api/hrm/employeeRoster/types';

/**
 * 查询员工花名册列表
 * @param query
 * @returns {*}
 */

export const listEmployeeRoster = (query?: EmployeeRosterQuery): AxiosPromise<EmployeeRosterVO[]> => {
  return request({
    url: '/hrm/employeeRoster/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询员工花名册详细
 * @param id
 */
export const getEmployeeRoster = (id: string | number): AxiosPromise<EmployeeRosterVO> => {
  return request({
    url: '/hrm/employeeRoster/' + id,
    method: 'get'
  });
};

/**
 * 新增员工花名册
 * @param data
 */
export const addEmployeeRoster = (data: EmployeeRosterForm) => {
  return request({
    url: '/hrm/employeeRoster',
    method: 'post',
    data: data
  });
};

/**
 * 修改员工花名册
 * @param data
 */
export const updateEmployeeRoster = (data: EmployeeRosterForm) => {
  return request({
    url: '/hrm/employeeRoster',
    method: 'put',
    data: data
  });
};

/**
 * 删除员工花名册
 * @param id
 */
export const delEmployeeRoster = (id: string | number | Array<string | number>) => {
  return request({
    url: '/hrm/employeeRoster/' + id,
    method: 'delete'
  });
};
