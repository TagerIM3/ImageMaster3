import remote from './remote'
import axios from 'axios'

export default {
  login(username_email, password, resolve, reject) {
    var data = {}
    data['username_email'] = username_email;
    data['password'] = password;
    // data.append(, password);
    remote.get( data, resolve, reject,"/user/login");
  },
  register(userPre, resolve, reject) {
    // axios({
    //   url:'http://106.14.178.184:8080/im3/user/register',
    //   method:'post',
    //   data:userPre,
    //   headers: {'content-type': 'application/json'},
    // }).then()

    remote.post(userPre, resolve,reject, "/user/register");
  },
  // isUsedEmail(email, resolve, reject) {
  //   var data = new FormData();
  //   data.append('email', email);
  //
  //
  //   remote.queryGet(data, resolve, reject, "/user/isUsedEmail");
  // },
  send(email, mode, resolve, reject) {
    var data = {}
    data['email'] = email;
    data['mode'] = mode;
    // axios({
    //     url:'http://106.14.178.184:8080/im3/user/send',
    //     method:'get',
    //     params:{'email':email,'mode':0},
    //     headers: {'content-type': 'application/json'},
    //   }).then()
    remote.get(data, resolve, reject, "/user/send");
  },
  modify(userPre, resolve, reject) {
    remote.post(userPre, resolve, reject, "/user/modifyPassword");
  }
}
