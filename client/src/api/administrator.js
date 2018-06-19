import remote from './remote'
import axios from "axios";

export default {
  login(key, resolve, reject) {
    var data = {}
    data['key'] = key;
    remote.get(data, resolve, reject, "/admin/login");
  },
  getStatistics(resolve, reject) {
    remote.get('', resolve, reject, "/admin/getStatistics");
  }
}
