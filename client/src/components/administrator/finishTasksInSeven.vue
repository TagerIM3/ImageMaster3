<template>
  <div style="height: 250px;width:100%;white-space: nowrap">
    <Card>
      <p slot="title">近7天发布与完成任务数</p>
      <div id="card7" style="height: 160px;width:90%;left:5%" v-if="showCard7 === 0"></div>
      <div id="card7-1" style="height: 160px;width:90%;left:5%" v-if="showCard7 === 1">
        <Alert type="error" show-icon>加载失败，暂无数据</Alert>
      </div>
    </Card>
  </div>
</template>

<script>
  import adminAPI from '../../api/administrator'
  export default {
    name: "finishTasksInSeven",
    data() {
      return {
        showCard7: 0,
        submitTasks: [],
        finishTasks: [],
        xData: [],
        yData1: [],
        yData2: [],
      }
    },
    mounted() {
      this.showData();
      // this.drawCard7();
    },
    methods: {
      showData() {
        adminAPI.getStatistics((res) => {
          console.log(res);
          if(res.message === 'SUCCESS'){
            var adminSta = res.object;
            this.submitTasks = adminSta.submitTasks;
            this.finishTasks = adminSta.finishTasks;

            let xData7 = [];
            for (let i = 0; i < 7; i++) {
              let dd = new Date();
              dd.setDate(dd.getDate() - (6 - i));//获取i天后的日期
              let m = dd.getMonth() + 1;//获取当前月份的日期
              let d = dd.getDate();
              xData7[i] = m.toString() + '/' + d.toString();
              // this.yData[i] = this.createUsers[i + 23];
            }
            this.xData = xData7;
            console.log(xData7);
            this.drawCard7();
          }else{
            this.showCard7=1;
          }
          },
          (err) => {
            this.showCard7=1;
            this.$Message.error(err.message);
          })
      },
      drawCard7() {
        let myChart7 = this.$echarts.init(document.getElementById('card7'));
        myChart7.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['发布任务数', '完成任务数']
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
            data: this.xData
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '发布任务数',
              type: 'line',
              // stack: '总量',
              data: [
                this.submitTasks[23],
                this.submitTasks[24],
                this.submitTasks[25],
                this.submitTasks[26],
                this.submitTasks[27],
                this.submitTasks[28],
                this.submitTasks[29],
              ]
            },
            {
              name: '完成任务数',
              type: 'line',
              // stack: '总量',
              data: [
                this.finishTasks[23],
                this.finishTasks[24],
                this.finishTasks[25],
                this.finishTasks[26],
                this.finishTasks[27],
                this.finishTasks[28],
                this.finishTasks[29],
              ]
            }
          ]
        });
      }
    }
  }
</script>

<style scoped>

</style>
