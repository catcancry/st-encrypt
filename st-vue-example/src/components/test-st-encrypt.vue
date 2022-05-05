<script setup>
import { ref } from 'vue'
import { apiGetUserInfo,apiGetUserInfoNoEncrypt } from '@/apis/user-api'

defineProps({
  msg: String
})

const count = ref(0)

	apiGetUserInfo({
		userID: '10001',
		userName: 'Mike',
	}).then((res) => {
		console.log("加密请求->最终请求结果:",res)
	})
	apiGetUserInfoNoEncrypt({
		userID: '10001',
		userName: 'Mike',
	}).then((res) => {
		console.log("未加密请求->最终请求结果:",res)
	})
</script>

<template>
 
  <div @click="test">{{msg}}</div>
 
 
</template>

<style scoped>
a {
  color: #42b983;
}
</style>
