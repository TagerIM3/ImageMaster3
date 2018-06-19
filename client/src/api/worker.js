import remote from './remote'
import axios from "axios/index";

export default {
  getWorker(resolve, reject){
    remote.get('',resolve, reject, "/worker/getWorker");
  },
  modify(user, resolve, reject){
    remote.post(user, resolve,reject, "/worker/modify");
  },

  getTasks(resolve, reject) {
    remote.get('', resolve, reject, "/worker/getTasks");
  },
  getTasksByPreference(resolve,reject){
    remote.get('', resolve, reject, "/worker/getTasksByPreference");
  },
  getRecommendTasks(resolve, reject){
    remote.get('', resolve, reject, "/worker/getRecommendTasks");
  },
  getSkilledTasks(resolve, reject){
    remote.get('', resolve, reject, "/worker/getSkilledTasks");
  },
  getRewardTasks(resolve, reject){
    remote.get('', resolve, reject, "/worker/getRewardTasks");
  },
  acceptTask(taskId,resolve,reject){
    var data = {};
    data['taskId'] = taskId;
    remote.get(data, resolve, reject, "/worker/acceptTask");
  },
  getWorks(resolve, reject) {
    remote.get('', resolve, reject, "/worker/getWorks");
  },
  getSubmitWorks(resolve, reject){
    remote.get('',resolve, reject, "/worker/getSubmitWorks");
  },
  getFinishWorks(resolve, reject){
    remote.get('', resolve, reject, "/worker/getFinishWorks");
  },
  saveWork(work, resolve, reject){
    remote.post(work.work,resolve, reject, "/worker/saveWork");
  },
  submitWork(work, resolve, reject){
    remote.post(work.work, resolve, reject, "/worker/submitWork");
  },
  quitWork(workId, resolve,reject){
    var data= {};
    data['workId'] = workId;
    remote.get(data,resolve, reject, "/worker/quitWork");
  },
  getMessages(resolve, reject){
    remote.get('', resolve,reject, "/worker/getMessages");
  },
  deleteMessage(messageId, resolve, reject){
    var data={};
    data['messageId'] = messageId;
    remote.get(data, resolve,reject, "/worker/deleteMessage");
  },
  readMessage(messageId, resolve, reject){
    var data={};
    data['messageId'] = messageId;
    remote.get(data, resolve,reject, "/worker/readMessage");
  },
  getStatistics(resolve, reject){
    remote.get('', resolve, reject, "/worker/getStatistics");
  },
  getPointEntries(resolve, reject){
    remote.get('',resolve, reject, "/worker/getPointEntries");
  }


}
