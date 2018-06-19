<template>
  <div style="height: 250px;width:100%;white-space: nowrap">
    <Card>
      <p slot="title">近30天发布与完成任务数</p>
      <div id="card8" style="height: 160px;width:90%;left:5%"></div>
      <!--<div id="card8-1" style="height: 160px;width:90%;left:5%" v-if="showCard8 === 1">-->
        <!--<Alert type="error" show-icon>加载失败，暂无数据</Alert>-->
      <!--</div>-->
    </Card>
  </div>
</template>

<script>
  import adminAPI from '../../api/administrator'

  export default {
    name: "finishTasksInThirty",
    data() {
      return {
        showCard8: 0,
        submitTasks: [],
        finishTasks: [],
        submitData: [],
        finishData: [],
        xData: [],
      }
    },
    mounted() {
      this.showData();
      // this.drawCard8();
    },
    methods: {
      showData() {
        adminAPI.getStatistics((res) => {
            if (res.message === 'SUCCESS') {
              var adminSta = res.object;
              this.submitTasks = adminSta.submitTasks;
              this.finishTasks = adminSta.finishTasks;

              let xData30 = [];
              for (let i = 0; i < 30; i++) {
                let dd = new Date();
                dd.setDate(dd.getDate() - (29 - i));//获取i天后的日期
                let m = dd.getMonth() + 1;//获取当前月份的日期
                let d = dd.getDate();
                xData30[i] = m.toString() + '/' + d.toString();
              }
              this.xData = xData30;
              this.drawCard8();
            }
            else {

            }
          },
          (err) => {
            this.$Message.error(err.message);
          })
      },
      drawCard8() {
        let myChart8 = this.$echarts.init(document.getElementById('card8'));
        myChart8.setOption({
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
            // data: ['25-30', '20-25', '15-20', '10-15', '5-10', '0-5']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '发布任务数',
              type: 'line',
              // stack: '总量',
              data: this.submitTasks
            },
            {
              name: '完成任务数',
              type: 'line',
              // stack: '总量',
              data: this.finishTasks
            }
          ]
        });
      }
    }
  }
</script>

<style scoped>

</style>
