<template>
  <div style="height: 250px;width:100%;white-space: nowrap">
    <Card>
      <p slot="title">近7天发布任务数</p>
      <div id="card5" style="height: 160px;width:90%;left:5%"></div>
    </Card>
  </div>
</template>

<script>
  import userAPI from '../../api/administrator'

  export default {
    name: "submitTasksInSeven",
    data(){
      return{
        xData: [],
        yData: [],
        submitTasks: [],
        finishTask: [],
      }
    },
    mounted() {
      this.drawCard5();
    },
    methods: {
      showData() {
        // console.log('ppp')
        userAPI.getStatistics((res) => {
            if (res.message === 'SUCCESS') {
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
                this.yData[i] = this.createUsers[i + 23];
              }
              this.xData = xData7;
              this.drawCard5();
              // this.$Message.success();
            }
            else {

            }
          },
          (err) => {
            this.$Message.error(err);
          })
      },
      drawCard5() {
        let myChart5 = this.$echarts.init(document.getElementById('card5'));

        let xData7 = [];
        for (let i = 0; i < 7; i++) {
          let dd = new Date();
          dd.setDate(dd.getDate() - (6 - i));//获取i天后的日期
          let m = dd.getMonth() + 1;//获取当前月份的日期
          let d = dd.getDate();
          xData7[i] = m.toString() + '/' + d.toString();
          this.yData[i] = this.createUsers[i + 23];
        }
        this.xData = xData7;
        myChart5.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['任务']
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
            data: ['6', '5', '4', '3', '2', '1']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '任务',
              type: 'line',
              stack: '总量',
              data: [120, 97, 78, 134, 90, 230]
            }
          ]
        });
      }
    }
  }
</script>

<style scoped>

</style>
