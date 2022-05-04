import CryptoJS from 'crypto-js';
import jsrsasign from 'jsrsasign';


/* 
 * crypto-js进行AES加密,安装: npm i crypto-js
 * jsrsasign进行rsa加密,安装: npm i jsrsasign
*/

const StClientUtil = {};

/**
 * 随机生成16位的AES密钥
 */
function getKeyAES() {
  const key = []
  for (let i = 0; i < 16; i++) {
    const num = Math.floor(Math.random() * 26)
    const charStr = String.fromCharCode(97 + num)
    key.push(charStr.toUpperCase())
  }
  const result = key.join('')
  return result
}


/**
 * AES加密
 * 转Utf8编码: CryptoJS.enc.Utf8.parse();
 * 转Base64: CryptoJS.enc.Base64.stringify();
 * @param data 需要加密的数据
 * @param key 密钥
 * @returns 加密后的数据
 */
function encodeAES(data, key) {
  if (typeof data !== 'string') {
    data = JSON.stringify(data)
  }
  // AES加密
  // const result = CryptoJS.AES.encrypt(data, CryptoJS.enc.Utf8.parse(key), {
  //   iv: CryptoJS.enc.Utf8.parse('1234567812345678'), // 向量。使用CBC模式时，需要使用向量。
  //   mode: CryptoJS.mode.CBC,
  //   padding: CryptoJS.pad.ZeroPadding // 偏移量。使用非补码方式时，需要使用ZeroPadding。默认为PKCS5Padding。
  // })
  const result = CryptoJS.AES.encrypt(data, CryptoJS.enc.Utf8.parse(key), {
    mode: CryptoJS.mode.ECB
  })
  // base64转码
  return CryptoJS.enc.Base64.stringify(result.ciphertext)
}

/**
 * AES解密
 * @param data 需要解密的数据
 * @param key 密钥
 * @returns 解密后的数据
 */
function decodeAES(data, key) {
  if (typeof data !== 'string') {
    data = JSON.stringify(data)
  }
  // const result = CryptoJS.AES.decrypt(data, CryptoJS.enc.Utf8.parse(key), {
  //   iv: CryptoJS.enc.Utf8.parse('1234567812345678'), // 向量。使用CBC模式时，需要使用向量。
  //   mode: CryptoJS.mode.CBC,
  //   padding: CryptoJS.pad.ZeroPadding // 偏移量。使用非补码方式时，需要使用ZeroPadding。默认为PKCS5Padding。
  // })
  const result = CryptoJS.AES.decrypt(data, CryptoJS.enc.Utf8.parse(key), {
	mode: CryptoJS.mode.ECB,
  })
  // 转为utf-8编码
  return CryptoJS.enc.Utf8.stringify(result)
}

/**
 * RSA加密
 * @param data 需要加密的数据
 * @param key 密钥
 * @returns 加密后的数据
 */
function encodeRSA(data, key) {
  if (typeof data !== 'string') {
    data = JSON.stringify(data)
  }		
  let pk = "-----BEGIN PUBLIC KEY-----\n" + key + "\n-----END PUBLIC KEY-----";
  let pub = new  jsrsasign.KEYUTIL.getKey(pk);
  let enc = jsrsasign.KJUR.crypto.Cipher.encrypt(data,pub);
  return enc;
}

/**
 * RSA解密
 * @param data 需要解密的数据
 * @param key 密钥
 * @returns 解密后的数据
 */
function decodeRSA(data, key) {
  if (typeof data !== 'string') {
    data = JSON.stringify(data)
  }		
  let pk = "-----BEGIN PRIVATE  KEY-----\n" + key + "\n-----END PRIVATE  KEY-----";
  let prv = jsrsasign.KEYUTIL.getKey(pk);
  return jsrsasign.KJUR.crypto.Cipher.decrypt(data,prv);
}

/**
 * 签名,MD5withRSA 签名并转换成hex编码
 * @param data 需要签名的数据
 * @param key SHA1签名的密钥
 */
function sign(data, key ) {
  if (typeof data !== 'string') {
    data = JSON.stringify(data)
  }		    
  let pk = "-----BEGIN PRIVATE  KEY-----\n" + key + "\n-----END PRIVATE  KEY-----";
  // RSA signature generation
  var rsaKey = new jsrsasign.KEYUTIL.getKey(pk);
  var sig = new jsrsasign.KJUR.crypto.Signature({"alg": "MD5withRSA"});
  sig.init(rsaKey);
  sig.updateString(data);
  return  jsrsasign.hextob64(signature.sign());
}

/**
 * 验证签名 
 * @param  data 签名内容
 * @param  sign 签名值
 * @param  key rsa公钥
 */
function signVerify(data,sign, key) {  
  if (typeof data !== 'string') {
	data = JSON.stringify(data)
  }
  let pk = "-----BEGIN PUBLIC KEY-----\n" + key + "\n-----END PUBLIC KEY-----";
  // RSA signature generation
  var rsaKey = new jsrsasign.KEYUTIL.getKey(pk);
  var sig = new jsrsasign.KJUR.crypto.Signature({"alg": "MD5withRSA"});
  sig.init(rsaKey);
  sig.updateString(data);
  return sig.verify(sign);
}

/**
 * 通过md5获取摘要信息
 * @param  data
 */
function md5(data){
	if (typeof data !== 'string') {
	  data = JSON.stringify(data)
	}
	return CryptoJS.MD5(data).toString();
}

/**
 * 格式化日期
 * @param date 日期
 * @param formatType 格式化类型
 */
function format(date, formatType = 'yyyy-MM-dd hh:mm:ss') {
  // eslint-disable-next-line no-extend-native
  Date.prototype.format =
    function(fmt) {
      const o = {
        'M+': this.getMonth() + 1, // 月份
        'd+': this.getDate(), // 日
        'h+': this.getHours(), // 小时
        'm+': this.getMinutes(), // 分
        's+': this.getSeconds(), // 秒
        'q+': Math.floor((this.getMonth() + 3) / 3), // 季度
        S: this.getMilliseconds() // 毫秒
      }
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (this.getFullYear() + '').substr(4 - RegExp.$1.length)
        )
      }
      for (const k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) {
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length === 1
              ? o[k]
              : ('00' + o[k]).substr(('' + o[k]).length)
          )
        }
      }
      return fmt
    }
  if (!date || !date.format) {
    return date
  }
  return date.format(formatType)
}

/**
 * 随机生成16位的AES密钥
 */
StClientUtil.createAESBase64Key = ()=>{
	const key = []
	  for (let i = 0; i < 16; i++) {
	    const num = Math.floor(Math.random() * 26)
	    const charStr = String.fromCharCode(97 + num)
	    key.push(charStr.toUpperCase())
	  }
	  const result = key.join('')
	return  result
}


StClientUtil.encrypt = (publicKey,aesKey,t,appId,appAuth,data)=>{

	appId = appId == null ? "null":appId;
	appAuth = appAuth == null ? "null":appAuth;
	//安装格式拼接代码
	let keyTemp = [aesKey,t,appId,appAuth].join("###");
	//rsa加密key
	let keyTempEncrypt = encodeRSA(keyTemp,publicKey);
	
	//进行内容摘要
	let sign = md5(keyTemp);
	//aes加密内容
	let dataTemp = encodeAES(data,aesKey) ;
	
	return {sign:sign,key:keyTempEncrypt,data:dataTemp}
}

StClientUtil.dencrypt = (publicKey,aesKey,data,func)=>{
	//解密请求结果
	let sign = data.sign;
	let data2 = data.data;

	//使用aes解密响应结果
	let dataTemp = decodeAES(data2,aesKey);	
	//验证签名是否正确
	let v = signVerify(dataTemp,sign,publicKey) 
	return {signVerify:v,data:JSON.parse(dataTemp)};
}

export default StClientUtil