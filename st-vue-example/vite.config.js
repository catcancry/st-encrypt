import {
	defineConfig
} from 'vite'
import vue from '@vitejs/plugin-vue'
import * as path from 'path';

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [vue()],

	resolve: {
		alias: {
			'@': path.resolve(__dirname, './src') //设置别名
		}
	},
	server: {
		port: 3000, //启动端口
		host:"0.0.0.0",
		open: false,
		// proxy: {
		// 	'/api': {
		// 		target: 'http://127.0.0.1:8080',
		// 		changeOrigin: true,
		// 		rewrite: (path) => path.replace(/^\/api/, '')
		// 	},
		// },
		cors: true
	}
})
