<script setup>
import { ref } from 'vue'
import { apiGetUserInfo,apiGetUserInfoByGet,apiGetUserInfoByPost,apiGetUserInfoNoEncrypt } from '@/apis/user-api'

defineProps({
  msg: String
})

const count = ref(0)

	//带参数
	// apiGetUserInfo({
	// 	t: 'tdddddd',
	// 	appId: 'appIdddddddd',
	// 	auth: 'authddddddd',
	// 	test:"test12345",
	// 	data:"测试中文@%*&=@&！@#￥@#%#……&（&）（*&……#！"
	// }).then((res) => {
	// 	console.log("apiGetUserInfo加密请求(带参数)->最终请求结果:",res)
	// })
	//不带参数
	// apiGetUserInfo(null).then((res) => {
	// 	console.log("apiGetUserInfo加密请求(不带参数)->最终请求结果:",res)
	// })
	//get 请求,带参数请求
	// apiGetUserInfoByGet({
	// 	t: 'tdddddd',
	// 	appId: 'appIdddddddd',
	// 	auth: 'authddddddd',
	// 	test:"test12345",
	// 	data:"测试中文@%*&=@&！@#￥@#%#……&（&）（*&……#！"
	// }).then((res) => {
	// 	console.log("apiGetUserInfoByGet加密请求(带参数)->最终请求结果:",res)
	// })
	// //get请求不带参数 覆盖null,{},""等情况
	// apiGetUserInfoByGet(null).then((res) => {
	// 	console.log("apiGetUserInfoByGet加密请求(不带参数)->最终请求结果:",res)
	// })
	
	//post请求,带参数请求
	apiGetUserInfoByPost({
		t: 'tdddddd',
		appId: 'appIdddddddd',
		auth: 'authddddddd',
		test:"test12345",
		data:"测试中文@%*&=@&！@#￥@#%#……&（&）（*&……#！"
	}).then((res) => {
		console.log("apiGetUserInfoByPost加密请求(带参数)->最终请求结果:",res)
	})
	
	// apiGetUserInfoNoEncrypt({
	// 	userID: '10001',
	// 	userName: 'Mike',
	// }).then((res) => {
	// 	console.log("apiGetUserInfoNoEncrypt未加密请求->最终请求结果:",res)
	// })
</script>

<template>
 
  <div @click="test">{{msg}}</div>
 
 
</template>

<style scoped>
a {
  color: #42b983;
}
</style>
