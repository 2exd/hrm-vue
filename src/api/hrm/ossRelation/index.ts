import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { OssRelationVO, OssRelationForm, OssRelationQuery } from '@/api/hrm/ossRelation/types';

/**
 * 查询通用业务-OSS文件关联列表
 * @param query
 * @returns {*}
 */

export const listOssRelation = (query?: OssRelationQuery): AxiosPromise<OssRelationVO[]> => {
  return request({
    url: '/hrm/ossRelation/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询通用业务-OSS文件关联详细
 * @param id
 */
export const getOssRelation = (id: string | number): AxiosPromise<OssRelationVO> => {
  return request({
    url: '/hrm/ossRelation/' + id,
    method: 'get'
  });
};

/**
 * 新增通用业务-OSS文件关联
 * @param data
 */
export const addOssRelation = (data: OssRelationForm) => {
  return request({
    url: '/hrm/ossRelation',
    method: 'post',
    data: data
  });
};

/**
 * 修改通用业务-OSS文件关联
 * @param data
 */
export const updateOssRelation = (data: OssRelationForm) => {
  return request({
    url: '/hrm/ossRelation',
    method: 'put',
    data: data
  });
};

/**
 * 删除通用业务-OSS文件关联
 * @param id
 */
export const delOssRelation = (id: string | number | Array<string | number>) => {
  return request({
    url: '/hrm/ossRelation/' + id,
    method: 'delete'
  });
};
