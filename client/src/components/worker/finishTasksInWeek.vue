<template>
  <div>
  <div style="width:100%;white-space: nowrap;">
    <Card style="min-height: 360px">
      <p slot="title">近7天完成任务数</p>
      <!--<RadioGroup v-model="dataMode" type="button" @on-change="drawCard">-->
        <!--<Radio label="近7天数据"></Radio>-->
        <!--<Radio label="近30天数据"></Radio>-->
      <!--</RadioGroup>-->

      <div id="card1" style="min-height: 200px;width:90%;left:5%"></div>
      <!--<div id="card_1" style="height: 160px;width:90%;left:5%" v-if="showCard===1">-->
        <!--<Alert type="error" show-icon>加载失败，暂无数据</Alert>-->
      <!--</div>-->
    </Card>
  </div>
  <div style="width:100%;padding-top: 3%">
    <Card style="min-height: 360px">
      <p slot="title">近30天完成任务数</p>

      <div id="card2" style="min-height: 200px;width:90%;left:5%"></div>
      <!--<div id="card_1" style="height: 160px;width:90%;left:5%" v-if="showCard===1">-->
      <!--<Alert type="error" show-icon>加载失败，暂无数据</Alert>-->
      <!--</div>-->
    </Card>
  </div>
  </div>
</template>

<script>
  import workerAPI from '../../api/worker'

  export default {
    name: "finishTasksInWeek",
    data() {
      return {
        finishTasks: [],
        showCard: 0,
        dataMode: '近7天数据',
        xData1: [],
        xData2: [],
        yData: [],
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
              var workerSta = res.object;
              this.finishTasks = workerSta.submitTasks;
              this.drawCard();
            }
            else if (res.message === 'TIMEOUT') {
              this.$Message.error('您因为太久未操作而被登出！');
              this.$router.push({
                path: '/',
              });
            }
            else {
              this.showCard = 1;
            }
          },
          (err) => {
            this.showCard = 1;
          }
        )
      },
      drawCard() {
        let myChart1 = this.$echarts.init(document.getElementById('card1'));
        let myChart2 = this.$echarts.init(document.getElementById('card2'));

          let xData7 = [];
          for (let i = 0; i < 7; i++) {
            let dd = new Date();
            dd.setDate(dd.getDate() - (6 - i));//获取i天后的日期
            let m = dd.getMonth() + 1;//获取当前月份的日期
            let d = dd.getDate();
            xData7[i] = m.toString() + '/' + d.toString();
            this.yData[i] = this.finishTasks[i + 23];
          }
          this.xData1 = xData7;
          // console.log(this.xData);

          let xData30 = [];
          for (let i = 0; i < 30; i++) {
            let dd = new Date();
            dd.setDate(dd.getDate() - (29 - i));//获取i天后的日期
            let m = dd.getMonth() + 1;//获取当前月份的日期
            let d = dd.getDate();
            xData30[i] = m.toString() + '/' + d.toString();
          }
          this.xData2 = xData30;
          this.yData = this.finishTasks;
          // console.log(this.xData);



        // console.log(y);
        // console.log(m);
        // console.log(d);

        myChart1.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['完成任务数']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: this.xData1
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '完成任务数',
              type: 'line',
              stack: '总数',
              data: [
                this.yData[23],
                this.yData[24],
                this.yData[25],
                this.yData[26],
                this.yData[27],
                this.yData[28],
                this.yData[29],
              ]
              // data: this.finishTasks
            }
          ]
        });

        myChart2.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['完成任务数']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: this.xData2
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '完成任务数',
              type: 'line',
              stack: '总数',
              data: this.yData
              // data: this.finishTasks
            }
          ]
        });

      }
    }
  }
</script>

<style scoped>

</style>
