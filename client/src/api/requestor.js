import remote from './remote'
import axios from "axios/index";

export default {
  submitTask(taskPre, resolve, reject) {
    remote.post(taskPre, resolve, reject, "/requestor/submitTask");
  },
  uploadImages(file, resolve, reject) {
    let param = new FormData(); //创建form对象
    param.append('file',file);//通过append向form对象添加数据
    //console.log(file)
    axios({
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      method: 'POST',
      url: 'http://106.14.178.184/im3/requestor/uploadImages',
      data: param,
      withCredentials: true
    })
      .then((res)=>{resolve(res.data)})
      .catch((err)=>{reject(err)});

  },

  modifyAvatar(file, resolve, reject) {
    let param = new FormData(); //创建form对象
    param.append('file',file);//通过append向form对象添加数据
    //console.log(file)
    axios({
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      method: 'POST',
      url: 'http://106.14.178.184/im3/requestor/uploadAvatar',
      data: param,
      withCredentials: true
    })
      .then((res)=>{resolve(res.data)})
      .catch((err)=>{reject(err)});

  },

  modify(userInfo,res,err){
    remote.post(userInfo,res,err, "/requestor/modifyInfo");
  },

  getMessages(res, err) {
    remote.post("", res, err, "/requestor/getMessages");
  },
  finishTask(data,res,err){
    remote.get(data,res,err,"/requestor/finishTask")
  },
  deleteMessages(data,res,err){
    // axios({
    //     url:'http://106.14.178.184:8080/im3/requestor/deleteMessages',
    //     method:'post',
    //     params:{'messageId':1},
    //     headers: {'content-type': 'application/json'},
    //   }).then()
    remote.get(data,res,err,"/requestor/deleteMessages")
  },
  getFinishTasks(res,err){
    remote.post("", res, err, "/requestor/getFinishTasks");
  },
  getOngoingTASKS(res,err){
    remote.post("", res, err, "/requestor/getOngoingTasks");
  },
  getStatistics(res,err){
    remote.get("", res, err, "/requestor/getStatistics");
  },
  recharge(code, resolve, reject){
    let data = {};
    data['code']=code;
    remote.get(data, resolve, reject, "/requestor/recharge");
  },
  getRequestor(resolve, reject){
    remote.get('',resolve, reject, "/requestor/getRequestor");
  },
}
