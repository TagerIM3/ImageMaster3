<template>
  <div style="padding-top: 3%">


    <div style="width:20%;height: 100px;float:left;background-color: white;position: relative;margin-right: 5%">
      <div
        style="position: relative;width:30%;height:100%;background-color: deepskyblue;padding-top: 5%;padding-left: 3%;float: left">
        <Icon type="ios-person" size="80"></Icon>
      </div>
      <div style="font-size: 15px;font-family: 幼圆;padding-left: 35%;padding-top: 5%">
        <p style="float: left;color: deepskyblue">效率：</p>
        <p style="float: left;color: deepskyblue;font-family: 'Arial Rounded MT Bold'">{{efficiency}}</p><br>
        <p style="float: left;color: deepskyblue">准确性：</p>
        <p style="float: left;color: deepskyblue;font-family: 'Arial Rounded MT Bold'">{{accuracy}}</p><br>
        <p style="float: left;color: deepskyblue">已完成任务：</p>
        <p style="color: deepskyblue;font-family: 'Arial Rounded MT Bold'">{{tasks}}</p>
      </div>
    </div>


    <div style="width:20%;height: 100px;float:left;background-color: white;margin-right:5%">
      <div
        style="position: relative;width:30%;height:100%;background-color: red;padding-top: 10%;padding-left: 4%;float: left">
        <Icon type="heart" size="60"></Icon>

      </div>
      <div style="font-size: 15px;font-family: 幼圆;padding-left: 35%;padding-top: 5%">
        <p style="float: left;color:red">我的偏好：</p><br>
        <p style="color: red;max-width:100%">{{preStr}}</p>
      </div>
    </div>


    <div style="width:20%;height: 100px;float:left;background-color: white;margin-right:5%">
      <div
        style="position: relative;width:30%;height:100%;background-color: darkorange;padding-top: 5%;padding-left: 3%;float: left">
        <Icon type="android-done" size="70"></Icon>

      </div>
      <div style="font-size: 15px;font-family: 幼圆;padding-left: 35%;padding-top: 5%">
        <p style="float: left;color:darkorange">我的擅长：</p><br>
        <p style="color:darkorange;max-width:100%">{{goodsStr}}</p>
      </div>
    </div>

    <div style="width:20%;height: 100px;float:left;background-color: white;margin-right:5%">
      <div
        style="position: relative;width:30%;height:100%;background-color: green;padding-top: 8%;padding-left: 3%;float: left">
        <Icon type="person-stalker" size="60"></Icon>
      </div>
      <div style="font-size: 15px;font-family: 幼圆;padding-left: 35%;padding-top: 5%">
        <p style="float: left;color:green">排名：</p><br>
        <div style="position:relative;left:35%">
          <p style="color:green;font-family: 'Arial Rounded MT Bold';font-size: 30px">{{ranking}}</p>
        </div>

      </div>
    </div>


    <!--<div style="position:relative;width:20%;height: 100px;float: left;padding-left:50%;background-color: black">-->

    <!--</div>-->

    <!--<Card style="width:20%;height: 100px;float: left;left:12%"></Card>-->
    <!--<Card style="width:20%;height: 100px;float: left;left:16%"></Card>-->


    <!--<div style="height: 280px;width:45%;padding-top: 12%">-->
    <!--<Card>-->
    <!--<p slot="title">个人信息</p>-->
    <!--<div id="card1" style="height: 160px;width:90%;left:5%">-->
    <!--<p style="float: left">我的效率：</p>-->
    <!--<p style="float: left">{{efficiency}}</p>-->
    <!--<p>分钟/张</p>-->
    <!--<p style="float: left">我的准确性：</p>-->
    <!--<p style="float: left">{{accuracy}}</p>-->
    <!--<p>%</p>-->
    <!--<p style="float: left">已完成任务数：</p>-->
    <!--<p>{{tasks}}</p>-->
    <!--<p style="float: left">我的偏好：</p>-->
    <!--<p>{{preStr}}</p>-->
    <!--<p style="float: left">我的擅长：</p>-->
    <!--<p>{{goodsStr}}</p>-->
    <!--<p style="float: left">我的分数：</p>-->
    <!--<p>{{grade}}</p>-->
    <!--<p style="float: left">我的排名：</p>-->
    <!--<p>{{ranking}}</p>-->
    <!--</div>-->
    <!--&lt;!&ndash;<div id="card1-1" style="height: 160px;width:90%;left:5%" v-if="showCard1===1">&ndash;&gt;-->
    <!--&lt;!&ndash;<Alert type="error" show-icon>加载失败，暂无数据</Alert>&ndash;&gt;-->
    <!--&lt;!&ndash;</div>&ndash;&gt;-->
    <!--</Card>-->
    <!--</div>-->

    <div style="padding-top: 12%;">
      <div style="height: 100%">
        <accuracy></accuracy>
        <finish-tasks-in-week></finish-tasks-in-week>
      </div>

      <!--<finish-tasks-in-month></finish-tasks-in-month>-->
      <!--<div>-->
        <!--<top-ranking></top-ranking>-->
      <!--</div>-->

    </div>


  </div>
</template>

<script>
  import workerAPI from '../../api/worker'
  import finishTasksInWeek from './finishTasksInWeek'
  import finishTasksInMonth from './finishTasksInMonth'
  import topRanking from './topRanking'
  import accuracy from './accuracy'
  export default {
    name: "workerStatistics",
    components: {finishTasksInWeek, topRanking, finishTasksInMonth, accuracy},
    data() {
      return {
        efficiency: 0.0,
        accuracy: 0,
        tasks: 0,
        preferences: [],
        goods: [],
        preStr: '',
        goodsStr: '',
        usernames: [],
        grades: [],
        ranking: '',
        grade: 0.0,
        finishTasks: []
      }
    },
    mounted() {
      this.showData();
    },
    methods: {
      showData() {
        workerAPI.getStatistics((res) => {
          console.log(res);
            if (res.message === 'SUCCESS') {
              // console.log(res.object);
              var workerSta = res.object;
              this.efficiency = workerSta.efficiency;
              this.accuracy = workerSta.accuracy;
              this.tasks = workerSta.tasks;
              this.preferences = workerSta.preferences;
              this.goods = workerSta.goods;
              this.ranking = workerSta.ranking;
              for (var i = 0; i < this.preferences.length; i++) {
                this.preStr += this.preferences[i] + ' ';
              }
              // this.preStr.substr(0, this.preStr.length - 1);
              for (var i = 0; i < this.goods.length; i++) {
                this.goodsStr += this.goods[i] + ' ';
              }
              this.preStr = '人物 风景';
              this.goodsStr = '人物 车辆';
              this.ranking = '7/45';
              // this.goodsStr.substr(0, this.goodsStr.length - 1);
            }
            else if (res.message === 'TIMEOUT') {
              this.$Message.error('您因为太久未操作而被登出！');
              this.$router.push({
                path: '/',
              });
            }
            else {
              this.$Message.error('网络出错');
            }
          },
          (err) => {
            this.$Message.error(err.message);
          })
      }
    }
  }
</script>

<style scoped>

</style>
