import axios from 'axios';

   var  root =  "http://106.14.178.184/im3"

  // queryGet(params, resolve, reject, root) {
  //
  //   fetch(this.baseUrl + root, {
  //     method: 'POST',
  //     body: params,
  //   })
  //     .then(res => res.text())
  //     .then(text => {
  //       console.log(text);
  //       resolve(JSON.parse(text));
  //     })
  //     .catch(err => reject(err));
  // },
  //
  // queryPost(params, resolve, reject, root) {
  //
  //   fetch(this.baseUrl + root, {
  //     method: 'POST',
  //     headers: {
  //       'Content-Type': 'application/json;charset=UTF-8',
  //     },
  //     body: params,
  //   })
  //     .then(res => res.text())
  //     .then(text => {
  //       console.log(text);
  //       resolve(JSON.parse(text));
  //     })
  //     .catch(err => reject(err));
  // }

// 引用axios
// 自定义判断元素类型JS
function toType (obj) {
  return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase()
}
// 参数过滤函数
function filterNull (o) {
  for (var key in o) {
    if (o[key] === null) {
      delete o[key]
    }
    if (toType(o[key]) === 'string') {
      o[key] = o[key].trim()
    } else if (toType(o[key]) === 'object') {
      o[key] = filterNull(o[key])
    } else if (toType(o[key]) === 'array') {
      o[key] = filterNull(o[key])
    }
  }
  return o
}


function apiAxios (method, url, params, success, failure) {
  if (params) {
    params = filterNull(params)
  }

  axios({
    headers: {
      'Content-Type': 'application/json;',
    },
    method: method,
    url: url,
    data: method === 'POST' || method === 'PUT'? params : null,
    params: method === 'GET' || method === 'DELETE' ? params : null,
    baseURL: root,
    withCredentials: true
  })
    .then((res)=>{success(res.data)})
    .catch((err)=>{failure(err)});
}

// 返回在vue模板中的调用接口
export default {
  get: function ( params, success, failure,url) {
    return apiAxios('GET', url, params, success, failure)
  },
  post: function (params, success, failure,url) {
    return apiAxios('POST', url, params, success, failure)
  },
  put: function (url, params, success, failure) {
    return apiAxios('PUT', url, params, success, failure)
  },
  delete: function (url, params, success, failure) {
    return apiAxios('DELETE', url, params, success, failure)
  }
}
