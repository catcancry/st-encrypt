<script setup>
import { ref } from 'vue'
import { apiGetUserInfo,apiGetUserInfoByGet,apiGetUserInfoByPost,apiGetUserInfoNoEncrypt } from '@/apis/user-api'

defineProps({
  msg: String
})

const count = ref(0)


	// apiGetUserInfo({
	// 	userID: '10001',
	// 	userName: 'Mike',
	// }).then((res) => {
	// 	console.log("apiGetUserInfo加密请求->最终请求结果:",res)
	// })
	
	apiGetUserInfoByGet({
		userID: '10001',
		userName: 'Mike',
	}).then((res) => {
		console.log("apiGetUserInfoByGet加密请求->最终请求结果:",res)
	})
	
	// apiGetUserInfoByPost({
	// 	userID: '10001',
	// 	userName: 'Mike',
	// }).then((res) => {
	// 	console.log("apiGetUserInfoByPost加密请求->最终请求结果:",res)
	// })
	
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
