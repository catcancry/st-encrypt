// 导入axios实例
import httpRequest from '@/util/request.js'


export function apiGetUserInfoByGet(param) {
    return httpRequest({
		url: '/server/test/encrypt2',
		method: 'get',
		encrypt:true,
		params: param,
	})
}


export function apiGetUserInfoByPost(param) {
    return httpRequest({
		url: '/server/test/encrypt2',
		method: 'post',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
		encrypt:true,
		data: param,
	})
}


export function apiGetUserInfo(param) {
    return httpRequest({
		url: '/server/test/encrypt',
		method: 'post',
		data: param,
		encrypt:true
	})
}


export function apiGetUserInfoNoEncrypt(param) {
    return httpRequest({
		url: '/server/test/noEncrypt',
		method: 'post',
		data: param,
		encrypt:false
	})
}


export function downloadFile(param) {
  return httpRequest({
    url: '',
    method: 'post',
    data: param,
    responseType: 'blob',
  })
}

export function upLoadFile(aesKey,param) {
  return httpRequest({
    url: '/server/test/encrypt2',
    method: 'post',
	aesKey:aesKey,
    data: param,
	encrypt:true,
    headers: { 'Content-Type': 'multipart/form-data;charset=utf-8' },
  })
}